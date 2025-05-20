package com.mayu.practice.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.mayu.practice.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: 马瑜
 * @Date: 2024/11/21 17:56
 * @Description:
 */
@Slf4j
public class Test03 {

    public static void main(String[] args) {
        System.out.println(String.format("%03d", 1) + ".txt");

        dataStr();
//        System.out.println(DateUtils.getStartTimeStr(-1));
//
//        String timestamp = String.valueOf(new Date().getTime());
//        System.out.println(timestamp);
//        System.out.println(calcSignature(timestamp, "T1100832190", "KYi8vHktYsDwAHUy"));

//        String filePath = "/Users/mayu/Desktop/截图/test.xlsx";
//        File file = new File(filePath);
//
//
//        List<List<String>> headTitles = buildH();
//        log.info("==headTitles: {} ==", JSON.toJSONString(headTitles));
//        List<List<Object>> contentList = buildC();
//        log.info("==contentList: {} ==", JSON.toJSONString(contentList));
//        EasyExcel.write(file)
//                .sheet("test")
//                .head(headTitles)
////                .registerWriteHandler(new Custemhandler())
//                .doWrite(contentList);
    }

    private static void dataStr() {
        Date systemDate = DateUtils.truncate(new Date(), Calendar.DATE);
        Date endDate = DateUtils.truncate(DateUtils.addHours(systemDate, (-1)*(480/60)), Calendar.MINUTE);
        Date startDate = DateUtils.truncate(DateUtils.addDays(endDate, -1 * 1), Calendar.MINUTE);

        System.out.println(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, startDate));
        System.out.println(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, endDate));
    }

    private static String calcSignature(String timestamp, String c, String se) {
        String s = "client_id=" + c + "&timestamp=" + timestamp + "&client_secret=" + se;
        return DigestUtils.md5DigestAsHex(s.getBytes());
    }

    public static final String getDateStrByOffSet(int timeType, int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(timeType, offset);
        Date beforeThreeMonthsTime = calendar.getTime();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(beforeThreeMonthsTime);
    }

//    public static String getTodayStartTimeStr() {
//        Calendar calendar = Calendar.getInstance();
//        // 移除时分秒信息
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//        return parseDateToStr(YYYY_MM_DD_HH_MM_SS, calendar.getTime());
//    }

    public static List<List<String>> buildH() {
        String jsonString = "[[\"工单编号\"],[\"录音编号\"],[\"录音时间\"],[\"机构名称\"],[\"装维手机号\"],[\"装维工号\"],[\"分析状态\"],[\"转写文本\"],[\"服务敬语\",\"敬语其它礼貌\"],[\"服务敬语\",\"敬语结束语\"],[\"服务敬语\",\"敬语开口语\"],[\"服务忌语\",\"忌语不耐烦\"],[\"服务忌语\",\"忌语推诿\"],[\"服务忌语\",\"忌语忌语脏话\"],[\"宽带类\",\"投诉倾向\"],[\"宽带类\",\"离网倾向\"],[\"宽带类\",\"时间_改约\"],[\"宽带类\",\"撤单退单\"],[\"改约原因一级\"],[\"改约原因二级\"],[\"退单原因一级\"],[\"退单原因二级\"]]";
        return JSON.parseObject(jsonString, new TypeReference<List<List<String>>>() {
        });
    }

    public static List<List<Object>> buildC() {
        String jsonString = "[[\"SX-101-20241121-1732181602579-91\",\"2024112117330d9015\",\"2024-11-21 17:33:22\",\"凉山州\",\"13919044481\",\"SX_GZH_WorkUserId_32\",\"分析成功\",\"客服：您好听得见吗？真是烦人，您傻我真是服了你真笨。\\r\\n\",\"违规\",\"合格\",\"合格\",\"违规\",\"违规\",\"违规\",\"--\",\"--\",\"违规\",\"违规\",\"\",\"\",\"\",\"\"]]";
        return JSON.parseObject(jsonString, new TypeReference<List<List<Object>>>() {
        });
    }


}
