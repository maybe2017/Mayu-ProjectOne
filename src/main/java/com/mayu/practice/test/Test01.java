package com.mayu.practice.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.mayu.common.Constants;
import com.mayu.practice.dto.ComplaintDetailsParseRes;
import com.mayu.practice.dto.EmotionParseRes;
import com.mayu.practice.dto.RemoteLinkProperties;
import com.mayu.practice.po.MayuTest;
import com.mayu.practice.po.resourceInfo.LabelKeyword;
import com.mayu.practice.utils.DateUtils;
import com.mayu.practice.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author: 马瑜
 * @Date: 2023/6/27 17:04
 * @Description:
 */
@Slf4j
public class Test01 {
    public static void main(String[] args) {
        System.out.println(parseValidResByFixedConfig("有兴趣；无兴趣；无法办理", "[无兴趣]"));
//        testEor();
//        handleStr();
//        testJavaTempDir();
//        test11();
//        testSkip();
//        System.out.println(parseTaskId("AAB.wav"));
//        System.out.println(JSON.toJSONString(null));
//        checkDatePattern();
//        checkOs();
//        testStack(4);
//        getDatePartitionDir("adasdadsada_20230712asdasdasd");
//        System.out.println(getNeedQueryFtpDateDirPath("rootDirPath", "20240120", "20240125"));
//        getNeedQueryFtpDateDirPathMap("rootDirPath", "20240120", "20240125");
//        String absDir = "/test/sxyd/txt-file/20231101";
//        System.out.println(absDir.substring(absDir.lastIndexOf("/") + 1));
//        testFtpScanPath();
//        refreshPath("/JSYD/", "yyyyMMdd", new ArrayList<>(), Arrays.asList("jike/dw"), 10, "daiwei");
//        refreshPathCK("/jnwb3_sp/keda/", "yyyyMMdd",  Arrays.asList("qualityResult"), 2, "changke");
//        getChannel("/jsyd/20210630/jiake/dw/");
//        log.info("--{}", getVoiceTime("/jsyd/20210630/jiake/xxxx.txt"));
//        log.info("--{}", getVoiceTime("/jnwb3_sp/keda/qualityResult/20240301/asdas.txt"));


//        log.info("--{}", getVoiceTime2("/jnwb3_sp/keda/qualityResult/20240301/asdas.txt"));
//        test222();
//        te();
//        String audioUrl = "http://10.227.18.72:45500/bit-dfs/file/download?fileId=2f0cd17e128a4968b0a30c5dd17debbb&sourceSystem=CQ-CMCC-EOMS";
//        String audioName = audioUrl.substring(audioUrl.lastIndexOf("/") + 1);
//        System.out.println(getFilePath(audioUrl, null));
//        kk();
//        oo("/jnwb3_sp/keda/qualityResult/20240301/changke_t/");
//        ll();
//        System.out.println(ll("/xxxx.txt"));
//        asda();
//        testSubList();
//        testCalendar();
//        testArr();
//        System.out.println(JSON.toJSONString(getQueryConversationTimeRange("2024-07-09 10:30:22", "48,0")));
//        reName1();
//        testList();
//        testPattern();
//        random();
//        System.out.println(JSON.toJSONString(parseEmotionStatus("情绪状态：有负面情绪；投诉状态：无投诉；情绪原因：客户对网络突然中断和维修服务电话无法接通表示不满。")));
//        testUuid();
//        testWhileDate("2024-07-13 00:00:00", "2024-07-14 00:00:00");
//        System.out.println(maskAddress("四川省成都市武侯区芳草街道芳草地小区6栋一单元1301号道芳草地小区金山乡"));
//        split("负面情绪方：客户负面情绪分类：流量消耗过快。");
//        System.out.println(JSON.toJSONString(parseEmotionStatus("情绪状态：有负面情绪投诉状态：无投诉情绪原因：客户对网络突然中断和维修服务电话无法接通表示不满。")));
//        regexTest("(?=.*(?:评价))(?=.*(?:10分|10星))", "如果您有收到微管家满意度评价短信，麻烦帮我3个选项都打“10分”哦，这是对小客服个人的评分[爱心]，然后截图发我，我再送您流量哦~~");
//        ttt();
//        testList();
//        testSubString("asdasdasd/asdasd");
//        listFtpPath("ftpPath", 3);
//        System.out.println(DesensitizationUtils.checkContainsChineseCharacters("15asdasg固安县"));
//        checkJkVoiceUrl("U:\\374\\0\\20210216\\6666\\1932510.V3");
//        parseTitle();
//        String systemDate = DateUtils.getTime();
//        System.out.println(DateUtils.addDays(DateUtils.parseDate(systemDate), (-1) * 3));
//        testException();
//        System.out.println(removeInvalidChar("笑嘻嘻：我们去\n哪里呢；", "笑嘻嘻："));
//        refreshPath();
//        System.out.println(JSON.toJSONString(parseEmotionStatus("```json{  \"情绪状态\": \"无负面情绪\",  \"投诉状态\": \"无投诉\",  \"情绪原因\": \"客户对流量使用规则不够清晰感到不满，担心流量超出导致额外费用。\"}```")));
    }


    //  http://10.40.7.26:9100/test/4c15c8d832e841509cfa1f8cc521316e.mp3?
    private static String[] getBucketAndObjNameInfo(String audioUrl) {
        log.info("解析Url: origin_audioUrl: {}", audioUrl);
        String[] pieces = audioUrl.split("/");
        String bucket = audioUrl.startsWith("http") ? pieces[3] : pieces[1];
        int index = audioUrl.indexOf(bucket);

        String suffix = ".mp3";
        int mp3Index = audioUrl.indexOf(".mp3");
        if (mp3Index == -1) {
            mp3Index = audioUrl.indexOf(".mp3");
            suffix = ".mp3";
        }

        String objectName = audioUrl.substring(index + bucket.length() + 1, mp3Index) + suffix;
        log.info("解析出信息, bucket:{}, objectName:{}", bucket, objectName);
        return new String[] {bucket, objectName};
    }

