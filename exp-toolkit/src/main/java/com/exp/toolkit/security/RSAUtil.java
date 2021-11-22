package com.exp.toolkit.security;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * RSA编、解码
 *
 * @author Exprass
 * @noinspection unused
 */
public final class RSAUtil {
    
    /**
     * 密钥长度
     */
    private static final int DEFAULT_KEY_SIZE = 1024;
    /**
     * {@link Cipher}实例，使用公钥初始化，用于编码操作
     */
    private static final Cipher publicCipher;
    /**
     * {@link Cipher}实例，使用私钥初始化，用于解码操作
     */
    private static final Cipher privateCipher;
    /**
     * {@link Signature}实例，使用私钥初始化，用于签名操作。
     */
    private static final Signature signer;
    /**
     * {@link Signature}实例，使用公钥初始化，用于验签操作。
     */
    private static final Signature verifier;
    
    private static final String PUBLIC_KEY;
    
    static {
        try {
            KeyPair keyPair = randomKeyPair(DEFAULT_KEY_SIZE);
            PUBLIC_KEY = getKey(keyPair.getPublic());
            privateCipher = initCipher(keyPair, false);
            publicCipher = initCipher(keyPair, true);
            signer = initSignature(keyPair, true);
            verifier = initSignature(keyPair, false);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    
    /**
     * 读取密钥对文件，生成{@link KeyPair}实例并返回。
     *
     * @param fileName 给定密钥对文件名
     * @return {@link KeyPair}实例，或者密钥对文件不存在时返回null。
     */
    @SneakyThrows
    public static KeyPair readKeyPairFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            // 文件存在时直接读取其内容并生成KeyPair实例
            try (InputStream inStream = new FileInputStream(file);
                 ObjectInputStream ois = new ObjectInputStream(inStream)) {
                return (KeyPair) ois.readObject();
            }
        } else {
            return null;
        }
    }
    
    /**
     * 按指定密钥位数，生成{@link KeyPair}实例并写入目标（密钥）文件，然后返回实例。若目标文件已存在，抛出运行时异常。
     *
     * @param fileName 指定密钥对文件名
     * @param keySize  密钥位数
     * @return 新建密钥对文件包含的公钥
     */
    @SneakyThrows
    public static KeyPair createKeyPairFile(String fileName, int keySize) {
        File file = new File(fileName);
        if (file.exists()) {
            throw new RuntimeException(String.format("[%s]已存在", fileName));
        }
        //noinspection ResultOfMethodCallIgnored
        file.createNewFile();
        
        KeyPair keyPair = randomKeyPair(keySize);
        try (FileOutputStream fos = new FileOutputStream(file, false);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(keyPair);
        }
        return keyPair;
    }
    
    /**
     * 指定密钥位数，随机生成一组公/私密钥对。
     *
     * @param keySize 密钥位数
     * @return 密钥对
     * @ 内部异常直接抛出
     */
    @SneakyThrows
    public static KeyPair randomKeyPair(int keySize) {
        KeyPairGenerator keyPairGenerator =
                KeyPairGenerator.getInstance("RSA", new BouncyCastleProvider());
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyPairGenerator.initialize(keySize, random);
        return keyPairGenerator.generateKeyPair();
    }
    
