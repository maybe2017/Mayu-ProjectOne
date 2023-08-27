package com.mayu.practice.po;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Author: 马瑜
 * @Date: 2023/5/26 10:32
 * @Description:
 */
@Data
@Document("website")
public class PlayTest {
    private String startTime;
    private String micName;
    private String cusName;
    private Integer cusSpeakName;
    private String cityName;
}