    private static String parseValidResByFixedConfig(String validValueListOfConfig, String originBigModelRes) {
            log.info("合法值:{}, 处理前缀后的大模型返回值:{}", validValueListOfConfig, originBigModelRes);
            if (StringUtils.isBlank(originBigModelRes)) {
                return null;
            }
        originBigModelRes = originBigModelRes.trim();
        if (originBigModelRes.startsWith("[") && originBigModelRes.endsWith("]")) {
            originBigModelRes = originBigModelRes.substring(1, originBigModelRes.length() - 1);
        }
            if (StringUtils.isNotBlank(validValueListOfConfig)) {
                String[] validValueList = validValueListOfConfig.split("；");
                for (String validValue : validValueList) {
                    if (validValue.trim().equals(originBigModelRes.trim())) {
                        return validValue.trim();
                    }
                }
            }
            return "INVALID_DEFAULT_RESULT";
    }
    private static void testRemove0(String value) {
        if (null != value && value.endsWith(".0")) {
            value = value.substring(0, value.length() - 2);
        }
        System.out.println(value);
    }

    private static void testMinio(String filePath) {
        int index = filePath.lastIndexOf(File.separator);
        if (index < 0) {
            index = filePath.lastIndexOf("/");
        }
        String remoteFileName = index == -1 ? filePath : filePath.substring(index + 1);
        System.out.println(remoteFileName);
    }


    // 处理大模型返回值，可能在target前后出现空格、分号等多余字符
    private static String removeInvalidChar(String originResult, String replaceStr) {
        if (StringUtils.isNotBlank(originResult) && StringUtils.isNotBlank(replaceStr) && originResult.startsWith(replaceStr)) {
            originResult = originResult.replace(replaceStr, "");
        }

        // 去除可能出现的分号、逗号、换行符
        originResult = originResult.replace("；", "");
        originResult = originResult.replace(";", "");
        originResult = originResult.replace("\n", "");
        originResult = originResult.replace("，", "");
        originResult = originResult.replace(",", "");

        return originResult.trim();
    }


    private static void parseTitle() {
        String json = "{\"image_url\":\"https://wap.zj.10086.cn/ai/mshop/amimages/2835d96a2ace4c629dd758b73ea76af8.png\"," +
                "\"description\":\"微管家专属领取50GB流量包-128档\",\"link_url\":\"https://wwurl.rtxapp.com/nl/check_url?id=S8nFkiwZTZqXkIi\"," +
                "\"title\":\"微管家专属领取50GB流量包-128档\"}";
        JSONObject jsonObject = JSONObject.parseObject(json);
        System.out.println(jsonObject.get("title"));
    }

    private static void checkJkVoiceUrl(String voiceUrl) {
        int secondSlash = voiceUrl.indexOf("\\", voiceUrl.indexOf("\\") + 1);
        String pathStr = voiceUrl.substring(secondSlash).
                replaceAll("\\\\", "/");
        voiceUrl = "ftp://ftpuser:iflytek2018@10.40.7.19" + pathStr;
        System.out.println(voiceUrl);

        RemoteLinkProperties ftpConfig = getPropertyFromFtpUrl(voiceUrl);
        System.out.println(JSON.toJSONString(ftpConfig));

        // 目录
        String ftpDirPath = ftpConfig.getLinkRootPath()
                .substring(0, ftpConfig.getLinkRootPath().lastIndexOf("/"));
        System.out.println(ftpDirPath);
    }

    public static RemoteLinkProperties getPropertyFromFtpUrl(String url) {
        RemoteLinkProperties obj = new RemoteLinkProperties();
        if (true) {
            String username = null;
            String password = null;
            String hostname = null;
            String port = "21";

            String userPwdIp = url.substring(6, url.indexOf('/', 6));
            String ipAndPort;
            if (userPwdIp.contains("@")) {
                String userAndPwd = url.substring(6, url.indexOf('@'));
                if (userAndPwd.contains(":")) {
                    username = userAndPwd.split(":")[0];
                    password = userAndPwd.split(":")[1];
                } else {
                    username = "anonymous";
                }

                ipAndPort = url.substring(url.indexOf('@') + 1, url.indexOf('/', 6));
            } else {
                ipAndPort = userPwdIp;
            }

            if (ipAndPort.contains(":")) {
                hostname = ipAndPort.split(":")[0];
                port = ipAndPort.split(":")[1];
            } else {
                hostname = ipAndPort;
            }

            // TODO 截取长度写死的？
            String fileNamePath = url.substring(url.indexOf('/', 6));
            obj.setUsername(username);
            obj.setUserPwd(password);
            obj.setLinkRootPath(fileNamePath);
            obj.setPort(Integer.parseInt(port));
            obj.setIp(hostname);
            obj.setFtpIsActiveMode("0");
            obj.setLinkProtocol(port.contains("22") ? "sftp" : "ftp");
            return obj;
        }
        throw new RuntimeException("通过原始音频地址解析ftp连接信息异常!");
    }

    private static void listFtpPath(String ftpPath, Integer timeOffSet) {
        List<String> dayList = new ArrayList<>();
        for (int i = 0; i <= timeOffSet; i++) {
            String dayStr = DateUtils.getDayByOffSet(DateUtils.YYYYMMDD, i);
            String path = ftpPath + Constants.SP + dayStr;
            dayList.add(path);
            System.out.println(path.substring(path.lastIndexOf(Constants.SP) + 1));
        }
        log.info("本次待扫描目录为: {}", JSON.toJSONString(dayList));
    }

