package com.exp.toolkit.security;

import com.exp.toolkit.basic.StringUtils;
import lombok.SneakyThrows;

import java.security.MessageDigest;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * MD5编码
 *
 * @noinspection unused
 */
public final class MD5Util {
    /**
     * 用于MD5编码的默认{@link MessageDigest}实例
     */
    private static final MessageDigest mdg;
    
    static {
        mdg = init();
    }
    
    /**
     * 初始化一个用于MD5编码的{@link MessageDigest}实例并返回
     *
     * @return 用于MD5编码的{@link MessageDigest}实例
     */
    @SneakyThrows
    private static MessageDigest init() {
        return MessageDigest.getInstance("MD5", "SUN");
    }
    
    /**
     * 以大写字母+数字的形式，返回给定字符串的MD5值。<ol>
     * <li>使用内部定义的final的{@link MD5Util#mdg mdg}实例进行摘要计算，不会新增{@link MessageDigest}实例；
     * <li>使用<code>synchronized</code>保证线程安全，初时比{@link #md5(String)}方法效率更低。
     * 除非短时间内需要进行大量的md5计算，且对内存使用有严格的限制，否则不建议使用此方法。
     *
     * @param origin 给定字符串
     * @return 给定字符串的MD值，字母为大写形式
     */
    public static String syncMd5(String origin) {
        byte[] bytes = origin.getBytes(UTF_8);
        synchronized (mdg) {
            mdg.update(bytes);
            bytes = mdg.digest();
            mdg.reset();
        }
        return StringUtils.byteArr2HexStr(bytes);
    }
    
    /**
     * 以大写字母+数字的形式返回给定字符串的MD5值。<ul>
     * <li>每次调用都会创建新的{@link MessageDigest}实例进行摘要计算，没有线程安全问题；
     * <li>调用数多时，因为要大量新建相关实例，效率下降较明显；
     *
     * @param origin 给定字符串
     * @return 给定字符串的MD值，字母为大写形式
     */
    public static String md5(String origin) {
        MessageDigest mdg = MD5Util.init();
        byte[] bytes = origin.getBytes(UTF_8);
        mdg.update(bytes);
        bytes = mdg.digest();
        return StringUtils.byteArr2HexStr(bytes);
    }
}