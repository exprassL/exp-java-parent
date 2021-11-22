package com.exp.toolkit.security;

import com.exp.toolkit.basic.StringUtils;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;

import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * AES加、解密密。
 *
 * @noinspection unused
 */
public final class AESUtil {
    
    /**
     * 默认密钥
     */
    private static final Key KEY;
    /**
     * 默认{@link Cipher}实例，用于加、解密密
     */
    private static final Cipher CIPHER;
    
    static {
        try {
            KEY = initKey(AESUtil.class.getName().getBytes(UTF_8), getSize128().value);
            CIPHER = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * 使用默认AES策略（默认密钥、128位）返回给定字符串的AES加密值。
     * 使用synchronized保证线程安全，效率较低。
     *
     * @param origin 给定字符串
     * @return 加密后的字符串
     */
    @SneakyThrows
    public static String encrypt(String origin) {
        byte[] inBytes = origin.getBytes(UTF_8);
        byte[] bytes;
        synchronized (CIPHER) {
            CIPHER.init(Cipher.ENCRYPT_MODE, KEY);
            bytes = CIPHER.doFinal(inBytes);
        }
        
        return new String(Hex.encodeHex(bytes));
    }
    
    /**
     * 使用默认AES策略（默认密钥、128位），将给定加密字符串解密。
     * 使用synchronized保证线程安全，效率较低。
     *
     * @param encoded 给定加密字符串
     * @return 解密得到的源字符串
     */
    @SneakyThrows
    public static String decrypt(String encoded) {
        byte[] inBytes = Hex.decodeHex(encoded.toCharArray());
        byte[] bytes;
        synchronized (CIPHER) {
            CIPHER.init(Cipher.DECRYPT_MODE, KEY);
            bytes = CIPHER.doFinal(inBytes);
        }
        
        return new String(bytes, UTF_8);
    }
    
    /**
     * 使用指定参数初始化{@link Cipher}实例加密给定的字符串并返回。
     *
     * @param origin  给定的字符串
     * @param key     指定用于配置随机数生成器的密码
     * @param keySize 指定密钥位数：128、192、256之一，超出范围抛异常
     * @return 加密后字符串
     */
    @SneakyThrows
    public static String encrypt(String origin, String key, KeySize keySize) {
        Cipher cipher = getCipher(key, keySize, true);
        return new String(Hex.encodeHex(cipher.doFinal(origin.getBytes(UTF_8))));
    }
    
    /**
     * 使用指定参数初始化{@link Cipher}实例解密给定的字符串并返回。
     *
     * @param encoded 给定的已加密字符串
     * @return 加密后字节数组
     */
    @SneakyThrows
    public static String decrypt(String encoded, String key, KeySize keySize) {
        Cipher cipher = getCipher(key, keySize, false);
        return new String(cipher.doFinal(Hex.decodeHex(encoded.toCharArray())), UTF_8);
    }
    
    /**
     * 指定参数，初始化返回AES加、解密密所需的{@link Key}实例。
     *
     * @param seed    指定用于配置随机数生成器的字节数组
     * @param keySize 指定密钥位数
     * @return 密钥，{@link SecretKeySpec}实例，{@link Key}接口的实现
     */
    @SneakyThrows
    private static Key initKey(byte[] seed, int keySize) {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "SunJCE");
        SecureRandom random = new SecureRandom(seed);
        // SecureRandom.getInstance("SHA1PRNG", "SUN");
        // random.setSeed(seed);
        keyGenerator.init(keySize, random);
        SecretKey secretKey = keyGenerator.generateKey();
        
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }
    
    /**
     * 指定参数，初始化AES加、解密密所需的{@link Cipher}实例并返回。
     *
     * @param key     指定用于配置随机数生成器的密码
     * @param keySize 指定密钥位数
     * @param encrypt 指定返回用于AES加密或解密的{@link Cipher}实例:
     *                <li>true：加密
     *                <li>false：解密
     * @return {@link Cipher}实例
     */
    @SneakyThrows
    private static Cipher getCipher(String key, KeySize keySize, boolean encrypt) {
        if (StringUtils.isBlank(key)) {
            key = AESUtil.class.getName();
        }
        if (keySize == null) {
            keySize = KeySize.SIZE_128;
        }
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        int type = encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        cipher.init(type, initKey(key.getBytes(UTF_8), keySize.value));
        return cipher;
    }
    
    public static KeySize getSize128() {
        return KeySize.SIZE_128;
    }
    
    public static KeySize getSize192() {
        return KeySize.SIZE_192;
    }
    
    public static KeySize getSize256() {
        return KeySize.SIZE_256;
    }
    
    /**
     * 密钥位数，128、192、256其中之一。
     */
    private enum KeySize {
        
        SIZE_128(128),
        SIZE_192(192),
        SIZE_256(256);
        
        private final int value;
        
        KeySize(int value) {
            this.value = value;
        }
    }
}