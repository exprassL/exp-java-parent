package com.exp.toolkit.image;

import com.exp.toolkit.basic.ArrayUtils;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;

import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 图片工具
 *
 * @noinspection DuplicatedCode
 */
@Slf4j
public final class ImageUtils {
    
    /**
     * 图片生成缩略图
     *
     * @param original  原图路径
     * @param thumbnail 缩略图路径
     * @return true-缩略图成功，false-无需压缩，或缩略图失败，缩略图路径即原图路径
     */
    public static String generateThumbnail(String original, String thumbnail) {
        return generateThumbnail(original, thumbnail, 1024 * 10);
    }
    
    /**
     * 为图片生成限定文件大小的缩略图。
     * 最多只压缩10次，若压缩10次后，就算缩略图文件大小仍超出限定值，也不再继续压缩。
     *
     * @param original     原图路径
     * @param thumbnail    缩略图路径
     * @param targetLength 目标缩略图文件大小的最大许可值
     */
    public static String generateThumbnail(String original, String thumbnail, int targetLength) {
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
    
    /**
     * 图片翻转
     *
     * @param image 表示待翻转图片的对象
     * @param x     水平翻转：1-正常，-1-翻转
     * @param y     垂直翻转：1-正常，-1-翻转
     */
    public static void flip(BufferedImage image, int x, int y) {
        int direction = x + y * 10;
        if (direction == 11) {
            return; // 无翻转
        }
        int height = image.getHeight();
        int width = image.getWidth();
        int[] rgb = image.getRGB(0, 0, width, height, null, 0, width);
        switch (direction) {
            case 9: // 水平翻转
                for (int r = 0; r < height; r++) {
                    for (int c = 0, half = width / 2; c < half; c++) {
                        int left = r * width + c;
                        int right = (r + 1) * width - c - 1;
                        int tmp = rgb[left];
                        rgb[left] = rgb[right];
                        rgb[right] = tmp;
                    }
                }
                break;
            case -9: // 垂直翻转
                for (int i = 0, max = height / 2 * width; i < max; i++) {
                    int r = i / width;
                    int o = (height - 2 - r) * width + i % width;
                    int tmp = rgb[i];
                    rgb[i] = rgb[o];
                    rgb[o] = tmp;
                }
                break;
            case -11: // 同时翻转
                ArrayUtils.reverse(rgb);
                break;
            default:
                break;
        }
        image.setRGB(0, 0, width, height, rgb, 0, width);
    }
    
    /**
     * 图片旋转。
     *
     * @param image  图片
     * @param degree 旋转角度，顺时针为正
     * @return
     */
    public static BufferedImage rotate(BufferedImage image, int degree) {
        // 转为弧度，计算三角函数值
        double radian = Math.toRadians(degree);
        double sin = Math.sin(radian);
        double cos = Math.cos(radian);
        
        // 高、宽
        int h = image.getHeight();
        int w = image.getWidth();
        log.info("宽高：({}, {})", w, h);
        
        /*
         * x' = cosΔ * (x) - sinΔ * (y)
         * y' = cosΔ * (y) + sinΔ * (x)
         * 顶点旋转，注意坐标系的向右、向下为正方向，右转（顺时针）为角度增大，Δ为正，反之减小为负
         */
        // 左上角: [0, 0]
        // 左下角: [0, h)
        double w2 = cos * (0) - sin * (h);
        double h2 = cos * (h) + sin * (0);
        // 右上角: (w, 0]
        double w3 = cos * (w) - sin * (0);
        double h3 = cos * (0) + sin * (w);
        // 右下角: (w, h)
        double w4 = cos * (w) - sin * (h);
        double h4 = cos * (h) + sin * (w);
        
        // 转换后高、宽
        double xMax = Math.max(Math.max(Math.max(0, w2), w3), w4);
        double xMin = Math.min(Math.min(Math.min(0, w2), w3), w4);
        int nw = (int) Math.ceil(xMax - xMin);
        double yMax = Math.max(Math.max(Math.max(0, h2), h3), h4);
        double yMin = Math.min(Math.min(Math.min(0, h2), h3), h4);
        int nh = (int) Math.ceil(yMax - yMin);
        log.info("旋转{}°后宽高：({}, {})", degree, nw, nh);
        
        // 构建转换后的图片
        BufferedImage img = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        double offsetX = -xMin;
        double offsetY = -yMin;
        for (int hi = 0; hi < nh; hi++) {
            for (int wi = 0; wi < nw; wi++) {
                int rgb = rotate(wi, hi, offsetX, offsetY, sin, cos, image, w, h);
                img.setRGB(wi, hi, rgb);
            }
        }
        return img;
    }
    
    /**
     * 计算新图中坐标为 (x, y) 的像素点，对应原图上的坐标位置，进而计算该位置的4个最相关像素点的像素值的线性插值。
     * 计算方法：<ol>
     * <li>假设已经旋转并位置偏移完成，新图片的尺寸与原图已经不一致，且左上角位于原点；</li>
     * <li>新图片的每个整数坐标（像素点）对应原图的一个坐标（可能不会落在原图区域内）建立映射；</li>
     * <li>将映射中原图坐标（基本是小数）周围相邻最近像素点的像素值，作为映射中新图坐标所在像素点的像素值；</li>
     * <li>如果映射中的原图坐标落在原图区域以外（越界），像素值填充0；</li>
     * </ol>
     *
     * @param x       经过偏移处理后的 x 坐标
     * @param y       经过偏移处理后的 y 坐标
     * @param offsetX x 轴正向偏移量，即将旋转后左上角 (xLeft, yUp) 移动到 y 轴上，x 坐标偏移量，值为 -xLeft
     * @param offsetY x 轴正向偏移量，即将旋转后左上角 (xLeft, yUp) 移动到 x 轴上，y 坐标偏移量，值为 -yUp
     * @param sin     旋转角度的 sin 值
     * @param cos     旋转角度的 cos 值
     * @param image   原图
     * @param w       原图宽
     * @param h       原图高
     * @return 目标像素值
     */
    private static int rotate(int x, int y, double offsetX, double offsetY, double sin, double cos, BufferedImage image, int w, int h) {
        // 旋转后（偏移前）的左上角坐标
        double x0 = x - offsetX;
        double y0 = y - offsetY;
        /*
         * 对应原图坐标的映射关系表达式（未位移时）：
         * x = cosΔ * x0 + sinΔ * y0
         * y = cosΔ * y0 - sinΔ * x0
         */
        double xo = cos * (x0) + sin * (y0);
        double yo = cos * (y0) - sin * (x0);
        
        return getRGBPixel(image, (int)Math.round(xo), (int)Math.round(yo), w, h);
    }
    
    /**
     * 获取图片指定坐标像素点的像素值
     *
     * @param image 图片
     * @param x     x 坐标
     * @param y     y 坐标
     * @param w     图片宽
     * @param h     图片高
     * @return 图片指定坐标的像素值，或者坐标落在图片之外时返回0
     */
    private static int getRGBPixel(BufferedImage image, int x, int y, int w, int h) {
        if (x < 0 || x >= w || y < 0 || y >= h) {
            return 0;
        }
        return image.getRGB(x, y);
    }
    
}
