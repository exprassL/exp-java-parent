package com.exp.web.file.ctrl;

import com.exp.model.base.BaseResult;
import com.exp.toolkit.basic.StringUtils;
import com.exp.web.aop.annotation.Hex2StringPointCut;
import com.exp.web.context.AppContextUtil;
import com.exp.web.exception.Asserts;
import com.exp.web.file.service.IFileAccessService;
import com.exp.model.response.MapResult;
import com.exp.model.response.RedirectResult;

import com.exp.web.file.buffer.BufferPool;
import com.exp.web.file.pojo.FileInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.UUID;

import static com.exp.web.file.buffer.BufferPool.rootDirectory;

/**
 * 文件系统访问
 */
@RestController
@RequestMapping("/access")
public class FileAccessCtrl {

    @Resource
    private IFileAccessService fileAccessService;

    /**
     * 打开指定目录，若指定路径不是目录而是文件，则转发到文件下载
     *
     * @param path    指定路径，相对于{@link BufferPool#rootDirectory}
     * @param request
     * @return
     */
    @GetMapping(value = { "/open/{path}", "/open/", "/open" })
    @Hex2StringPointCut
    public BaseResult open(@PathVariable(name = "path", required = false) String path, HttpServletRequest request) {
        File file;
        if (StringUtils.isBlank(path)) {
            file = new File(rootDirectory);
        } else {
            file = new File(rootDirectory + path);
            Asserts.isTrue(file.exists(), "文件或目录不存在：%s", path);

            if (file.isFile()) { // 文件缓存到当前线程，并转发到文件下载请求
                String uuid = UUID.randomUUID().toString();
                HttpSession session = request.getSession();
                session.setMaxInactiveInterval(60 * 5); // 5分钟
                session.setAttribute(uuid, file);
                return new RedirectResult().redirectTo(AppContextUtil.getContextPath() + "/access/read?uuid=" + uuid);
            }
        }
        MapResult result = new MapResult();
        List<FileInfo> infoList = fileAccessService.open(file, rootDirectory);
        FileInfo fileInfo = infoList.remove(0);
        result.put("thisFile", fileInfo);
        result.put("fileList", infoList);
        return result;
    }

    /**
     * 文件预览或下载
     *
     * 纯文本文件可以预览，纯文本格式 FIXME 应配置化
     *
     * @param uuid     Session中临时存储文件对象的Key
     * @param request
     * @param response
     */
    @GetMapping(value = "/read")
    public void read(String uuid, HttpServletRequest request, HttpServletResponse response) {
        File file = (File) request.getSession().getAttribute(uuid);
        String name = file.getName();
        Asserts.isTrue(file.exists(), "文件不存在：%s", name);
        if (name.endsWith(".txt") || name.endsWith(".sql") || name.endsWith(".properties")) {
            // 在线预览纯文本文件
            fileAccessService.read(file, rootDirectory, response);
        } else {
            // 在线预览图片：可下载文件的路径，放到html src标签
            // 下载文件
            fileAccessService.get(file, rootDirectory, response);
        }
        // FIXME 音乐或视频文件可否直接在页面生成对应的标签进行播放？PDF或word预览？
    }

    /**
     * 下载指定文件或目录
     *
     * @param path     指定文件或目录路径
     * @param response
     */
    @GetMapping(value = "/get/{path}")
    @Hex2StringPointCut
    public void get(@PathVariable(name = "path") String path, HttpServletResponse response) {
        File file = new File(rootDirectory + path);
        fileAccessService.get(file, rootDirectory, response);
    }
}
