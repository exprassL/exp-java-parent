package com.exp.web.file.util;

import com.exp.web.configer.WebAppEnvironment;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipOutputStream;

import static com.exp.toolkit.file.FileZipper.zipFile;
import static com.exp.web.exception.WebBizException.produce;

/**
 * 文件系统访问工具
 */
@Slf4j
public final class FileAccessUtil {
    
    /**
     * 读取配置中定义的IO操作允许的缓冲区大小，未配置则返回默认值1024×1024
     */
    private static final int BUFFER_SIZE = WebAppEnvironment.getIntProperty("file.io.buffer-size", 1024 * 1024);
    
    /**
     * 在线预览文本文件
     *
     * @param file
     * @param response
     * @noinspection DuplicatedCode
     */
    public static void read(File file, HttpServletResponse response) {
        String fileName = file.getName();
        response.reset();
        response.setContentType("text/plain;charset=UTF-8");
        response.setHeader("Content-Disposition", "inline; filename*=UTF-8''" + fileName);
        byte[] bytes = (fileName + "\n--------------------以下为文件内容:\n\n\n").getBytes(StandardCharsets.UTF_8);
        response.addHeader("Content-Length", Long.toString(file.length() + bytes.length));
        byte[] buffer = new byte[BUFFER_SIZE];
        try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            FileInputStream fis = new FileInputStream(file)) {
            bos.write(bytes);
            int len;
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
        } catch (IOException e) {
            throw produce(e,"文件[%s]下载失败", fileName);
        }
    }
    
    /**
     * 下载文件
     *
     * @param file 要下载的文件
     * @param response Http响应
     * @noinspection DuplicatedCode
     */
    public static void download(File file, HttpServletResponse response) {
        String fileName = file.getName();
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.addHeader("Content-disposition", "attachment;filename*=UTF-8''" + fileName);
        response.addHeader("Content-Length", Long.toString(file.length()));
        byte[] buffer = new byte[BUFFER_SIZE];
        try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            FileInputStream fis = new FileInputStream(file)) {
            int len;
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
        } catch (IOException e) {
            throw produce(e,"文件[%s]下载失败", fileName);
        }
    }
    
    /**
     * @param file 要下载的目录
     * @param response  Http响应
     * @param root     存储文件根路径，不应该出现在异常信息（可能会显示到前端页面）中，以免泄露服务器的文件系统信息
     */
    public static void downloadDir(File file, HttpServletResponse response, String root) {
        String fileName = file.getName();
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + fileName + ".zip");
        
        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            zipFile(file, zos, file.getParent() + File.separator, 0);
        } catch (IOException | IllegalStateException e) {
            throw produce(e, "打包下载出错：%s", file.getPath().replace(root, ""));
        }
    }
}
