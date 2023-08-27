package com.mayu.practice;

import com.alibaba.fastjson.JSON;
import com.mayu.practice.po.PlayTest;
import com.mayu.practice.po.bis.ResourceInfo;
import com.mayu.practice.po.bis.StateStats;
import com.mayu.practice.utils.DateUtils;
import com.mongodb.client.MongoCursor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @Author: 马瑜
 * @Date: 2023/5/9 10:46
 * @Description: TODO
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = NeptuneApplication.class)
public class MongoServiceTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void testGroup() {
        Date endTime = DateUtils.getDateNoHMS(0);
        Date beginTime = DateUtils.getDateNoHMS(-4);;

        TypedAggregation<ResourceInfo> agg;
        agg = newAggregation(ResourceInfo.class,
                match(Criteria.where("attr.voiceTime").gte(beginTime).lt(endTime)
                        .and("text.inventedNode").exists(true)),
                match(Criteria.where("statisticsStatus").is("1")),
                group("attr.callType") // 单渠道 仅按呼叫类型分组
                        .sum("text.inventedNode.audioDuration").as("totalTime")
                        .sum("text.inventedNode.muteDuration").as("invalidAudioDuration")
                        .count().as("callTotal")
                        .max("text.inventedNode.audioDuration").as("maxTime")
                        .min("text.inventedNode.audioDuration").as("minTime")
        ).withOptions(AggregationOptions.builder().build());
        AggregationResults<StateStats> result = mongoTemplate.aggregate(agg, StateStats.class);
        List<StateStats> stateStatsList = result.getMappedResults();
        if (stateStatsList.isEmpty()) {
            return;
        }
        log.info("聚合结果: {}", JSON.toJSONString(stateStatsList));
    }

    @Test
    public void queryLastTimeRecord() {
        Query query = new Query();
        query.addCriteria(Criteria.where("startTime").gte("2023-01-01 00:00:00").lt("2024-01-01 00:00:00"));
        query.with(Sort.by("startTime").ascending());
        PlayTest po =  mongoTemplate.findOne(query, PlayTest.class);
        log.info("========>>>结果: {}", JSON.toJSONString(po));
    }
    @Test
    public void handleData() {
        String startTimeStr = "2023-04-05 00:00:00";
        String endTimeStr = "2023-06-05 00:00:00";
        Query timeQuery = new Query();
        timeQuery.addCriteria(Criteria.where("startTime").gte(startTimeStr).lt(endTimeStr));
        Document queryFields = new Document();
        queryFields.append("cityName", 1);
        MongoCursor<Document> cursor = null;
        try {
            log.info("开始进行游标查询! batchSize:3000, query: {}", JSON.toJSONString(queryFields));
            cursor = mongoTemplate.getCollection("website")
                    .find(timeQuery.getQueryObject())
                    .projection(queryFields)
                    .batchSize(2)
                    .noCursorTimeout(true)
                    .iterator();
            Document doc = null;
            List<String> conversationIdList = new ArrayList<>();
            while (cursor.hasNext()) {
                doc = cursor.next();
                PlayTest simpleInfo = JSON.parseObject(JSON.toJSONString(doc), PlayTest.class);
                log.info("{}", JSON.toJSONString(simpleInfo));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
