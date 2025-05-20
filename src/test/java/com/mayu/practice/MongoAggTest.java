package com.mayu.practice;

import com.alibaba.fastjson.JSON;
import com.mayu.NeptuneApplication;
import com.mayu.practice.po.statistics.AccessSysType;
import com.mayu.practice.po.statistics.DataAccessStatistics;
import com.mayu.practice.po.statistics.DayGroupCountRes;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;

/**
 * @Author: 马瑜
 * @Date: 2024/2/22 15:55
 * @Description:
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NeptuneApplication.class)
public class MongoAggTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testAgg() {
        String startDay = "2024-06-02";
        String endDay = "2024-06-04";

        Criteria criteria3 = Criteria.where("parseDataDate").gte(startDay).lte(endDay);
        Aggregation countAgg = Aggregation.newAggregation(
                match(criteria3),
                Aggregation.group("parseDataDate").count().as("dayCount"),
                Aggregation.count().as("count")
        );
        AggregationResults<DayGroupCountRes> results3 = mongoTemplate.aggregate(countAgg, "dataAccessStatistics", DayGroupCountRes.class);
        System.out.println(JSON.toJSONString(results3));
    }

    @Test
    public void testInc() {
        Query query = new Query().addCriteria(Criteria.where("id").is("665d5e791fbd994cbce17e9c"));
        Update update = new Update();
        update.set("updateTime", new Date());
        update.inc("updateCount", 1);
        mongoTemplate.updateFirst(query, update, DataAccessStatistics.class);
    }

    @Test
    public void buildData() {
        DataAccessStatistics record = DataAccessStatistics.builder()
                .accessDataCount(1000)
                .parseDataDate("2024-06-03")
                .filterDataCount(200)
                .bizExceptionCount(100)
                .trySubmitCount(1700)
                .qualitySuccCount(1700)
                .accessSys(AccessSysType.WORK_PLAT.getValue())
                .build();

        DataAccessStatistics record2 = DataAccessStatistics.builder()
                .accessDataCount(500)
                .parseDataDate("2024-06-03")
                .filterDataCount(50)
                .bizExceptionCount(50)
                .trySubmitCount(400)
                .qualitySuccCount(400)
                .accessSys(AccessSysType.APP_INSTALL.getValue())
                .build();

        DataAccessStatistics record3 = DataAccessStatistics.builder()
                .accessDataCount(500)
                .parseDataDate("2024-06-03")
                .filterDataCount(44)
                .bizExceptionCount(56)
                .trySubmitCount(400)
                .qualitySuccCount(400)
                .accessSys(AccessSysType.APP_INSTALL.getValue())
                .build();

        DataAccessStatistics record7 = DataAccessStatistics.builder()
                .accessDataCount(1000)
                .parseDataDate("2024-06-02")
                .filterDataCount(200)
                .bizExceptionCount(100)
                .trySubmitCount(1700)
                .qualitySuccCount(1700)
                .accessSys(AccessSysType.WORK_PLAT.getValue())
                .build();

        DataAccessStatistics record8 = DataAccessStatistics.builder()
                .accessDataCount(500)
                .parseDataDate("2024-06-02")
                .filterDataCount(50)
                .bizExceptionCount(50)
                .trySubmitCount(400)
                .qualitySuccCount(400)
                .accessSys(AccessSysType.APP_INSTALL.getValue())
                .build();

        DataAccessStatistics record9 = DataAccessStatistics.builder()
                .accessDataCount(500)
                .parseDataDate("2024-06-02")
                .filterDataCount(44)
                .bizExceptionCount(56)
                .trySubmitCount(400)
                .qualitySuccCount(400)
                .accessSys(AccessSysType.APP_INSTALL.getValue())
                .build();

        List<DataAccessStatistics> data = new ArrayList<>();
        data.add(record);
        data.add(record2);
        data.add(record3);
        data.add(record7);
        data.add(record8);
        data.add(record9);
        mongoTemplate.insert(data, DataAccessStatistics.class);
    }
}
