package com.mayu.practice.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.mayu.practice.po.HolidaysRecord;
import com.mayu.practice.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class HolidaysRecordService {

    @Value("${holidays.url:http://timor.tech}")
    private String holidaysUrl;
    @Resource
    private MongoTemplate mongoTemplate;

    public void updateHolidaysRecord(int year) {
        String holidays = HttpClientUtil.doGet(holidaysUrl + "/api/holiday/year/" + year, Maps.newHashMap());
        Map map = JSON.parseObject(holidays, Map.class) == null ? new HashMap<>() : JSON.parseObject(holidays, Map.class);
        if (map.get("code") != null) {
            if (Integer.parseInt(String.valueOf(map.get("code"))) == 0) {
                HolidaysRecord holidaysRecord = new HolidaysRecord();
                holidaysRecord.setYear(year);
                holidaysRecord.setHolidays(holidays);

                Query query = new Query();
                query.addCriteria(Criteria.where("year").is(year));
                HolidaysRecord exitsHolidaysRecord = mongoTemplate.findOne(query, HolidaysRecord.class);

                if (null == exitsHolidaysRecord) {
                    mongoTemplate.save(holidaysRecord);
                }
            }
        }
    }

    public Map<String, Object> getHolidaysByYear(int currentYear) {
        HolidaysRecord exitsHolidaysRecord = mongoTemplate.findOne(new Query().addCriteria(Criteria.where("year").is(currentYear)), HolidaysRecord.class);
        String respJson;
        if (null != exitsHolidaysRecord) {
            respJson = exitsHolidaysRecord.getHolidays();
        } else {
            respJson = HttpClientUtil.doGet(holidaysUrl + "/api/holiday/year/" + currentYear, Maps.newHashMap());
            updateHolidaysRecord(currentYear);
        }
        Map<String, Object> respJsonObj = JSON.parseObject(respJson, Map.class) == null ? new HashMap<>() : JSON.parseObject(respJson, Map.class);
        Map<String, Object> holidaysJsonObj = new HashMap<>();
        if (respJsonObj.get("code") != null) {
            if (Integer.parseInt(String.valueOf(respJsonObj.get("code"))) == 0) {
                holidaysJsonObj = respJsonObj.get("holiday") == null ? new HashMap<>() : (Map) respJsonObj.get("holiday");
            }
        }
        return holidaysJsonObj;
    }
}
