package com.mayu.practice.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;

/**
 * @Author: 马瑜
 * @Date: 2023/2/23 09:51
 * @Description: 加解密
 */
@Slf4j
public class AESUtils {

    // 加密算法
    private static final String KEY_ALGORITHM = "AES";
    // 固定值
    private static final String SECRET_RANDOM = "SHA1PRNG";
    // 算法、加密模式、填充方式
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    // 私钥
    private static final String ASSETS_DEV_PWD_FIELD = "56eB6ZKl";

    /**
     * 加密
     * @param content 需要加密的文本
     */
    public static String encrypt(String content) {
        if (StringUtils.isBlank(content)) {
            return "";
        }
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);

            // 设置模式（ENCRYPT_MODE：加密模式；DECRYPT_MODE：解密模式）和指定秘钥
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(ASSETS_DEV_PWD_FIELD));
            // 加密
            byte[] result = cipher.doFinal(byteContent);
            // 通过Base64转码返回
            return Base64Utils.encodeToString(result);
        } catch (Exception e) {
            log.error("加密异常: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * AES 解密操作
     * @param content 需要加密的文本
     */
    public static String decrypt(String content) {
        if (StringUtils.isBlank(content)) {
            return "";
        }
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(ASSETS_DEV_PWD_FIELD));
            //执行操作
            byte[] result = cipher.doFinal(Base64Utils.decodeFromString(content));
            return new String(result, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("解密异常: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 生成加密秘钥
     */
    private static SecretKeySpec getSecretKey(String password) {

        // 返回生成指定算法密钥生成器的 KeyGenerator 对象
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            SecureRandom secureRandom = SecureRandom.getInstance(SECRET_RANDOM);
            secureRandom.setSeed(password.getBytes());
            // AES 要求密钥长度为 128
            kg.init(128, secureRandom);
            // 生成一个密钥
            SecretKey secretKey = kg.generateKey();
            // 转换为AES专用密钥
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            log.error("生成加密秘钥异常: {} ", e.getMessage(), e);
        }
        return null;
    }

    public static void main(String[] args) throws ParseException {
//        String value = "居翔瑗";
//        String encrypt = AESUtils.encrypt(value);
//        String decrypt = AESUtils.decrypt(encrypt);
//        log.info("加密前：" + value);
//        log.info("加密后：" + encrypt);
//        log.info("解密后：" + decrypt);
    }
}