    private static void refreshPath() {
        Integer timeOffSet = 0;
        List<String> folderPaths = new ArrayList<>();
        for (int i = 1; i <= Math.abs(timeOffSet) + 1; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -i); // 起始时间从昨天开始，向前偏移
            Date date = calendar.getTime();
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            String folder = df.format(date);
            String pathName = "/FTP/JSYD/" + folder + "/";
            folderPaths.add(pathName);
        }
        log.info("本次待扫描目录为: {}", JSON.toJSONString(folderPaths));
    }


    private static void testSubString(String ftpPathUrl) {
        String suffix = ftpPathUrl.substring(ftpPathUrl.lastIndexOf(Constants.DOT));
        System.out.println(suffix);
    }

    private static void testList() {
        List<MayuTest> list1 = new ArrayList<>();
        MayuTest obj1 = new MayuTest();
        obj1.setTaskId("AAAA");
        list1.add(obj1);

        MayuTest obj2 = new MayuTest();
        obj2.setTaskId("BBBB");
        list1.add(obj2);

        Optional<MayuTest> res = list1.stream().filter(r -> r.getTaskId().equals("BBBB")).findFirst();
        if (res.isPresent()) {
            System.out.println(JSON.toJSONString(res.get()));
        }
        System.out.println(JSON.toJSONString(list1));
    }


    private static void ttt() {
        String ftpPathUrl = "/asdasdasd/asdasd/ssd.mp3";
        String audioType = "";
        if (StringUtils.isBlank(audioType)) {
            int maybeDotIndex = ftpPathUrl.lastIndexOf(".");
            if (maybeDotIndex != -1) {
                String suffixStr = ftpPathUrl.substring(maybeDotIndex + 1);
                System.out.println(suffixStr);
            }
        }
    }

    private static void regexTest(String regexContent, String msgContent) {
        Pattern pattern = Pattern.compile(regexContent);
        System.out.println(pattern.matcher(msgContent).find());
    }

    private static ComplaintDetailsParseRes parseComplaintDetails(String conversationId, String bigModelRespStr) {
        log.info("根据大模型返回的投诉处理详细结果解析对象[{}]! bigModelRespStr:{}", conversationId, bigModelRespStr);
        if (StringUtils.isBlank(bigModelRespStr)) {
            return null;
        }
        ComplaintDetailsParseRes res = new ComplaintDetailsParseRes();
        String mircIfComplaintRes = null;
        String complaintSatisfactionRes = null;
        String complaintDissatisfactionCauseRes = null;

        try {
            String[] pieces = bigModelRespStr.split("；");
            if (pieces.length == 3) {
                String mircIfComplaintStr = pieces[0];
                mircIfComplaintRes = mircIfComplaintStr.substring(Constants.MIRC_IF_COMPLAINT_STR.length());
                String complaintSatisfactionStr = pieces[1];
                complaintSatisfactionRes = complaintSatisfactionStr.substring(Constants.COMPLAINT_SATISFACTION_STR.length());
                String complaintDissatisfactionCauseStr = pieces[2];
                complaintDissatisfactionCauseRes = complaintDissatisfactionCauseStr.substring(Constants.COMPLAINT_DISSATISFACTION_CAUSE_STR.length());
            }

            if (pieces.length == 1) {
                String piece1 = bigModelRespStr.substring(0, bigModelRespStr.indexOf(Constants.COMPLAINT_SATISFACTION_STR));
                mircIfComplaintRes = piece1.substring(Constants.MIRC_IF_COMPLAINT_STR.length());
                String piece2 = bigModelRespStr.substring(piece1.length(), bigModelRespStr.indexOf(Constants.COMPLAINT_DISSATISFACTION_CAUSE_STR));
                complaintSatisfactionRes = piece2.substring(Constants.COMPLAINT_SATISFACTION_STR.length());
                complaintDissatisfactionCauseRes = bigModelRespStr.substring(piece1.length() + piece2.length() + Constants.COMPLAINT_DISSATISFACTION_CAUSE_STR.length());
            }

            if (null != complaintDissatisfactionCauseRes && complaintDissatisfactionCauseRes.contains("。")) {
                complaintDissatisfactionCauseRes = complaintDissatisfactionCauseRes.substring(0, complaintDissatisfactionCauseRes.length() - 1);
            }

            res.setMircIfComplaint(mircIfComplaintRes);
            res.setComplaintSatisfaction(complaintSatisfactionRes);
            res.setComplaintDissatisfactionCause(complaintDissatisfactionCauseRes);
            return res;
        } catch (Exception e) {
            log.error("根据大模型返回的投诉处理详细结果解析对象异常[{}]! bigModelRespStr:{}", conversationId, bigModelRespStr);
            log.error("{}", e.getMessage(), e);
        }
        return null;
    }


    private static EmotionParseRes parseEmotionStatus(String bigModelRespStr) {
        if (StringUtils.isBlank(bigModelRespStr)) {
            return null;
        }
        EmotionParseRes res = new EmotionParseRes();
        String emotionStatusRes = null;
        String complaintStatusRes = null;
        String emotionCauseRes = null;
        try {
            // ```json{  "情绪状态": "无负面情绪",  "投诉状态": "无投诉",  "情绪原因": "客户对流量使用规则不够清晰感到不满，担心流量超出导致额外费用。"}```
            if (bigModelRespStr.startsWith("```json")) {
                bigModelRespStr = bigModelRespStr.replace("```json","").trim();
                if (bigModelRespStr.endsWith("```")) {
                    bigModelRespStr = bigModelRespStr.replace("```","").trim();
                }
                JSONObject jsonObject = JSON.parseObject(bigModelRespStr);
                emotionStatusRes = jsonObject.getString("情绪状态");
                complaintStatusRes = jsonObject.getString("投诉状态");
                emotionCauseRes = jsonObject.getString("情绪原因");
            } else {
                // 正常情况
                String[] pieces = bigModelRespStr.split("；");
                if (pieces.length == 3) {
                    String emotionStatusStr = pieces[0];
                    emotionStatusRes = emotionStatusStr.substring(Constants.EMOTION_STATUS_STR.length());
                    String complaintStatusStr = pieces[1];
                    complaintStatusRes = complaintStatusStr.substring(Constants.COMPLAINT_STATUS_STR.length());
                    String emotionCauseStr = pieces[2];
                    emotionCauseRes = emotionCauseStr.substring(Constants.EMOTION_CAUSE_STR.length());
                }

                // 情绪状态：有负面情绪投诉状态：无投诉情绪原因：客户对网络突然中断和维修服务电话无法接通表示不满。
                if (pieces.length == 1) {
                    String piece1 = bigModelRespStr.substring(0, bigModelRespStr.indexOf(Constants.COMPLAINT_STATUS_STR));
                    emotionStatusRes = piece1.substring(Constants.EMOTION_STATUS_STR.length());
                    String piece2 = bigModelRespStr.substring(piece1.length(), bigModelRespStr.indexOf(Constants.EMOTION_CAUSE_STR));
                    complaintStatusRes = piece2.substring(Constants.COMPLAINT_STATUS_STR.length());
                    emotionCauseRes = bigModelRespStr.substring(piece1.length() + piece2.length() + Constants.EMOTION_CAUSE_STR.length());
                }
            }

            if (null != emotionCauseRes && emotionCauseRes.contains("。")) {
                emotionCauseRes = emotionCauseRes.substring(0, emotionCauseRes.length() - 1);
            }

            res.setEmotionStatus(emotionStatusRes);
            res.setComplaintStatus(complaintStatusRes);
            res.setEmotionCause(emotionCauseRes);
            return res;
        } catch (Exception e) {
            log.error("{}", e.getMessage(), e);
        }
        return null;
    }

    // 负面情绪方：客户负面情绪分类：资费异议
    // 负面情绪方：客户；负面情绪分类：资费异议
    private static void split(String bigModelRespStr) {
        String negativeRoleRes = "";
        String negativeTypeRes = "";
        String[] pieces = bigModelRespStr.split("；");
        if (pieces.length == 2) {
            String negativeRoleStr = pieces[0];
            negativeRoleRes = negativeRoleStr.substring(Constants.NEGATIVE_ROLE_STR.length());
            String negativeTypeStr = pieces[1];
            negativeTypeRes = negativeTypeStr.substring(Constants.NEGATIVE_TYPE_STR.length());
        }
        if (pieces.length == 1) {
            String piece1 = bigModelRespStr.substring(0, bigModelRespStr.indexOf(Constants.NEGATIVE_TYPE_STR));
            negativeRoleRes = piece1.substring(Constants.NEGATIVE_ROLE_STR.length());
            negativeTypeRes = bigModelRespStr.substring(piece1.length() + Constants.NEGATIVE_TYPE_STR.length());
        }


        System.out.println(negativeRoleRes);
        if (negativeTypeRes.contains("。")) {
            negativeTypeRes = negativeTypeRes.substring(0, negativeTypeRes.length() - 1);
        }
        System.out.println(negativeTypeRes);
    }

    /**
     * 地址脱敏，脱敏规则：对目标会话所有消息进行分词，对分词结果进行正则匹配
     * (省|市|区|县|路|街|座|号|楼|栋|梯|室|巷|大道|花园|苑|幢|弄|单元|乡)
     * 是否包含以以上词组结尾的关键词
     */
    private static String desensitizedAddress(String text) {
        String[] pieces = text.split("，");
        Pattern pattern = Pattern.compile("(省|市|区|县|路|街|座|号|楼|栋|梯|室|巷|大道|花园|苑|幢|弄|单元|乡)");
        for (String piece : pieces) {
            Matcher matcher = pattern.matcher(piece);
            while (matcher.find()) {
                System.out.println(matcher.group());
                String desensitizedBankAddress = "**地址**";
                text = text.replace(piece, desensitizedBankAddress);
            }
        }
        return text;
    }

    public static String maskAddress(String input) {
        // 定义一个正则表达式，用于匹配地址信息
        String regex = "(?<province>[^省]+省|.+自治区)(?<city>[^市]+市)(?<district>[^区]+区|.+县)(?<street>[^路]+路|.+街)(?<dis>[^号]+号|.+座|.+栋|.+梯|.+室|.+巷|.+大道|.+苑|.+幢|.+弄|.+单元|.+乡)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        // 替换匹配到的地址信息为脱敏后的内容
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            String province = matcher.group("province");
            String city = matcher.group("city");
            String district = matcher.group("district");
            String street = matcher.group("street");
            String dis = matcher.group("dis");

            // 对省份、城市、区县和街道进行脱敏处理
            province = province.replaceAll(".", "*");
            city = city.replaceAll(".", "*");
            district = district.replaceAll(".", "*");
            street = street.replaceAll(".", "*");
            dis = dis.replaceAll(".", "*");

            // 将脱敏后的地址信息替换回原字符串
            matcher.appendReplacement(result, province + city + district + street + dis);
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private static void testWhileDate(String startTime, String endTime) {
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = DateUtils.convertToDate(startTime, DateUtils.YYYY_MM_DD_HH_MM_SS);
            endDate = DateUtils.convertToDate(endTime, DateUtils.YYYY_MM_DD_HH_MM_SS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 一个小时一个小时的跑
        int queryCount = 1;
        while (startDate.before(endDate)) {
            Date currentEndTimeDate = DateUtils.addHours(startDate, 1);
            String currentStartTimeStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, startDate);
            String currentEndTimeStr = DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, currentEndTimeDate);
            log.info("漏回检测重跑-第{}次查询时间范围[{} - {}]", queryCount, currentStartTimeStr, currentEndTimeStr);
            startDate = currentEndTimeDate;
            queryCount++;
        }
    }

    private static void testUuid() {
        Date conversationEndDate = DateUtils.parse2Date("2024-07-15 22:45:54", DateUtils.YYYY_MM_DD_HH_MM_SS);
        Date newDate = DateUtils.addHours(conversationEndDate, 24);
        System.out.println(DateUtils.formatDate(newDate, DateUtils.YYYY_MM_DD_HH_MM_SS));
        Date startDate = DateUtils.truncate(DateUtils.addDays(newDate, -2), Calendar.DATE);
        System.out.println(DateUtils.formatDate(startDate, DateUtils.YYYY_MM_DD_HH_MM_SS));
    }


    private static void testListSort() {
        List<MayuTest> list1 = new ArrayList<>();
        MayuTest m = new MayuTest();
        m.setTaskId("20240718");
        list1.add(m);

        MayuTest m1 = new MayuTest();
        m1.setTaskId("20240719");
        list1.add(m1);
        list1.sort((r1, r2) -> r2.getTaskId().compareTo(r1.getTaskId()));
        System.out.println(JSON.toJSONString(list1));
    }

    public static void random() {
        Random random = new Random();
        int randomNumber = 10000 + random.nextInt(90000); // 生成一个5位的随机数，范围在10000到90000之间
        System.out.println("Generated 5-digit random number: " + randomNumber);
    }


    private static void testPattern() {
        String msgContent = "哈哈 为什么买了随心会员 领取不了? 我对自由的向往?没有什么能够阻挡!";
        Pattern pattern = Pattern.compile("你|我|他|她|它|的|了");
        Matcher res = pattern.matcher(msgContent);
        while (res.find()) {
            System.out.println(JSON.toJSONString(res.group()));
        }
    }

    private static void reName1() {
        String line = "5716194363000r6IdurnUVA||15578866914|1|2024-05-22 07:14:48||||wav|/8.wav|河南省|南阳市|宁陵县|5|家宽开通||||||||||||\n";
        String[] infoArr = line.split("\\|", -1);
        System.out.println(infoArr.length);
    }

    private static void reName() {
        // 重命名图片
        String path = "/Users/mayu/Documents/workspace/codes/Mayu-ProjectOne/allGoodsInfo_202405061505168_1--涉黄.txt";
//        String path = "allGoodsInfo_202405061505168_1.txt";
//        String path2 = "/Users/mayu/Desktop/allGoodsInfo_202405061505168_1.txt";
        String category_description = "涉黄2";

        File file = new File(path);
        String parentPath = file.getParentFile().getAbsolutePath();

        String suffix = file.getName().substring(file.getName().indexOf("."));
        String originName = file.getName().substring(0, file.getName().indexOf(".")).split("--")[0];
        String newName = originName + "--" + category_description + suffix;
        file.renameTo(new File(parentPath, newName));
    }


    private static String[] getQueryConversationTimeRange(String orderTimeStr, String checkConversationRange) {
        if (StringUtils.isAnyBlank(orderTimeStr, checkConversationRange)) {
            log.warn("订单稽核: 订单时间[updateTime:{}]、或订单稽核规则中的会话稽核范围时间配置[checkConversationRange:{}]为空! ", orderTimeStr, checkConversationRange);
            return null;
        }
        try {
            Date orderTime = DateUtils.parse2Date(orderTimeStr, DateUtils.YYYY_MM_DD_HH_MM_SS);
            String[] timeRangeArr = checkConversationRange.split(",");
            if (timeRangeArr.length == 2) {

                Calendar orderCalender = Calendar.getInstance();
                orderCalender.setTime(orderTime);
                orderCalender.add(Calendar.HOUR, -Integer.parseInt(timeRangeArr[0]));
                String[] timeRangeRes = new String[2];
                timeRangeRes[0] = DateUtils.format(orderCalender.getTime(), DateUtils.YYYY_MM_DD_HH_MM_SS);

                orderCalender.setTime(orderTime);
                orderCalender.add(Calendar.HOUR, Integer.parseInt(timeRangeArr[1]));
                timeRangeRes[1] = DateUtils.format(orderCalender.getTime(), DateUtils.YYYY_MM_DD_HH_MM_SS);
                return timeRangeRes;
            }
        } catch (Exception e) {
            log.error("订单稽核: 计算会话查询时间范围异常! {}", e.getMessage(), e);
        }
        return null;
    }

    private static void testArr() {
        String cityIdParam = "10,20";
        String[] multiCityIds = cityIdParam.split(",");
        System.out.println(JSON.toJSONString(Arrays.asList(multiCityIds)));
    }

    private static void testCalendar() {
        String waitMinutes = "10";
        String userMsgTime = "2024-05-08 14:00:00";
        String earliestMircoReplayTime = "2024-05-08 14:09:59";
        try {
            int waitMinutesInt = Integer.parseInt(waitMinutes);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date userMsgDate = sdf.parse(userMsgTime);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(userMsgDate);

            calendar.add(Calendar.MINUTE, waitMinutesInt);
            // 如果微管家最近回复的消息，仍然大于加了最大等待分钟后的时间，就置为响应延迟
            if (earliestMircoReplayTime.compareTo(sdf.format(calendar.getTime())) >= 0) {
                System.out.println("======= 响应延迟 ======");
            }
        } catch (Exception e) {
            log.error("微管家回复分钟checkContentKeys:{} 转换错误! {}", waitMinutes, e.getMessage(), e);
        }
    }

    private static void testSubList() {
        List<String> list = new ArrayList<>();
        list.add("AAA");
        list.add("222");
        list.add("CCC");
        list.add("444");
        System.out.println(JSON.toJSONString(list.subList(1, list.size())));
    }

    private static void asda() {
        long count = 29;
        // 百分比
        double res = new BigDecimal((float) (5) / count).setScale(4, RoundingMode.HALF_UP).doubleValue();
        res = 100 * res;
        System.out.println(res + "%");
    }


    private static String getFileName(String filePath) {
        int index = filePath.lastIndexOf(File.separator);
        if (index < 0) {
            index = filePath.lastIndexOf("/");
        }
        return index == -1 ? filePath : filePath.substring(index + 1);
    }

    public static void oo(String folderPath) {
        // folderPath: /jsyd/20210630/jiake/xxxx.txt  /jsyd/20210630/jike/xxxx.txt
        int lastSlashInt = folderPath.lastIndexOf("/");
        int secondLastSlashInt = folderPath.lastIndexOf("/", lastSlashInt - 1);
        System.out.println(folderPath.substring(secondLastSlashInt + 1, lastSlashInt));
    }

    private static String ll(String filePath) {
        int index = filePath.lastIndexOf(File.separator);
        if (index < 0) {
            index = filePath.lastIndexOf("/");
        }
        String suffixName = index == -1 ? filePath : filePath.substring(index + 1);

        StringJoiner stringJoiner = new StringJoiner("/");
        LocalDate now = LocalDate.now();
        stringJoiner.add(now.getYear() + "")
                .add(now.getMonth().name())
                .add(now.getDayOfMonth() + "")
                .add(suffixName);
        return stringJoiner.toString();
    }

    private static void hh() {
        String wuXiaoRuleContent = "asdas|asdsad";
        String[] wuXiaoRuleArr = wuXiaoRuleContent.split("\\|");
        System.out.println(JSON.toJSONString(wuXiaoRuleArr));
    }

    private static void kk() {
        List<String> folderPaths = new ArrayList<>();
        String startDay = "20240322";
        String endDay = "20240411";
        Date startDate = DateUtils.parse2Date(startDay, "yyyyMMdd");
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);

        // 后移时间戳
        long currentTime = startCalendar.getTime().getTime();

        Date endDate = DateUtils.parse2Date(endDay, "yyyyMMdd");
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        while (endDate.getTime() > currentTime) {
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            DateFormat df = new SimpleDateFormat("yyyyMMdd");
            String folder = df.format(startCalendar.getTime());
            String pathName = "ftp/jsyd/" + folder + "/";
            folderPaths.add(pathName);
            currentTime = startCalendar.getTime().getTime();
        }
        System.out.println(JSON.toJSONString(folderPaths));

        String folderPath = folderPaths.get(0);
        int lastSlashInt = folderPath.lastIndexOf("/");
        int secondLastSlashInt = folderPath.lastIndexOf("/", lastSlashInt - 1);
        String upFolderPath = folderPath.substring(secondLastSlashInt + 1, lastSlashInt);
        System.out.println(upFolderPath);
    }

    public static String getFilePath(String url, String audioName) {
        FileUtil.mkdir("/data/file/");
        // fix， 解决url音频没有后缀，解析失败
        String suffix = getFileSuffix(StringUtils.isNotEmpty(audioName) ? audioName : url);
        String uuid = IdUtil.simpleUUID();
        String fileName = StringUtils.isNotEmpty(suffix) ? uuid + "." + suffix : uuid;
        fileName = "/data/file/" + fileName;
        int index = fileName.lastIndexOf("?");
        return index == -1 ? fileName : fileName.substring(0, index);
    }

    public static String getFileSuffix(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        }
        int indexOfDot = fileName.lastIndexOf(".");
        if (indexOfDot == -1) {
            return "mp3";
        }
        String suffix = fileName.substring(indexOfDot + 1);

        // 兼容录音后缀为.do的文件
        if (suffix.equalsIgnoreCase("do")) {
            return "mp3";
        }
        return suffix;
    }

    private static void te() {
        String str = "今天，享好礼，可以";
        String regx1 = "下个月|享好礼";
        String regx = ".*(下个月|享好礼).*";
        Pattern pattern1 = Pattern.compile(regx1);
        Pattern pattern = Pattern.compile(regx);
        System.out.println(pattern1.matcher(str));
        System.out.println(pattern1.matcher(str).matches());
        System.out.println(pattern1.matcher(str).find());
    }


    private static void test222() {
        String relativePath = "/1/0/20240311/107/1520320.V3";
        String cloudFilePath = relativePath.substring(relativePath.lastIndexOf("/") + 1);
        System.out.println(cloudFilePath);
        relativePath = relativePath.substring(0, relativePath.lastIndexOf("/") + 1);
        System.out.println(relativePath);
        String ftpPath = relativePath.substring(0, relativePath.lastIndexOf("/"));
        System.out.println(ftpPath);
        System.out.println(ftpPath.substring(1));
    }

    private static void test() {
        String rowDataStrWH = "##currentDupCount_###";
        rowDataStrWH.replace("currentDupCount", "2");
    }

    public static String getVoiceTime2(String folderPath) {
        String regex = "\\d{8}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(folderPath);
        while (matcher.find()) { // 查找所有匹配项
            String matchedString = matcher.group();
            log.info("=>> 匹配到的voiceTime字符串: {}", JSON.toJSONString(matchedString));
            Date infoDate = DateUtils.parse2Date(matchedString, DateUtils.YYYYMMDD);
            return DateUtils.formatDate(infoDate, DateUtils.YYYY_MM_DD_HH_MM_SS);
        }
        throw new RuntimeException(String.format("未匹配到的voiceTime字符串! [folderPath:%s], [pattern:%s]", folderPath, DateUtils.YYYYMMDD));
    }


    private static String getVoiceTime(String folderPath) {
        // folderPath: /jsyd/20210630/jiake/xxxx.txt  /jsyd/20210630/jike/xxxx.txt
        int lastSlashInt = folderPath.lastIndexOf("/");
        int secondLastSlashInt = folderPath.lastIndexOf("/", lastSlashInt - 1);
        int thirdLastSlashInt = folderPath.lastIndexOf("/", secondLastSlashInt - 1);
        String voiceTimeStr = folderPath.substring(thirdLastSlashInt + 1, secondLastSlashInt);
        try {
            Date infoDate = DateUtils.parse2Date(voiceTimeStr, DateUtils.YYYYMMDD);
            return DateUtils.formatDate(infoDate, DateUtils.YYYY_MM_DD_HH_MM_SS);
        } catch (Exception e) {
            int fourthLastSlashInt = folderPath.lastIndexOf("/", thirdLastSlashInt - 1);
            String tempVoiceTimeStr = folderPath.substring(fourthLastSlashInt + 1, thirdLastSlashInt);
            Date infoDate = DateUtils.parse2Date(tempVoiceTimeStr, DateUtils.YYYYMMDD);
            return DateUtils.formatDate(infoDate, DateUtils.YYYY_MM_DD_HH_MM_SS);
        }
    }

