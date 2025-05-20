package com.mayu.practice.po;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("holidaysRecord")
@Data
public class HolidaysRecord {

    private String id;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 节假日列表及补班情况
     */
    private String holidays;

}
