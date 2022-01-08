package com.czh.redis.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenzh
 * RSA工具类
 */
@Slf4j
public class RSAUtil {

    public static Map<String, String> getRSAKey() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = null;
        keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair kp = keyPairGen.generateKeyPair();

        Key publicKey = kp.getPublic();
        byte[] publicKeyBytes = publicKey.getEncoded();
        String pub = new String(Base64.encodeBase64(publicKeyBytes), StandardCharsets.UTF_8);
        // 得到私钥
        Key privateKey = kp.getPrivate();
        byte[] privateKeyBytes = privateKey.getEncoded();
        String pri = new String(Base64.encodeBase64(privateKeyBytes), StandardCharsets.UTF_8);

        Map<String, String> map = new HashMap<String, String>();
        map.put("publicKey", pub);
        map.put("privateKey", pri);
        RSAPublicKey rsp = (RSAPublicKey) kp.getPublic();
        BigInteger bint = rsp.getModulus();
        byte[] b = bint.toByteArray();
        byte[] deBase64Value = Base64.encodeBase64(b);
        String retValue = new String(deBase64Value);
        map.put("modulus", retValue);
        return map;
    }

    /**
     * 加密
     * @param source      加密内容
     * @param publicKey   RSA公钥
     * @return
     * @throws RuntimeException
     */
    public static String encryptByPubKey(String source, String publicKey) {
        try {
            Key key = getPublicKey(publicKey);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] b = source.getBytes();
            // 执行加密操作
            byte[] b1 = cipher.doFinal(b);
            return new String(Base64.encodeBase64(b1), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("RSA加密错误", e);
            throw new RuntimeException("RSA加密错误" + e.getMessage());
        }
    }

    /**
     * 加密
     * @param source      加密内容
     * @param privateKey  RSA私钥
     * @return
     * @throws RuntimeException
     */
    public static String encryptByPriKey(String source, String privateKey) {
        try {
            Key key = getPrivateKey(privateKey);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] b = source.getBytes();
            // 执行加密操作
            byte[] b1 = cipher.doFinal(b);
            return new String(Base64.encodeBase64(b1), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("RSA加密错误", e);
            throw new RuntimeException("RSA加密错误" + e.getMessage());
        }
    }

    /**
     * 解密
     * @param encodeContent  加密后的字符串
     * @param privateKey     RSA私钥
     * @return
     * @throws RuntimeException
     */
    public static String decryptByPriKey(String encodeContent, String privateKey) {
        try {
            Key key = getPrivateKey(privateKey);
            // 得到Cipher对象对已用公钥加密的数据进行RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] b1 = Base64.decodeBase64(encodeContent.getBytes());
            // 执行解密操作
            byte[] b = cipher.doFinal(b1);
            return new String(b);
        } catch (Exception e) {
            log.error("RSA解密出错", e);
            throw new RuntimeException("RSA解密出错" + e.getMessage());
        }
    }

    /**
     * 解密
     * @param encodeContent  加密后的字符串
     * @param publicKey      RSA共钥
     * @return
     * @throws RuntimeException
     */
    public static String decryptByPubKey(String encodeContent, String publicKey) {
        try {
            Key key = getPublicKey(publicKey);
            // 得到Cipher对象对已用公钥加密的数据进行RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] b1 = Base64.decodeBase64(encodeContent.getBytes());
            // 执行解密操作
            byte[] b = cipher.doFinal(b1);
            return new String(b);
        } catch (Exception e) {
            log.error("RSA解密出错", e);
            throw new RuntimeException("RSA解密出错" + e.getMessage());
        }
    }

    public static PublicKey getPublicKey(String key) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(
                    Base64.decodeBase64(key.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            log.error("RSA获取公钥出错", e);
            throw new RuntimeException("RSA获取公钥出错：" + e.getMessage());
        }
    }

    public static PrivateKey getPrivateKey(String key) {
        try {
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(
                    Base64.decodeBase64(key.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("RSA获取私钥出错", e);
            throw new RuntimeException("RSA获取私钥出错：" + e.getMessage());
        }
    }
}
