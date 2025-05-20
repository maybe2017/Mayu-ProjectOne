package com.mayu.practice.test;

import com.alibaba.fastjson.JSON;
import com.mayu.common.Constants;
import com.mayu.practice.po.MayuTest;
import com.mayu.practice.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author: 马瑜
 * @Date: 2024/10/28 16:36
 * @Description: TODO
 */
@Slf4j
public class Test02 {

    public static void main(String[] args) throws ParseException {
//        matchRegex("您的积分>1000分，可兑换流量包、热门会员等多种福利！[机智]\\n积分不兑换要过期的，请及时点击链接兑换。\\n如果进链接后不会弄，可以联系我哦");
        getScanPaths("/data/", "yyyyMMdd", new ArrayList<>(), 1, "yuyue");
        System.out.println(getScanPath("/data/", "yyyyMMdd", Arrays.asList("aaa"), 1));
//        String audioName = "BJ2XZ9750236D77604967802678232BC3B64B-quick_audio_file.mp3";
//        System.out.println(audioName.substring(0, audioName.lastIndexOf("mp3")));
//        System.out.println("2021112700000031".substring(7));
//        System.out.println(JSON.toJSONString("lineInfo||||".split("\\|")));
//        testParse6();
        System.out.println(getDateStrByOffSet(Calendar.DAY_OF_MONTH, 2));
    }

    private static final String MIRCO_FOLLOW_STATUS_PREFIX = "跟进状态：";
    private static final String MIRCO_FOLLOW_RESULT_PREFIX = "跟进总结：";

