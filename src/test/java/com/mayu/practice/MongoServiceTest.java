package com.mayu.practice;

import com.alibaba.fastjson.JSON;
import com.mayu.practice.po.PlayTest;
import com.mayu.practice.po.bis.ResourceInfo;
import com.mayu.practice.po.bis.StateStats;
import com.mayu.practice.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Date;
import java.util.List;

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
}
