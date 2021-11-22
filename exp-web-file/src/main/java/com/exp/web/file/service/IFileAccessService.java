package com.exp.web.file.service;

import com.exp.toolkit.basic.StringUtils;
import com.exp.web.exception.Asserts;
import com.exp.web.file.pojo.FileInfo;
import com.exp.web.file.util.FileAccessUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.*;

/**
 * 文件系统访问接口，提供一组默认方法，用于文件访问（预览、下载等）；
 * 引用方需实现该接口，并提供一个<code> Service Bean </code>到<code> Spring </code>容器。
 */
public interface IFileAccessService {
    
//    Logger log = LoggerFactory.getLogger(IFileAccessService.class);
    
    /**
     * 打开指定目录
     *
     * @param file 指定目录的{@link File}对象
     * @param root 文件存储根路径
     * @return  返回目录下的内容
     */
    default List<FileInfo> open(File file, String root) {
        String targetPath;
        if (file.getPath().equals(new File(root).getPath())) {
            targetPath = "./";
        } else {
            targetPath = file.getPath().replace(root, "");
        }
        Asserts.isTrue(file.isDirectory(), "目标路径不是目录：%s", targetPath);
    
        List<FileInfo> fileList = new ArrayList<>();
        // 当前目录
        FileInfo fi = new FileInfo();
        fi.setName(targetPath.replace(File.separator, "/"));
        fi.setLastModifiedDate(new Date(file.lastModified()));
        String parent = file.getParent();
        if (new File(root).getPath().equals(parent)) {
            fi.setParentPath("");
        } else if (parent.startsWith(root)) {
            parent = parent.replace(root, "");
            fi.setParentPath(StringUtils.str2hex(parent).replaceAll("\\\\", "")); // 需作为前台请求参数，不能以.或/开头
        }
        fileList.add(fi);
    
        Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(f -> {
            FileInfo fileInfo = new FileInfo();
            String name = f.getName();
            fileInfo.setName(name);
            fileInfo.setFile(f.isFile());
            int idx = name.lastIndexOf(".");
            if (idx >= 0) {
                fileInfo.setExt(name.substring(idx));
            }
            fileInfo.setLastModifiedDate(new Date(f.lastModified()));
            fileInfo.setPath(StringUtils.str2hex(f.getPath().replace(root, "")).replaceAll("\\\\", ""));
            fileList.add(fileInfo);
        });
    
        return fileList;
    }
    
    /**
     * 预览文本文件
     *
     * @param file
     * @param root
     * @param response
     */
    default void read(File file, String root, HttpServletResponse response) {
        String targetPath = file.getPath().replace(root, "");
        Asserts.isTrue(file.exists(), "目标文件或目录不存在：%s", targetPath);
        FileAccessUtil.read(file, response);
    }
    
    /**
     * 下载文件，或打包下载目录
     *
     * @param file 指定要下载的文件或目录
     * @param root 文件存储根路径
     * @param response
     */
    default void get(File file, String root, HttpServletResponse response) {
        String targetPath = file.getPath().replace(root, "");
        Asserts.isTrue(file.exists(), "目标文件或目录不存在：%s", targetPath);
    
        if (file.isFile()) {
            FileAccessUtil.download(file, response);
        } else {
            FileAccessUtil.downloadDir(file, response, root);
        }
    }
}
