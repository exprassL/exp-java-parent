package com.exp.toolkit.basic;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.IllegalFormatException;
import java.util.UUID;

/**
 * 字符串工具
 */
@Slf4j
public final class StringUtils {
    
    /**
     * 16进制字符按升序组成的char数组
     */
    private static final char[] HEX_DIGITS =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    
    /**
     * uuid without "-"
     * @return {@link UUID#randomUUID()}，去掉其中的“-”
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    /**
     * null或空串
     *
     * @param s 目标字符串
     * @return true：字符串为null或无内容，否则：false
     */
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }
    
    /**
     * @see #isEmpty(String)
     * @param s 目标字符串
     * @return false：字符串为null或无内容，否则：true
     */
    public static boolean isNotEmpty(String s) {
        return !isEmpty(s);
    }
    
    /**
     * null、空串、空白
     * @param s 目标字符串
     * @return  true：字符串为空白，否则：false
     */
    public static boolean isBlank(String s) {
        return isEmpty(s) || s.trim().length() == 0;
    }
    
    /**
     * @see #isBlank(String)
     * @param s 目标字符串
     * @return  true：字符串非空白，否则：false
     */
    public static boolean isNotBlank(String s) {
        return !isBlank(s);
    }
    
    /**
     * 字符串格式化
     *
     * @param template 格式模板
     * @param parameters 格式化参数
     * @return 格式化的字符串
     * @throws IllegalFormatException 格式错误或格式与参数不匹配时
     */
    public static String format(String template, Object... parameters) {
        return String.format(template, parameters);
    }
    
    /**
     * hex解码
     *
     * @param hex 16进制表示的字符串
     * @return  普通字符串，可能是{@code null}
     */
    public static String hex2Str(String hex) {
        if (StringUtils.isEmpty(hex)) {
            log.info("hex无内容，无需转换：{}", hex);
            return hex;
        }
        
        log.info("转换前hex：" + hex);
        StringBuilder result = new StringBuilder();
        int offset = 0;
        int len = hex.length();
        
        while (offset < len) {
            if (hex.startsWith("\\u", offset)) {
                result.append((char)Integer.parseInt(hex.substring(offset + 2, offset + 6), 16));
                offset += 6;
            }else {
                result.append(hex.charAt(offset++));
            }
            
        }
        
        String rs = result.toString();
        log.info("转换后：" + rs);
        return rs;
    }
    
    /**
     * hex编码
     *
     * @param str 原始字符串
     * @return  16进制表示的字符串，可能是{@code null}
     */
    public static String str2hex(String str) {
        if (StringUtils.isEmpty(str)) {
            log.info("str无内容，无需转换：{}", str);
            return str;
        }
        
        log.info("转换前：" + str);
        StringBuilder result = new StringBuilder();
        char[] charArray = str.toCharArray();
        
        for (char c : charArray) {
            result.append("\\u").append(String.format("%04x", (int) c));
        }
        
        String rs = result.toString();
        log.info("转换后hex：" + rs);
        return rs;
    }
    
    /**
     * 将给定字节数组转为16进制字符串并返回
     *
     * @param bytes 给定的字节数组
     * @return 转换后的16进制字符串
     */
    public static String byteArr2HexStr(byte[] bytes) {
        int len = bytes.length;
        char[] str = new char[len * 2];
        int k = 0;
        for (byte byte_ : bytes) {
            str[k++] = HEX_DIGITS[byte_ >>> 4 & 0xf];
            str[k++] = HEX_DIGITS[byte_ & 0xf];
        }
        return new String(str);
    }
    
    /**
     * 将给定16进制字符串转为字节数组并返回
     *
     * @param hex 给定的16进制字符串
     * @return 转换后的字节数组
     */
    public static byte[] hexStr2ByteArr(String hex) {
        int len = hex.length();
        String[] hexArr = hex.split("");
        byte[] bytes = new byte[len >> 1];
        for (int i = 0; i < len; ) {
            bytes[i >> 1] =
                    (byte) ((Byte.parseByte(hexArr[i++], 16) << 4) ^ Byte.parseByte(hexArr[i++], 16));
        }
        return bytes;
    }
    
    /**
     * 字符串编码由{@link StandardCharsets#UTF_8 UTF-8}转换为指定编码
     *
     * @param source 原字符串
     * @param charset 新编码
     * @return 转换后的新字符串
     */
    public static String switchCharset(String source, Charset charset) {
        return switchCharset(source, StandardCharsets.UTF_8, charset);
    }
    
    /**
     * 字符串编码转换
     *
     * @param source 原字符串
     * @param sourceCharset 原编码
     * @param targetCharset 新编码
     * @return 转换后的新字符串
     */
    public static String switchCharset(String source, Charset sourceCharset, Charset targetCharset) {
        return new String(source.getBytes(sourceCharset), targetCharset);
    }
}