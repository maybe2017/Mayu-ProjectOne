package com.mayu.practice.config;

import com.mayu.practice.po.DateRange;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: 马瑜
 * @Date: 2023/6/20 14:03
 * @Description:
 */
@Configuration
@Data
@Slf4j
public class EmailConfig {

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${message.monitor.email.sendTo}")
    private String sendTo;

    @Value("${message.monitor.email.delayText}")
    private String delayText;

    @Value("${message.monitor.email.pushSuspendText}")
    private String pushSuspendText;

    @Value("${message.monitor.email.subject}")
    private String subject;

    @Value("${message.monitor.suspendValidTimeRange:}")
    private String suspendValidTimeRange;

    public List<DateRange> getSuspendValidTimeRange() {
        return checkAndGetTimeRange();
    }

    private List<DateRange> checkAndGetTimeRange() {
        return doCheckAndGetTimeRange(this.suspendValidTimeRange);
    }

    public List<DateRange> doCheckAndGetTimeRange(String configStr) {
        List<DateRange> res = new ArrayList<>();
        if (StringUtils.isNotBlank(configStr)) {
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");
            String[] pieces = configStr.split(",");

            for (String piece : pieces) {
                piece = piece.trim();
                String[] rangeArr = piece.split("-");

                boolean valid = false;
                if (rangeArr.length == 2) {
                    try {
                        Date leftDate = df.parse(rangeArr[0].trim());
                        Date rightDate = df.parse(rangeArr[1].trim());
                        if (leftDate.before(rightDate)) {
                            valid = true;
                            res.add(new DateRange(rangeArr[0].trim(), rangeArr[1].trim()));
                        }
                    } catch (Exception e) {
                        log.error("{}", e.getMessage(), e);
                    }
                }
                if (!valid) {
                    log.warn("suspendValidTimeRange值:{} 配置非法! 忽略该区间!", piece);
                }
            }
        }
        return res;
    }

}