//    private static void testBlockQueue() {
//        BlockingQueue<Integer> sftpBlockingQueue = new ArrayBlockingQueue<>(10);
//        sftpBlockingQueue.add()
//    }


    public static void getChannel(String folderPath) {
        // folderPath: /jsyd/20210630/jiake/xxxx.txt  /jsyd/20210630/jike/xxxx.txt
        int lastSlashInt = folderPath.lastIndexOf("/");
        int secondLastSlashInt = folderPath.lastIndexOf("/", lastSlashInt - 1);
        log.info("=>> getChannel: {}", folderPath.substring(secondLastSlashInt + 1, lastSlashInt));
    }

    public static void refreshPathCK(String path, String fileNameFormat,
                                     List<String> pathExt, Integer timeOffSet, String who) {
        List<String> scanPaths = new ArrayList<>();
        DateFormat df = new SimpleDateFormat(fileNameFormat);
        for (int i = 1; i <= Math.abs(timeOffSet) + 1; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -i); // 起始时间从昨天开始，向前偏移
            Date date = calendar.getTime();
            String dateFolder = df.format(date);
            pathExt.forEach(v -> {
                scanPaths.add(path + v + "/" + dateFolder + "/");
            });
        }
        log.info("[{}]=>> 本次扫描到要处理的FTP目录集: {}", who, JSON.toJSONString(scanPaths));
    }

    public static void refreshPath(String path, String fileNameFormat, List<String> folderPaths,
                                   List<String> pathExt, Integer timeOffSet, String who) {
        for (int i = 1; i <= Math.abs(timeOffSet) + 1; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -i);// 起始时间从昨天开始，向前偏移
            Date date = calendar.getTime();
            DateFormat df = new SimpleDateFormat(fileNameFormat);
            String folder = df.format(date);
            String pathName = path + folder + "/";
            log.info("pathName:{}", pathName);

            pathExt.forEach(v -> {
                String r = pathName + v + "/";
                log.info("--> {}", r);
                folderPaths.add(r);
            });
        }
