package com.mayu.practice.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    private DateUtils() {
    }

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    static SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM");
    static SimpleDateFormat syf = new SimpleDateFormat("yyyy");

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    public static SimpleDateFormat sdfs = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);


    private static void testChar(String phoneNumber) {
        String lastStr = phoneNumber.substring(phoneNumber.length() - 1);
        if (StringUtils.isNumeric(lastStr)) {
            int num = Integer.valueOf(lastStr);
            int nextNum = num + 1;
            System.out.println(phoneNumber.substring(0, phoneNumber.length() - 1) + nextNum);
        }
    }

    private static void testCheckNumber(String phoneNumber) {
        if (!StringUtils.isNumeric(phoneNumber)) {
            System.out.println("----非数字---");
        }
    }

    public static String getDateOffset(String startTimeStr, int offset, String formatStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime(formatStr, startTimeStr));
        calendar.add(Calendar.DATE, offset);
        return new SimpleDateFormat(formatStr).format(calendar.getTime());
    }

    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    public static String getDateOffset(String startTimeStr, int offset) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(startTimeStr));
            calendar.add(Calendar.DATE, offset);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {

        }
        return "";
    }

    public static String getDateBegin(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, offset);
        return sdf.format(calendar.getTime());
    }

    public static Date getDate(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, offset);
        return calendar.getTime();
    }

    public static Date getDateNoHMS(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, offset);
        // 时
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        // 分
        calendar.set(Calendar.MINUTE, 0);
        // 秒
        calendar.set(Calendar.SECOND, 0);
        // 毫秒
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    //获取昨天日期字符串 yyyy-MM-dd
    public static String getYestDay() {
        //计算日期，计算1天前的时间
        Calendar s = Calendar.getInstance();
        s.add(Calendar.DATE, -1);
        return sdf.format(s.getTime());
    }

    /*
    获取月份格式化yyyy-MM
     */
    public static String getMonthByTime(long time) {
        return smf.format(time);
    }

    /*
    获取年份格式化yyyy
     */
    public static String getYearByTime(long time) {
        return syf.format(time);
    }


    //获取前31天日期字符串 yyyy-MM-dd
    public static String getOneMonthAgo() {
        //计算日期，计算30天前的时间
        Calendar s = Calendar.getInstance();
        s.add(Calendar.DATE, -31);
        String time = sdf.format(s.getTime());
        return time;
    }

    //转换日期格式
    public static String fomateDate(Date date) {
        //计算日期，计算1天前的时间
        return sdf.format(date.getTime());
    }

    public static String formatDate(Date date, SimpleDateFormat sdf) {
        //计算日期，计算1天前的时间
        return sdf.format(date.getTime());
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    //字符串转日期格式
    public static Date StrToDate(String str) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = format.parse(str);
        return date;
    }

    //
    public static Date simpleDateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        ParsePosition pos = new ParsePosition(8);
        Date date_ = formatter.parse(dateString, pos);
        return date_;
    }

    public static String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    //整数(秒数)转换为时分秒格式(xx:xx:xx)
    public static String secToTime(int time) {
        String timeStr = null;

        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "0秒";
        else {
            time = time / 1000;
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                if (time > 60) {
                    timeStr = minute + "分钟" + second + "秒";
                } else {
                    timeStr = time + "秒";
                }
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99小时59分钟59秒";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = hour + "小时" + minute + "分钟" + second + "秒";
            }
        }
        return timeStr;
    }

    /**
     * 计算两个时间差的秒数
     */
    public static long getDateDiffSec(Date endDate, Date nowDate) {
        long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少秒//输出结果
        return diff / ns;
    }

    /**
     * 判断时间是否在时间段内
     *
     * @param nowTime
     * @param beginTime
     * @param endTime
     * @return
     */
    public static boolean belongCalendar(Date nowTime, Date beginTime, Date endTime) {
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(beginTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        String startTimeStr = "10:20";
        String endTimeStr = "15:20";
        System.out.println(belongTime(startTimeStr, endTimeStr, new Date()));
    }


    /**
     * 判断时间是否在时间段内
     *
     * @param date
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean belongTime(String startTime, String endTime, Date date) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date now = null;
        Date beginDate = null;
        Date endDate = null;
        try {
            now = df.parse(df.format(date));
            beginDate = df.parse(startTime);
            endDate = df.parse(endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean flag = belongCalendar(now, beginDate, endDate);
        System.out.println("时间是否在" + startTime + "--" + endTime + "之间，结果：" + flag);
        return flag;
    }
}
