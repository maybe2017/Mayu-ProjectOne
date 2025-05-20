package com.mayu.practice.utils;

import com.mayu.practice.service.HolidaysRecordService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @Author: 马瑜
 * @Date: 2024/5/7 14:52
 * @Description: 工作日、工作时间的判断
 */
@Component
public class WorkDayUtils {

    @Resource
    private HolidaysRecordService holidaysRecordService;

    // 判断是否在工作日，的工作时间范围之内
    public boolean isValidTime(String msgTimeStr, String workingStartTimeStr, String workingEndTimeStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(msgTimeStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int currentYear = calendar.get(Calendar.YEAR);
        SimpleDateFormat dayFormat = new SimpleDateFormat("MM-dd");
        String currentDay = dayFormat.format(date);

        // 1. 先判断消息的时间 是不是在工作日？
        boolean isWorkDay = false;
        Map<String, Object> holidaysJsonObj = holidaysRecordService.getHolidaysByYear(currentYear);
        if (holidaysJsonObj.get(currentDay) == null) {
            // 如果当前day不在json体中，说明不是节假日，但是可能是周末，要继续判断
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            isWorkDay = dayOfWeek >= Calendar.MONDAY && dayOfWeek <= Calendar.FRIDAY;
        } else {
            // 如果在json体中，可直接判断出是否是工作日还是假期。
            Map<String, Object> details = (Map) holidaysJsonObj.get(currentDay);
            isWorkDay = !(Boolean) details.get("holiday");
        }

        if (!isWorkDay) {
            return false;
        }


        // 2. 判断 HH:mm:ss 是否在工作时间范围内
        boolean isWorkTime = false;
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = timeFormat.format(date);
        isWorkTime = currentTime.compareTo(workingStartTimeStr) >= 0 && currentTime.compareTo(workingEndTimeStr) <= 0;
        return isWorkTime;
    }
}