    /**
     * 给定密钥对初始化{@link Cipher}实例
     *
     * @param keyPair 给定密钥对
     * @param encrypt 指定返回的{@link Cipher}实例用于编码或解码：
     *                <li>true：编码</li>
     *                <li>false：解码</li>
     * @return 可用于编、解码的{@link Cipher}实例
     */
    @SneakyThrows
    private static Cipher initCipher(KeyPair keyPair, boolean encrypt) {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());
        if (encrypt) {
            // 使用公钥初始化用于编码的Cipher实例
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        } else {
            // 使用私钥初始化用于解码的Cipher实例
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        }
        return cipher;
    }
    
    /**
     * 给定密钥对初始化{@link Signature}实例。
     *
     * @param keyPair 给定密钥对
     * @param sign    指定返回的{@link Signature}实例用于签名或验签：
     *                <li>true：签名</li>
     *                <li>false：验签</li>
     * @return 可用于签名/验签的{@link Signature}实例
     */
    @SneakyThrows
    public static Signature initSignature(KeyPair keyPair, boolean sign) {
        Signature signature = Signature.getInstance("MD5withRSA");
        if (sign) {
            signature.initSign(keyPair.getPrivate());
        } else {
            signature.initVerify(keyPair.getPublic());
        }
        return signature;
    }
    
    
    /**
     * 默认策略编码给定字符串；使用synchronized保证线程安全，效率较低。
     *
     * @param origin 给定密字符串
     * @return 编码后的字符串
     */
    public static String encrypt(String origin) {
        byte[] oriBytes = origin.getBytes(UTF_8);
        byte[] bytes = encryptBySegment(oriBytes);
        return Base64.encodeBase64String(bytes);
    }
    
    /**
     * 默认策略解码给定的密文串；使用synchronized保证线程安全，效率较低。。
     *
     * @param encrypted 给定的密文串
     * @return 解码后的源字符串
     * @ throws
     */
    public static String decrypt(String encrypted) {
        byte[] bytes = Base64.decodeBase64(encrypted);
        return new String(decryptBySegment(bytes), UTF_8);
    }
    
    /**
     * 使用{@link #publicCipher}分段编码给定字节数组。
     *
     * @param inBytes 给定字节数组
     * @return 编码后的字节数组
     */
    @SneakyThrows
    private static byte[] encryptBySegment(byte[] inBytes) {
        byte[] bytes;
        int len = inBytes.length;
        int left = len;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        synchronized (publicCipher) {
            while (left > 0) {
                if (left > 117) { // DEFAULT_KEY_SIZE >> 3 - 11 == 117
                    bytes = publicCipher.doFinal(inBytes, len - left, 117);
                    out.write(bytes, 0, bytes.length);
                    left -= 117;
                } else {
                    bytes = publicCipher.doFinal(inBytes, len - left, left);
                    out.write(bytes, 0, bytes.length);
                    break;
                }
            }
        }
        return out.toByteArray();
    }
    
    /**
     * 使用指定{@link #privateCipher}分段解码给定字节数组。
     *
     * @param inBytes 给定字节数组
     * @return 解码后的字节数组
     */
    @SneakyThrows
    private static byte[] decryptBySegment(byte[] inBytes) {
        byte[] bytes;
        int len = inBytes.length;
        int left = len;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        synchronized (privateCipher) {
            while (left > 0) {
                if (left > 128) { // DEFAULT_KEY_SIZE >> 3 = 128
                    bytes = privateCipher.doFinal(inBytes, len - left, 128);
                    out.write(bytes, 0, bytes.length);
                    left -= 128;
                } else {
                    bytes = privateCipher.doFinal(inBytes, len - left, left);
                    out.write(bytes, 0, bytes.length);
                    break;
                }
            }
        }
        return out.toByteArray();
    }
    
    
    /**
     * 使用指定{@link Cipher}实例分段编、解码给定字节数组。
     *
     * @param inBytes 给定字节数组
     * @param block   每个分段包含的字节数
     * @param cipher  指定{@link Cipher}实例
     * @return 编、解码后的字节数组
     */
    @SneakyThrows
    private static byte[] doFinalBySegment(byte[] inBytes, int block, Cipher cipher) {
        byte[] bytes;
        int len = inBytes.length;
        int left = len;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while (left > 0) {
            if (left >= block) {
                bytes = cipher.doFinal(inBytes, len - left, block);
                out.write(bytes, 0, bytes.length);
                left -= block;
            } else {
                bytes = cipher.doFinal(inBytes, len - left, left);
                out.write(bytes, 0, bytes.length);
                break;
            }
        }
        return out.toByteArray();
    }
    
    /**
     * 指定密钥串，获取用于编、解码的{@link Cipher}实例。
     *
     * @param key     密钥串
     * @param encrypt 指定要返回的实例类型：<ul>
     *                <li>true：返回编码用{@link Cipher}实例
     *                <li>false：返回解码用{@link Cipher}实例
     * @return {@link Cipher}实例
     */
    @SneakyThrows
    private static Cipher getCipher(Key key, boolean encrypt) {
//        byte[] bytes = Base64.decodeBase64(key.getBytes(UTF_8));
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());
        if (encrypt) {
            cipher.init(Cipher.ENCRYPT_MODE, key);
        } else {
            cipher.init(Cipher.DECRYPT_MODE, key);
        }
        return cipher;
    }
    
    /**
     * 指定{@link Key}实例，编码指定字符串。调用流程：<ol>
     * <li>指定keySize，生成{@link KeyPair}，见{@link #randomKeyPair(int)}；
     * <li>从上述{@link KeyPair}中获取到{@link PublicKey}，见{@link KeyPair#getPublic()}；
     * <li>keySize和key做入参调用此方法。
     *
     * @param origin  给定字符串
     * @param key     给定Key
     * @param keySize 生成密钥时指定的位数
     * @return 编码后的字符串
     */
    public static String encrypt(String origin, Key key, int keySize) {
        Cipher cipher = getCipher(key, true);
        byte[] oriBytes = origin.getBytes(UTF_8);
        return Base64.encodeBase64String(doFinalBySegment(oriBytes, (keySize >> 3) - 11, cipher));
    }
    
    /**
     * 指定{@link Key}实例，解码指定字符串。调用流程：<ol>
     * <li>指定keySize，生成{@link KeyPair}，见{@link #randomKeyPair(int)}；
     * <li>从上述{@link KeyPair}中获取到{@link PrivateKey}，见{@link KeyPair#getPrivate()}；
     * <li>keySize和key做入参调用此方法。
     *
     * @param encrypted 待解码字符串
     * @param key       给定Key
     * @param keySize   生成密钥时指定的位数
     * @return 编码后的字符串
     */
    public static String decrypt(String encrypted, Key key, int keySize) {
        Cipher cipher = getCipher(key, false);
        byte[] bytes = Base64.decodeBase64(encrypted);
        return new String(doFinalBySegment(bytes, keySize >> 3, cipher), UTF_8);
    }
    
    /**
     * 从{@link Key}中读取密钥串
     *
     * @param key 密钥
     * @return 密钥文本串
     */
    public static String getKey(Key key) {
        return new String(Base64.encodeBase64(key.getEncoded()), UTF_8);
    }
    
    /**
     * 返回默认公钥串
     *
     * @return 默认公钥串
     */
    public static String getDefaultPublicKey() {
        return PUBLIC_KEY;
    }
    
    /**
     * 使用默认策略签名给定字符串。
     *
     * @param origin 给定的字符串
     * @return 已签名的字符串
     */
    @SneakyThrows
    public static String sign(String origin) {
        String signed;
        synchronized (signer) {
            signer.update(origin.getBytes(UTF_8));
            signed = new String(Base64.encodeBase64(signer.sign()));
        }
        return signed;
    }
    
    /**
     * 给定私钥，获取用于签名的{@link Signature}实例。
     *
     * @param key 私钥
     * @return 可用于签名的{@link Signature}实例
     */
    @SneakyThrows
    public static Signature getSigner(PrivateKey key) {
        Signature signer = Signature.getInstance("MD5withRSA");
        signer.initSign(key);
        return signer;
    }
    
    /**
     * 使用指定{@link Signature}实例签名给定字符串。
     *
     * @param origin 给定字符串
     * @param signer 指定{@link Signature}实例
     * @return 已签名的字符串
     */
    @SneakyThrows
    public static String sign(String origin, Signature signer) {
        signer.update(origin.getBytes(UTF_8));
        return new String(Base64.encodeBase64(signer.sign()));
    }
    
    /**
     * 使用默认策略验签给定字符串。
     *
     * @param signed 已签名字符串
     * @param origin 未签名的字符串
     * @return 验签通过返回true，否则返回false
     */
    @SneakyThrows
    public static boolean verify(String signed, String origin) {
        boolean verified;
        synchronized (verifier) {
            verifier.update(origin.getBytes(UTF_8));
            verified = verifier.verify(Base64.decodeBase64(signed.getBytes(UTF_8)));
        }
        return verified;
    }
    
    /**
     * 指定公钥，获取用于验签的{@link Signature}实例。
     *
     * @param key 公钥
     * @return 可用于验签的{@link Signature}实例
     */
    @SneakyThrows
    public static Signature getVerifier(PublicKey key) {
        Signature verifier = Signature.getInstance("MD5withRSA");
        verifier.initVerify(key);
        return verifier;
    }
    
    /**
     * 使用指定{@link Signature}实例验签给定字符串。
     *
     * @param signed   已签名字符串
     * @param origin   未签名的字符串
     * @param verifier 指定{@link Signature}实例
     * @return 验签通过返回true，否则返回false
     */
    @SneakyThrows
    public static boolean verify(String signed, String origin, Signature verifier) {
        boolean verified;
        verifier.update(origin.getBytes(UTF_8));
        verified = verifier.verify(Base64.decodeBase64(signed.getBytes(UTF_8)));
        return verified;
    }
}
