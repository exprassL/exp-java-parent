package com.exp.toolkit;

import com.exp.toolkit.image.ImageUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class ToolkitTest {
    
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }
    
    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }
    
    @org.junit.jupiter.api.Test
    void generateThumbnail() {
    }
    
    @org.junit.jupiter.api.Test
    void testGenerateThumbnail() {
    }
    
    @Test
    void test() {
        System.out.println(Math.PI % 1);
    }
    
    @SneakyThrows
    @org.junit.jupiter.api.Test
    void flip() {
        URL url = new URL("https://fengyuanchen.github.io/cropperjs/images/picture.jpg");
        BufferedImage image = ImageIO.read(url);
        ImageUtils.flip(image, -1, -1);
        ImageIO.write(image, "jpg", new File("E:\\Users\\Exprass\\Downloads\\flip-xy.jpg"));
    }
    
    @SneakyThrows
    @Test
    void rotate() {
        URL url = new URL("https://fengyuanchen.github.io/cropperjs/images/picture.jpg");
//        URL url = new URL("https://picsum.photos/1280/720");
        BufferedImage image = ImageIO.read(url);
        BufferedImage img;
        int degree;
        for (int i = 0; i < 5; i++) {
            degree = (int)(Math.round(Math.random() * 360)) * (Math.random() > 0.5D ? 1 : -1);
            img = ImageUtils.rotate(image, degree);
            ImageIO.write(img, "png", new File("E:\\Users\\Exprass\\Downloads\\rotate\\ " + degree + ".png"));
        }
    }
}
