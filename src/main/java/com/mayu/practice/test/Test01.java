package com.mayu.practice.test;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;

/**
 * @Author: 马瑜
 * @Date: 2023/6/27 17:04
 * @Description:
 */
@Slf4j
public class Test01 {
    public static void main(String[] args) {
//        testEor();
        handleStr();
    }

    // 异或的测试
    private static void testEor() {
        int i = 999;
        int j = 888;
        System.out.println(i ^ i ^ j);
        System.out.println(0 ^ i ^ j ^ i ^ j ^ i);
    }

    // 加密配置文件中的字符串
    private static String handleStr() {
        StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
        EnvironmentPBEConfig config = new EnvironmentPBEConfig();
        // 加密的算法，这个算法是默认的
        config.setAlgorithm("PBEWithMD5AndDES");
        // 加密的密钥，随便自己填写，很重要千万不要告诉别人
        config.setPassword("encryptPassword_iflytek0725");
        standardPBEStringEncryptor.setConfig(config);


        // mongo uri
        String mongoUri = "mongodb://10.40.7.26:27017/sas_multi_test_faw_vw?authSource=admin";
        String encryptedMongoUri = standardPBEStringEncryptor.encrypt(mongoUri);
        // mysql
        String mysqlUsername = "yuyin";
        String mysqlPassword = "iflytekcti";
        String encryptedMysqlPassword = standardPBEStringEncryptor.encrypt(mysqlPassword);
        String encryptedMysqlUsername = standardPBEStringEncryptor.encrypt(mysqlUsername);
        System.out.println("encryptedMongoUri:" + encryptedMongoUri);
        System.out.println("mysql-username:" + encryptedMysqlUsername);
        System.out.println("mysql-password:" + encryptedMysqlPassword);
        System.out.println("--------minio--------");

        // minio
        String accessKey = "aftsminio";
        String secretKey = "Ifatdlhe#dlmcpsl2#min#IO";
        String accessKeyEn = standardPBEStringEncryptor.encrypt(accessKey);
        String secretKeyEn = standardPBEStringEncryptor.encrypt(secretKey);

        System.out.println("accessKeyEn:" + accessKeyEn);
        System.out.println("secretKeyEn:" + secretKeyEn);
        System.out.println("--------es--------");
        // es
        String esUserName = "elastic";
        String esPassword = "9999nb";
        String esUserNameEn = standardPBEStringEncryptor.encrypt(esUserName);
        String esPasswordEn = standardPBEStringEncryptor.encrypt(esPassword);

        System.out.println("esUserNameEn:" + esUserNameEn);
        System.out.println("esPasswordEn:" + esPasswordEn);

        return "";
    }
    //
    // -Djasypt.encryptor.password=encryptPassword
}
