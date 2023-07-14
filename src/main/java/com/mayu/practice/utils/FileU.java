package com.mayu.practice.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: 马瑜
 * @Date: 2023/5/22 16:40
 * @Description: TODO
 */
@Slf4j
public class FileU {

    private static final String CALLBACK_FILE_PREFIX = "RecordingResults_";
    private static final String CALLBACK_FILE_SUFFIX = ".csv";
    private static final String CALLBACK_DIR = "scoreCallBack";
    public static SimpleDateFormat ymd = new SimpleDateFormat("yyyyMMdd");

    private static String hh(Date fileNameDate) {
        File file = new File("/tmp/");
        System.out.println(file.getAbsolutePath());
        return "";
    }


    private static String getLocalTempPath(Date fileNameDate) {
        String fileName = CALLBACK_FILE_PREFIX + DateUtils.formatDate(fileNameDate, ymd) + CALLBACK_FILE_SUFFIX;
        String tempPath = "";
        try {
            String tempDirRootPath =
                    "/tmp" + File.separatorChar
                            + CALLBACK_DIR + File.separatorChar + DateUtils.fomateDate(new Date());

            File tempM = new File(tempDirRootPath);
            if (!tempM.exists()) {
                org.apache.commons.io.FileUtils.forceMkdir(tempM);
            }

            File tempFile = new File(tempDirRootPath, fileName);

            // 如果本地已经存在 覆盖
            if (tempFile.exists()) {
                log.warn("本地已经存在同名文件! 删除并覆盖! tempFile:{}", tempFile.getAbsolutePath());
                org.apache.commons.io.FileUtils.deleteQuietly(tempFile);
            }

            // 覆盖
            boolean f = tempFile.createNewFile();
            if (f) {
                tempPath = tempFile.getAbsolutePath();
            } else {
                throw new RuntimeException("创建本地临时文件异常!");
            }
        } catch (Exception e) {
            log.error("创建本地临时文件异常! {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return tempPath;
    }

    public static void main(String[] args) {
        String l = getLocalTempPath(new Date());
//        hh(new Date());
        System.out.println(l);
    }
}
