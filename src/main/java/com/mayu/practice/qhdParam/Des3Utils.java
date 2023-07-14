package com.mayu.practice.qhdParam;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Author: 马瑜
 * @Date: 2023/2/16 10:28
 * @Description: des3加解密
 */
@Slf4j
public class Des3Utils {
    private Des3Utils() {
    }

    public static String des3En(String data, String salt, String secret) {
        try {
            String key = DigestUtils.md5Hex(salt + secret);
            byte[] bytes = DES3Crypto.encrypt(key, data);
            byte[] base64Encode = Base64.encodeBase64(bytes);
            return new String(base64Encode, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("{}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static String des3De(String encodeData, String salt, String secret) throws Exception {
        String key = DigestUtils.md5Hex(salt + secret);
        byte[] base64Decode = Base64.decodeBase64(encodeData.getBytes(StandardCharsets.UTF_8));
        byte[] bytes = DES3Crypto.decrypt(key, base64Decode);
        return new String(bytes, StandardCharsets.UTF_8);
    }


    public static Map<String, String> des3EnWithInNeedParam(Map<String, String> originParam) {
        String secret = "75HVYG0VQVDEYPLLODZUX99ZCV333EKY";
        String salt = String.valueOf(new Date().getTime());

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("salt", salt);
        paramMap.put("version","2.6");
        paramMap.put("time", salt);

        paramMap.put("data", des3En(JSON.toJSONString(originParam), salt, secret));

        String sign = SignatureUtils.generate(paramMap, secret, "sign");
        paramMap.put("sign", sign);

        return paramMap;
    }

    public static void main(String[] args) throws Exception {
//        String secret = "75HVYG0VQVDEYPLLODZUX99ZCV333EKY";
//        String salt = String.valueOf(new Date().getTime());
//
//
//        Map<String, String> paramMap = new HashMap<>();
//        paramMap.put("salt", salt);
//        paramMap.put("version","2.6");
//        paramMap.put("time", salt);
//
//        Map<String, String> data = new HashMap<>();
//        data.put("collectionName", "work_order");
//        data.put("groupFieldName", "number");
//
//        paramMap.put("data", des3En(JSON.toJSONString(data), salt, secret));
//
//        String sign = SignatureUtils.generate(paramMap, secret, "sign");
//        paramMap.put("sign", sign);
//
//        System.out.println(JSON.toJSONString(paramMap));
//        ttt();
        ll();

    }


    private static void ll() {
        List<Integer> arr = new ArrayList<>();
        arr.add(1);
        arr.add(1, 2);
        System.out.println(arr.size());
    }

    private static void r() {
        String noHostUrl = "/asdasd/";
        if (noHostUrl.startsWith("/")) {
            noHostUrl = noHostUrl.substring(1);
            System.out.println(noHostUrl);
        }
    }

    private static void ttt() {
        List<String> res = new ArrayList<>();
        res.add("AAa");
        res.add("AAb");
        res.add("AAc");
        res.add("AAd");
        res.add("AAe");
        res.add("AAf");
        res.add("AAg");

        int batchHandleSize = 2;
        int count = 1;
        if (res.size() > batchHandleSize) {
            count = res.size() / batchHandleSize;
            if (res.size() % batchHandleSize > 0) {
                count = count + 1;
            }
        }

        int startIndex = 0;
        int toIndex = 0;
        for (int i=0; i < count; i++) {
            startIndex = i * batchHandleSize;
            toIndex = i == count-1 ? res.size() : startIndex + batchHandleSize;
            System.out.println("startIndex = " + startIndex + ",toIndex = " + toIndex);
            System.out.println(res.subList(startIndex, toIndex));
        }
    }
}
