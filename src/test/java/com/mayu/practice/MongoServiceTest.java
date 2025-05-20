package com.mayu.practice;

import com.alibaba.fastjson.JSON;
import com.mayu.NeptuneApplication;
import com.mayu.practice.po.PlayTest;
import com.mayu.practice.po.bis.ResourceInfo;
import com.mayu.practice.po.bis.StateStats;
import com.mayu.practice.test.dto.DupGroupCount;
import com.mayu.practice.utils.DateUtils;
import com.mayu.practice.utils.WorkDayUtils;
import com.mayu.qw.repository.ConversationRecordRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoCursor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @Autowired
    private WorkDayUtils workDayUtils;
    @Resource
    private ConversationRecordRepository conversationRecordRepository;

    @Test
    public void testBulkOperations() {

        BulkOperations operations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, PlayTest.class);

        Query query1 = new Query().addCriteria(Criteria.where("micName").is("测试1"));
        Query query2 = new Query().addCriteria(Criteria.where("micName").is("马瑜"));
        Query query3 = new Query().addCriteria(Criteria.where("micName").is("马云飞"));
        operations.upsert(query1, Update.update("cityName", "测试"));
        operations.upsert(query2, Update.update("cityName", "巴中"));
        operations.upsert(query3, Update.update("cityName", "巴中1"));

        BulkWriteResult result = operations.execute();

        System.out.println("插入数量: " + result.getInsertedCount()); // 0
        System.out.println("已存在数量: " + result.getMatchedCount()); // 1
        System.out.println("更新数量: " + result.getModifiedCount()); // 1
        System.out.println("Upsert数量: " + result.getUpserts().size()); // 1
    }


    @Test
    public void findWithoutConversationId() throws Exception {
        conversationRecordRepository.findWithoutConversationId();
    }
    @Test
    public void testWorkDayUtils() throws ParseException {
        boolean valid = workDayUtils.isValidTime("2024-05-08 07:06:00", "08:00:00", "18:00:00");
        System.out.println(valid);
    }

    @Test
    public void agg() {

        // 过滤条件
        Criteria condition = Criteria.where("micName").exists(true);
        condition.andOperator(Criteria.where("cityName").exists(true));

        // having条件
        int conditionCount = 3;
        Criteria countCondition = Criteria.where("dupCount").is(conditionCount);

        // 聚合条件: 主叫与被叫号码 均相同的 数据
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(condition),
                Aggregation.group("micName", "cityName").count().as("dupCount")
//                Aggregation.match(countCondition)
        );
        AggregationResults<DupGroupCount> results = mongoTemplate.aggregate(agg, "website", DupGroupCount.class);
        if (results.getMappedResults().isEmpty()) {
           log.info("=== 聚合结果空 ===");
           return;
        }
        log.info("=== 聚合结果:{} ===", JSON.toJSONString(results.getMappedResults()));
    }

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
                    .projection(getBasicDBObject0())
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
    @Test
    public void handleResourceInfo() throws ParseException {
        String startTimeStr = "2023-11-01 00:00:00";
        String endTimeStr = "2023-11-01 23:00:00";
        Query timeQuery = new Query();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timeQuery.addCriteria(Criteria.where("attr.channel").is("default"));
        timeQuery.addCriteria(Criteria.where("attr.voiceTime")
                .gte(simpleDateFormat.parse(startTimeStr))
                .lt(simpleDateFormat.parse(endTimeStr)));
        MongoCursor<Document> cursor = null;
        try {
            log.info("开始进行游标查询! batchSize:3000, query: {}", JSON.toJSONString(timeQuery));
            cursor = mongoTemplate.getCollection("resourceInfo")
                    .find(timeQuery.getQueryObject())
                    .projection(getBasicDBObject())
                    .batchSize(20)
                    .noCursorTimeout(true)
                    .iterator();
            Document doc = null;
            while (cursor.hasNext()) {
                doc = cursor.next();
                ResourceInfo simpleInfo = JSON.parseObject(JSON.toJSONString(doc), ResourceInfo.class);
                log.info("{}", JSON.toJSONString(simpleInfo));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private static BasicDBObject getBasicDBObject0() {
        BasicDBObject projectionFields = new BasicDBObject();
        projectionFields.append("cityName", 1);
        return projectionFields;
    }

    private static BasicDBObject getBasicDBObject() {
        BasicDBObject projectionFields = new BasicDBObject();
        projectionFields.put("taskId", 1);
        projectionFields.put("third", 1);
        projectionFields.put("label", 1);
        projectionFields.put("attr.seqNo", 1);
        projectionFields.put("attr.tel", 1);
        projectionFields.put("attr.callType", 1);
        projectionFields.put("attr.voiceTime", 1);
        projectionFields.put("attr.duration", 1);
        projectionFields.put("attr.mp3Url", 1);
        projectionFields.put("attr.text.wholeFormatOut", 1);
        return projectionFields;
    }
}
