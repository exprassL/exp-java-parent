package com.exp.toolkit.image;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * 缩略图生成器
 */
@Slf4j
public class ThumbnailGenerator {
    
    /**
     * 图片生成缩略图
     *
     * @param original  原图路径
     * @param thumbnail 缩略图路径
     * @return true-缩略图成功，false-无需压缩，或缩略图失败，缩略图路径即原图路径
     */
    public static void generate(String original, String thumbnail) {
        generate(original, thumbnail, 1024 * 10);
    }
    
    /**
     * 为图片生成限定文件大小的缩略图。
     * 最多只压缩10次，若压缩10次后，就算缩略图文件大小仍超出限定值，也不再继续压缩。
     *
     * @param original  原图路径
     * @param thumbnail 缩略图路径
     * @param targetLength 目标缩略图文件大小的最大许可值
     * @return 若原图文件大小不超过限定值，或压缩过程发生异常，则返回原图路径；否则返回缩略图路径
     */
    public static String generate(String original, String thumbnail, int targetLength) {
        log.info("生成缩略图开始：{}", original);
        try {
            byte[] imgBytes = readImage(original);
            if (imgBytes.length <= targetLength) {
                log.info("生成缩略图结束：无需压缩，原图大小仅为 {} Bytes", imgBytes.length);
                return original;
            }
            
            int round = 0;
            do {
                try (ByteArrayInputStream bais = new ByteArrayInputStream(imgBytes);
                     ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                    
                    Thumbnails.of(bais).scale(0.5D).outputQuality(0.5D).toOutputStream(baos);
                    
                    imgBytes = baos.toByteArray();
                }
                round++;
            } while (imgBytes.length > 1024 * 10 && round < 10);
            
            FileUtils.writeByteArrayToFile(new File(thumbnail), imgBytes);
            log.info("生成缩略图结束：经过 {} 次压缩，获得缩略图大小为 {} Bytes", round, imgBytes.length);
            
            return thumbnail;
            
        } catch (IOException e) {
            log.error("生成缩略图出错：使用原图", e);
            return original;
        }
    }
    
    /**
     * 读取图片到字节数组
     *
     * @param imagePath 图片路径
     * @return
     * @throws IOException
     */
    private static byte[] readImage(String imagePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(imagePath);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            while (fis.read(buffer) != -1) {
                bos.write(buffer);
            }
            return bos.toByteArray();
        }
    }
}
