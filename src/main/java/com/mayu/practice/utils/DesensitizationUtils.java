package com.mayu.practice.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: 马瑜
 * @Date: 2024/7/8 14:17
 * @Description: 数据脱敏处理
 */
public class DesensitizationUtils {

    private static final String CHINESE_REGEX = "[\u4e00-\u9fff]+";

    public static void main(String[] args) {
        String msgContent = "这是一个电话号码13812345678，这是另一个电话号码13987654321," +
                " 这时省份整号码51370119930811321X，这是银行卡号6222020102030405060， 我的地址是四川省，成都市区天府新区";
        maskingHandle(msgContent);
    }

    // 检测字符串中是否包含中文汉字
    public static boolean checkContainsChineseCharacters(String text) {
        Pattern pattern = Pattern.compile(CHINESE_REGEX);
        Matcher matcher = pattern.matcher(text);
        return matcher.find();
    }

    // 消息脱敏处理
    private static String maskingHandle(String msgContent) {
        String firstRes = desensitizeIDCardAndPhoneNumbers(msgContent);
        return desensitizedAddress(firstRes);
    }


    /**
     * 地址脱敏，脱敏规则：对目标会话所有消息进行分词，对分词结果进行正则匹配
     * (省|市|区|县|路|街|座|号|楼|栋|梯|室|巷|大道|花园|苑|幢|弄|单元|乡)
     * 是否包含以以上词组结尾的关键词
     */
    public static String desensitizedAddress(String text) {
        String[] pieces = text.split("，");
        Pattern pattern = Pattern.compile("(省|市|区|县|路|街|座|号|楼|栋|梯|室|巷|大道|花园|苑|幢|弄|单元|乡)$");
        for (String piece : pieces) {
            Matcher matcher = pattern.matcher(piece);
            while (matcher.find()) {
                String desensitizedBankAddress = "**地址**";
                text = text.replace(piece, desensitizedBankAddress);
            }
        }
        return text;
    }

    // 电话号码 11位
    // 银行卡号 19位
    // 身份证号 18位 或者 17位+x(大小写)
    public static String desensitizeIDCardAndPhoneNumbers(String text) {
        Pattern pattern = Pattern.compile("\\d{19}|\\d{18}|\\d{17}(\\d|X|x)|\\d{16}|\\d{11}");
        Matcher matcher = pattern.matcher(text);
        StringBuffer desensitizedText = new StringBuffer();
        while (matcher.find()) {
            String idOrPhoneNumber = matcher.group();
            // 匹配到银行卡号
            if (idOrPhoneNumber.length() == 19 || idOrPhoneNumber.length() == 16) {
                String desensitizedBankCardNumber = "**银行卡号**";
                matcher.appendReplacement(desensitizedText, desensitizedBankCardNumber);
            } else if (idOrPhoneNumber.length() == 18) {
                // 匹配到身份证号码
                String desensitizedIdNumber = "**身份证号码**";
                matcher.appendReplacement(desensitizedText, desensitizedIdNumber);
            } else if (idOrPhoneNumber.length() == 11) {
                // 匹配到电话号码
                String desensitizedPhoneNumber = "**电话号码**";
                matcher.appendReplacement(desensitizedText, desensitizedPhoneNumber);
            } else {
                // 其他情况保留原样
                matcher.appendReplacement(desensitizedText, idOrPhoneNumber);
            }
        }
        matcher.appendTail(desensitizedText);
        return desensitizedText.toString();
    }
}