//        log.info("[{}]=>> 本次扫描到要处理的FTP目录集: {}", who, JSON.toJSONString(folderPaths));
    }

    private static void testFtpScanPath() {
        List<String> folderPaths = new ArrayList<>();
        Integer timeOffSet = 3;
        if (timeOffSet != null && timeOffSet > 0) {
            // 默认从前天开始 向前倒推timeOffSet天？
            for (int i = 2; i <= Math.abs(timeOffSet); i++) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_MONTH, -i);
                Date date = calendar.getTime();
                DateFormat df = new SimpleDateFormat("yyyyMMdd");
                String folder = df.format(date);
                String pathName = "FtpPath" + folder + "/";
                folderPaths.add(pathName);
            }
        }
        log.info("====>>>> {}", JSON.toJSONString(folderPaths));
    }

    private static Map<String, String> getNeedQueryFtpDateDirPathMap(String rootDirPath, String startDateStr, String endDateStr) {
        Map<String, String> needQueryFtpDateDirs = new TreeMap<>();
        if (startDateStr.equals(endDateStr)) {
            needQueryFtpDateDirs.put(startDateStr, rootDirPath + "/" + startDateStr);
            return needQueryFtpDateDirs;
        }
        if (endDateStr.compareTo(startDateStr) > 0) {
            Date currentDate = DateUtils.parse(startDateStr, "yyyyMMdd");
            Date endDate = DateUtils.parse(endDateStr, "yyyyMMdd");
            while (!endDate.before(currentDate)) {
                String currentStr = DateUtils.formatDate(currentDate, "yyyyMMdd");
                needQueryFtpDateDirs.put(currentStr, rootDirPath + "/" + currentStr);
                currentDate = DateUtils.addDays(currentDate, 1);
            }
        }
        log.info("需要查询的ftp上的随录文件目录: {}", JSON.toJSONString(needQueryFtpDateDirs));
        return needQueryFtpDateDirs;
    }

    private static List<String> getNeedQueryFtpDateDirPath(String rootDirPath, String startDateStr, String endDateStr) {
        List<String> needQueryFtpDateDirs = new ArrayList<>();
        if (startDateStr.equals(endDateStr)) {
            needQueryFtpDateDirs.add(startDateStr);
            return needQueryFtpDateDirs;
        }
        if (endDateStr.compareTo(startDateStr) > 0) {
            Date currentDate = DateUtils.parse(startDateStr, "yyyyMMdd");
            Date endDate = DateUtils.parse(endDateStr, "yyyyMMdd");
            while (!endDate.before(currentDate)) {
                needQueryFtpDateDirs.add(rootDirPath + "/" + DateUtils.formatDate(currentDate, "yyyyMMdd"));
                currentDate = DateUtils.addDays(currentDate, 1);
            }
        }
        return needQueryFtpDateDirs;
    }

    // 获取日期分区目录
    private static String getDatePartitionDir(String fileName) {
        String[] pieces = fileName.split("_");
        String containsStr = pieces[1];
        String datePartitionStr = containsStr.substring(0, 8);
        log.info("从fileName[{}]中获取到音频文件存储的分区目录:{}", fileName, datePartitionStr);
        return datePartitionStr;
    }

    private static void checkOs() {
        log.info("当前操作系统: {}", System.getProperty("os.name").toLowerCase());
    }

    private static void testStack(Integer whichOneNode) {
        Stack<List<String>> stack = new Stack<>();
        List<String> node1List = new ArrayList<>();
        node1List.add("AAA");
        List<String> node2List = new ArrayList<>();
        node2List.add("BBB");
        List<String> node3List = new ArrayList<>();
        node3List.add("CCC");

        stack.push(node1List);
        stack.push(node2List);
        stack.push(node3List);

        log.info(JSON.toJSONString(stack));

        int hasPopCount = 0;
        List<String> targetList = null;
        for (; hasPopCount < 3; hasPopCount++) {
            if (whichOneNode.equals(3 - hasPopCount)) {
                targetList = stack.pop();
                log.info("targetList: {}", JSON.toJSONString(targetList));
            } else {
                stack.pop();
            }
        }


    }

    private static void parseJson() {
        String keywordStackJson = "[[{\"index\":18,\"matchType\":[\"1\"],\"partId\":6,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"期望值\"},{\"index\":9,\"matchType\":[\"1\"],\"partId\":7,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"期望越高分越高\"}],[{\"index\":0,\"matchType\":[\"1\"],\"partId\":11,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"好的，\"},{\"index\":0,\"matchType\":[\"1\"],\"partId\":14,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"好的，\"},{\"index\":0,\"matchType\":[\"1\"],\"partId\":24,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"好的，\"},{\"index\":0,\"matchType\":[\"1\"],\"partId\":29,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"好的，\"},{\"index\":0,\"matchType\":[\"1\"],\"partId\":33,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"好的，\"},{\"index\":0,\"matchType\":[\"1\"],\"partId\":53,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"好的，\"},{\"index\":0,\"matchType\":[\"1\"],\"partId\":61,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"好的\"},{\"index\":0,\"matchType\":[\"1\"],\"partId\":70,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"好的，\"}],[{\"index\":3,\"matchType\":[\"1\"],\"partId\":11,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"10分\"},{\"index\":3,\"matchType\":[\"1\"],\"partId\":14,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"10分\"},{\"index\":3,\"matchType\":[\"1\"],\"partId\":24,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"9分\"},{\"index\":3,\"matchType\":[\"1\"],\"partId\":29,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"10分\"},{\"index\":3,\"matchType\":[\"1\"],\"partId\":33,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"9分\"},{\"index\":3,\"matchType\":[\"1\"],\"partId\":53,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"10分\"},{\"index\":3,\"matchType\":[\"1\"],\"partId\":70,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"10分\"}],[{\"index\":20,\"matchType\":[\"1\"],\"partId\":11,\"semanticLabelList\":[],\"type\":\"content\",\"value\":\"上网质量\"}]]";
        JSON.parseArray(keywordStackJson, LabelKeyword.class);
    }

    private static void getReadTimeRange() {
        String selfReadStartTime = "2023-12-02";
        String selfReadEndTime = "2023-12-03";

        // 默认精确到日(yyyy-MM-dd)，每次读取 昨天的数据
        DateTimeFormatter partitionDayFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 如果数据表没有记录，自定义配置生效
        if (StringUtils.isNoneBlank(selfReadEndTime, selfReadStartTime)) {
            try {
                String readStartTime = LocalDate.parse(selfReadStartTime, dayFormatter).format(partitionDayFormatter);
                String readEndTime = LocalDate.parse(selfReadEndTime, dayFormatter).format(partitionDayFormatter);
                log.info("====readStartTime:{}, readEndTime:{} =====", readStartTime, readEndTime);
            } catch (Exception e) {
                log.error("自定义读取hive数据时间区间解析错误! 请按规范配置或联系开发人员后重启! {}", e.getMessage(), e);
            }
        } else {
            // 数据表、及自定义配置都没有，就默认读取 昨天的数据
            log.info("缺失自定义读取hive数据时间范围配置! 默认读取昨天的数据!");
            String readEndTime = LocalDate.now().format(partitionDayFormatter);
            String readStartTime = LocalDate.now().minusDays(1).format(partitionDayFormatter);
            log.info("====readStartTime:{}, readEndTime:{} =====", readStartTime, readEndTime);
        }
    }


    private static void checkDatePattern() {
        String pattern = "\\d{8}";
        String stat_date = "20231204";
        System.out.println(stat_date.matches(pattern));
        System.out.println("20231204w".matches(pattern));
    }


    private static String parseTaskId(String ftpFileName) {
        return ftpFileName.substring(0, ftpFileName.lastIndexOf(".wav"));
    }

    // 异或的测试
    private static void testEor() {
        int i = 999;
        int j = 888;
        System.out.println(i ^ i ^ j);
        System.out.println(0 ^ i ^ j ^ i ^ j ^ i);
    }

    private static void testParseObject() {
        String arrStr = "[ []]";
        Integer[][] orderArry = JSON.parseObject(arrStr, Integer[][].class);
        System.out.println("orderArry=======" + orderArry.length);
    }

    // 小于返回-1 等于返回0
    private static void testStrCompare() {
        String orderTransTime = "2023-10-22 18:00:00";
        String remindTime = "2023-10-23";
        System.out.println("=======" + orderTransTime.compareTo(remindTime));
    }

    private static void testSkip() {
        List<String> res = new ArrayList<>();
        res.add("AAA");
        res.add("BBB");
        res.add("CCC");
        res.add("DDD");
        System.out.println("=======" + res.stream().skip(2).collect(Collectors.toList()));
        System.out.println("=======" + res.stream().skip(2).anyMatch(s -> "DDD".equals(s)));
    }

    private static void test11() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -7);
        Date beforeThreeMonthsTime = calendar.getTime();
        String defaultStartDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beforeThreeMonthsTime);
        System.out.println("三个月之前时间=======" + defaultStartDate);
    }

    private static String testJavaTempDir() {
        String fileName = "ffmpeg";

        String os = System.getProperty("os.name").toLowerCase();
        boolean isWindows = os.contains("windows");
        boolean isMac = os.contains("mac");
        if (!isWindows && !isMac) {
            return fileName;
        }
        String suffix = "";
        if (isWindows) {
            suffix = ".exe";
        } else if (isMac) {
            suffix = "-osx";
        }
        String arch = System.getProperty("os.arch");
        File dirFolder = new File(System.getProperty("java.io.tmpdir"), "jave/");
        File ffmpegFile = new File(dirFolder, "ffmpeg" + "-" + arch + suffix);
        System.out.println(ffmpegFile.getAbsolutePath());
        return dirFolder.getAbsolutePath();
    }


    // 加密配置文件中的字符串
    private static void handleStr() {
        String fileSuffix = FileUtil.getSuffix("asdasdasdasdjpg");
        MayuTest test = new MayuTest();
        test.setId(IdUtil.simpleUUID());
        test.setCreateTime(new Date());
        test.setUpdateTime(LocalDateTime.now());

        List<MayuTest> list = new ArrayList<>();
        list.add(test);
        list.add(test);

        String jsonStr = JacksonUtil.to(list);
        System.out.println(jsonStr);

        List<MayuTest> l = JacksonUtil.from(jsonStr, new TypeReference<List<MayuTest>>() {
        });
        for (MayuTest t : l) {
            System.out.println(JacksonUtil.to(t));
        }
    }
}