    public static String getDateStrByOffSet(int timeType, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(timeType, offset);
        Date beforeThreeMonthsTime = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beforeThreeMonthsTime);
    }

    public static List<String> getScanPaths(String path, String fileNameFormat, List<String> extraPaths, Integer timeOffSet, String who) {
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        List<String> scanPaths = new ArrayList<>();
        if (StringUtils.isBlank(fileNameFormat)) {
            fileNameFormat = DateUtils.YYYYMMDD;
        }
        DateFormat df = new SimpleDateFormat(fileNameFormat);
        for (int i = 0; i <= Math.abs(timeOffSet); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -i); // 起始时间从今天开始，向前偏移
            Date date = calendar.getTime();
            String dateFolder = df.format(date);
            if (CollectionUtils.isNotEmpty(extraPaths)) {
                String finalPath = path;
                extraPaths.forEach(extraPath -> scanPaths.add(finalPath + extraPath + "/" + dateFolder + "/"));
            } else {
                scanPaths.add(path + dateFolder + "/");
            }
        }
        log.info("[网优-投诉]=>> 本次扫描[{}], timeOffSet:{}, 要处理的FTP目录集: {}", path, timeOffSet, JSON.toJSONString(scanPaths));
        return scanPaths;
    }

    public static String getScanPath(String path, String fileNameFormat, List<String> extraPaths, Integer timeOffSet) {
        if (StringUtils.isBlank(fileNameFormat)) {
            fileNameFormat = DateUtils.YYYYMMDD;
        }
        DateFormat df = new SimpleDateFormat(fileNameFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -Math.abs(timeOffSet)); // 起始时间从今天开始，向前偏移
        Date date = calendar.getTime();
        String dateFolder = df.format(date);
        String finalPath = path;
        if (CollectionUtils.isNotEmpty(extraPaths)) {
            for (String extraPath : extraPaths) {
                if (!extraPath.endsWith("/")) {
                    extraPath = extraPath + "/";
                    finalPath = finalPath + extraPath;
                }
            }
        }
        return finalPath + dateFolder + "/";
    }

    private static void testParse6() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date endDate = calendar.getTime();
        log.info("{}", DateUtils.formatDate(startDate, DateUtils.YYYY_MM_DD_HH_MM_SS));
        log.info("{}", DateUtils.formatDate(endDate, DateUtils.YYYY_MM_DD_HH_MM_SS));
    }

    private static void testParse5() throws ParseException {
        Set<String> exitsTransConversationIdSet = new HashSet<>();
        exitsTransConversationIdSet.add("AAA");
        exitsTransConversationIdSet.add("AAA");
        exitsTransConversationIdSet.add("BBB");
        log.info("{}", new ArrayList<>(exitsTransConversationIdSet));
    }

    private static void testParse4() throws ParseException {
        String defaultOptionStr = "(?=.*(?:流量卡))";
        try {
            Pattern pattern = Pattern.compile(defaultOptionStr);
            Matcher matcher = pattern.matcher("给我一张流量卡");
            if (matcher.find()) {
                log.info("{}", matcher.group(0));
            }
        } catch (Exception e) {
            log.error("正则匹配异常! {}", e.getMessage(), e);
        }
    }

    private static void testParse3() throws ParseException {
        String defaultOptionStr = "2024-1-8";
        Date date = DateUtils.parse(defaultOptionStr, DateUtils.YYYY_MM_DD_HH_MM_SS);
        log.info("{}", DateUtils.formatDate(date, DateUtils.YYYY_MM_DD_HH_MM_SS));
    }

    private static void testParse1() throws ParseException {
        String defaultOptionStr = "包保录音:0|装机首联录音:1|修障首联录音:2|用户呼入录音:3";
        String[] optionStrArr = defaultOptionStr.split("\\|");
        System.out.println(JSON.toJSONString(optionStrArr));
    }

    private static void testParse() throws ParseException {
        String res = "跟进状态：有跟进/未跟进\n跟进总结：xxxxxx";
        String s1 = res.substring(MIRCO_FOLLOW_STATUS_PREFIX.length(), res.lastIndexOf(MIRCO_FOLLOW_RESULT_PREFIX));
        String s2 = res.substring(res.lastIndexOf(MIRCO_FOLLOW_RESULT_PREFIX) + MIRCO_FOLLOW_RESULT_PREFIX.length());
        System.out.println(s1);
        System.out.println(s2);
    }

    private static void testOrder() throws ParseException {
        String orderTimeStr = "2024-12-10 12:45:01";
        Date orderTime = DateUtils.convertToDate(orderTimeStr, "yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(orderTime);
        calendar.add(Calendar.HOUR_OF_DAY, 8 + 10);

        Date endTime = calendar.getTime();
        String endTimeStr = DateUtils.formatDate(endTime, DateUtils.YYYY_MM_DD_HH_MM_SS);
        log.info("endTimeStr:{}", endTimeStr);
        log.info("orderTimeStr:{}", orderTimeStr);

        long delayMillSeconds = calendar.getTime().getTime() - orderTime.getTime();
        log.info("订单[{}], 订单时间:{}, 配置延迟稽核最大小时数:{}, 配置等待会话切割小时数:{}, 计算后自此刻延迟发送到队列的时间:{}",
                "order.getOrderId()", orderTimeStr, 8,
                10, DateUtils.getDatePoor(delayMillSeconds));
    }

    public static List<String> getScanPaths(String path, String fileNameFormat, String dirAfterDate, Integer timeOffSet, String who) {
        List<String> scanPaths = new ArrayList<>();
        for (int i = 0; i <= Math.abs(timeOffSet); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -i); // 起始时间从昨天开始，向前偏移
            Date date = calendar.getTime();
            DateFormat df = new SimpleDateFormat(fileNameFormat);
            String dateFolder = df.format(date);

            // 处理路径后的'/'
            if (!path.endsWith("/")) {
                path = path + Constants.SP;
            }

            String pathName = path + dateFolder;
            if (StringUtils.isNotBlank(dirAfterDate)) {
                pathName = pathName + Constants.SP + dirAfterDate;
            }
            scanPaths.add(pathName);
        }
        log.info("[{}]=>> 本次扫描到要处理的目录集: {}", who, JSON.toJSONString(scanPaths));
        return scanPaths;
    }

    private static void re() {
        String audioFileName = "BJ2XZ9750236D77604967802678232BC3B64B-quick_audio_file.mp3";
        System.out.println(audioFileName.substring(0, audioFileName.indexOf("-quick")));

    }

    private static void testR() {
        Map<Integer, String> map = new HashMap<>();
        map.put(new Integer(2024), "AAAA");
        map.put(new Integer(20245), "AAAA");
        log.info("=={}==", map.get(2024));
    }

    private static void matchRegex(String msgContent) {
        String regex = "(?=.*(?:评价|评分))(?=.*(?:10分|10星|打10))";
        log.info("== 命中: {} ==", doMatchPattern(msgContent, regex) ? "是" : "否");
    }

    private static boolean doMatchPattern(String msgContent, String regexContent) {
        try {
            Pattern pattern = Pattern.compile(regexContent);
            return pattern.matcher(msgContent).find();
        } catch (Exception e) {
            throw new RuntimeException(regexContent);
        }
    }

    private static void testList() {
        List<MayuTest> list1 = new ArrayList<>();
        MayuTest obj1 = new MayuTest();
        obj1.setTaskId("AAAA");
        list1.add(obj1);

        MayuTest obj2 = new MayuTest();
        obj2.setTaskId("BBBB");
        list1.add(obj2);

        MayuTest obj3 = new MayuTest();
        obj3.setTaskId("CCCC");
        list1.add(obj3);


        MayuTest obj4 = new MayuTest();
        obj4.setTaskId("DDDD");
        list1.add(obj4);

        System.out.println(JSON.toJSONString(list1));

        int msgIndex = 0;
        for (; msgIndex < list1.size(); msgIndex++) {
            MayuTest record = list1.get(msgIndex);
            if ("BBBB".equals(record.getTaskId())) {
                break;
            }
        }
        List<MayuTest> leftRecordList = list1.stream().skip(msgIndex + 1).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(leftRecordList));
    }

}
