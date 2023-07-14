package com.mayu.practice.qhdParam;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @Author: 马瑜
 * @Date: 2023/3/3 16:51
 * @Description:
 */
public class DES3Crypto {

    public static byte[] encrypt(final String secret, String data) throws Exception {
        return crypto(secret, Cipher.ENCRYPT_MODE, data.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decrypt(final String secret, byte[] data) throws Exception {
        return crypto(secret, Cipher.DECRYPT_MODE, data);
    }

    private static byte[] crypto(String secret, int mode, byte[] data) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(secret.getBytes(StandardCharsets.UTF_8));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("desede");
        SecretKey desKey = keyFactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(mode, desKey);
        return cipher.doFinal(data);
    }
}
