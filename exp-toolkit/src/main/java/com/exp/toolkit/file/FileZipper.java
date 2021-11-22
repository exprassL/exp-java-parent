package com.exp.toolkit.file;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public final class FileZipper {
    
    /**
     * IO操作时允许的默认缓冲区大小
     */
    private static int defaultBufferSize = 1024 * 1024; // 1Mb
    
    /**
     * zip打包，递归，注意调用后释放{@link ZipOutputStream zos}流
     *
     * @param file 代表要打包的路径的{@link File}对象
     * @param zos  zip输出流，需要调用方释放资源
     * @param parent 打包父路径，一般是外部调用（相对于方法内部递归调用）此方法时，参数file的父级路径，
     *               以{@link File#separator /或者\}结尾，打包结果中不包含此路径
     * @param bufferSize 指定打包时使用的缓冲区大小，为0或小于0时，表示使用默认值{@link #defaultBufferSize 1024 * 1024}
     * @throws IOException  打包过程中可能发生IO错误，不捕获，直接抛出
     */
    public static void zipFile(File file, ZipOutputStream zos, String parent, int bufferSize) throws IOException {
        String path = file.getPath();
        if (file.isDirectory()) {
            //noinspection ConstantConditions
            if (file.listFiles().length == 0) {
                log.info("打包空目录：{}", path);
                doZip(zos, path.replace(parent, "") + File.separator);
                return;
            }
            log.info("进入非空目录：{}", path);
            for (File f : Objects.requireNonNull(file.listFiles())){
                zipFile(f, zos, parent, bufferSize);
            }
        } else {
            log.info("打包文件：{}", path);
            doZip(file, zos, path.replace(parent, ""), bufferSize);
        }
    }
    
    /**
     * 空目录加入Zip包
     *
     * @param zos  zip输出流
     * @param path file路径，相对打包父路径
     * @throws IOException  打包过程中可能发生IO错误，不捕获，直接抛出
     */
    private static void doZip(ZipOutputStream zos, String path) throws IOException {
        ZipEntry entry = new ZipEntry(path);
        zos.putNextEntry(entry);
    }
    
    /**
     * 文件加入Zip包
     *
     * @param file Zip打包文件
     * @param zos  zip输出流
     * @param path file路径，相对打包父路径，如dir1/dir2/file.ext
     * @param bufferSize 指定打包时使用的缓冲区大小
     * @throws IOException  打包过程中可能发生IO错误，不捕获，直接抛出
     */
    private static void doZip(File file, ZipOutputStream zos, String path, int bufferSize) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
            ZipEntry entry = new ZipEntry(path);
            zos.putNextEntry(entry);
            bufferSize = bufferSize > 0 ? bufferSize : FileZipper.defaultBufferSize;
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                zos.write(buffer, 0, len);
            }
        }
    }
}
