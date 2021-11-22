package com.exp.toolkit.security;

import com.exp.toolkit.basic.StringUtils;
import lombok.SneakyThrows;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * DES加解密
 *
 * @noinspection unused
 */
public final class DESUtil {
    
    /**
     * 加、解密密工具
     */
    private static final Cipher encryptCipher;
    /**
     * 解密工具
     */
    private static final Cipher decryptCipher;
    
    static {
        try {
            Key key = initKey(DESUtil.class.getName());
            SecureRandom random = getRandom();
            encryptCipher = initCipher(key, random, true);
            decryptCipher = initCipher(key, random, false);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * 使用默认密钥加密给定字符串
     *
     * @param origin 给定字符串
     * @return 加密后的16进制字符串
     */
    @SneakyThrows
    public static String encrypt(String origin)  {
        byte[] inBytes = origin.getBytes(UTF_8);
        byte[] bytes;
        synchronized (encryptCipher) {
            bytes = encryptCipher.doFinal(inBytes);
        }
        return StringUtils.byteArr2HexStr(bytes);
    }
    
    /**
     * 使用默认密钥解密给定16进制字符串
     *
     * @param hex 给定16进制字符串
     * @return 解密后的字符串
     */
    @SneakyThrows
    public static String decrypt(String hex)  {
        byte[] inBytes = StringUtils.hexStr2ByteArr(hex);
        byte[] bytes;
        synchronized (decryptCipher) {
            bytes = decryptCipher.doFinal(inBytes);
        }
        return new String(bytes, UTF_8);
    }
    
    /**
     * 指定密钥实例，加、解密密给定字符串。
     *
     * @param in      给定字符串
     * @param key     密钥
     * @param encrypt 指定要加密或解密给定字符串：
     *                <li>true：加密</li>
     *                <li>false：解密</li>
     * @return 加、解密密后的字符串
     */
    @SneakyThrows
    public static String des(String in, String key, boolean encrypt)  {
        Cipher cipher = initCipher(initKey(key), getRandom(), encrypt);
        if (encrypt) {
            return StringUtils.byteArr2HexStr(cipher.doFinal(in.getBytes(UTF_8)));
        } else {
            return new String(cipher.doFinal(StringUtils.hexStr2ByteArr(in)));
        }
    }
    
    /**
     * 返回一个随机数生成器
     *
     * @return 随机数生成器
     * @ throws
     */
    @SneakyThrows
    private static SecureRandom getRandom()  {
        return SecureRandom.getInstance("SHA1PRNG", "SUN");
    }
    
    /**
     * 指定密钥，初始化并返回{@link Key}实例。
     *
     * @param key 指定密钥
     * @return {@link Key}实例
     */
    private static Key initKey(String key) {
        byte[] keyBytes_ = key.getBytes(UTF_8);
        byte[] keyBytes = new byte[8];
        for (int i = 0; i < keyBytes_.length && i < keyBytes.length; i++) {
            keyBytes[i] = keyBytes_[i];
        }
        return new SecretKeySpec(keyBytes, "DES");
    }
    
    /**
     * 指定密钥，初始化并返回{@link Cipher}实例。
     *
     * @param key     指定密钥
     * @param random  随机数生成器
     * @param encrypt 指定返回的{@link Cipher}实例用于加密或解密：
     *                <li>true：加密</li>
     *                <li>false：解密</li>
     * @return {@link Cipher}实例
     */
    @SneakyThrows
    private static Cipher initCipher(Key key, SecureRandom random, boolean encrypt)  {
        Cipher cipher = Cipher.getInstance("DES", "SunJCE");
        if (encrypt) {
            cipher.init(Cipher.ENCRYPT_MODE, key, random);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, key, random);
        }
        return cipher;
    }
}
