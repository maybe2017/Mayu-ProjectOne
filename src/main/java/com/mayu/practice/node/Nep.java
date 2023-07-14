package com.mayu.practice.node;

import com.mayu.practice.po.DuplicateKeyDto;
import com.mayu.practice.po.MayuTest;
import com.mayu.practice.po.Params;
import com.mayu.practice.utils.AESUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 马瑜
 * @Date: 2023/2/15 16:08
 * @Description: TODO
 */
@Slf4j
@Component
public class Nep implements CommandLineRunner {

    private static final String TASK_KEY = "mayu";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Value("${elasticsearch.faw}")
    private String fawIndex;
    @Value("${elasticsearch.index}")
    private String indexVoiceIndex;

    
    @Override
    public void run(String[] args) {
        log.info("=== ===== ===");

//        testMongoSave();
//        testES();
//        testMongoUpdateNestedField();
//        testMongoAggregation(null, null);
//        testMongoDeleteById();
    }



    private void testMongoDeleteById(){
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is("640162969b4699641c0dd49a"));
        mongoTemplate.remove(query, MayuTest.class);
    }


    private void testMongoUpdateNestedField() {

        MayuTest obj = new MayuTest();
        obj.setTaskId("test2");
        Params params = new Params();
        params.setName("AAAAAA");
        params.setPhone("AAAAAA");
        obj.setParams(params);

        Params testParam = new Params();


//        Map<String, String> params = new HashMap<>();
//        params.put("name", "mayu");
//        params.put("phone", "18328589630");

        // 测试结果: 没有赋值的字段在save后 会消失。
        mongoTemplate.save(obj);
    }

    public Map<String, Integer> testMongoAggregation(Date startTime, Date endTime){

        List<AggregationOperation> operations = new ArrayList<>();
        if (null != startTime && null != endTime) {
            MatchOperation matchOperation = Aggregation.match(Criteria.where("createTime").gte(startTime).lte(endTime));
            operations.add(matchOperation);
        }
        operations.add(Aggregation.group("taskId").first("taskId").as("taskId").count().as("count"));
        Aggregation agg = Aggregation.newAggregation(operations);
        AggregationResults<DuplicateKeyDto> results = null;
        try {
            results = mongoTemplate.aggregate(agg, "mayuTest", DuplicateKeyDto.class);
            List<DuplicateKeyDto> mappedResults = results.getMappedResults();
            log.info("{}", mappedResults);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("统计查询错误");
        }
        return null;
    }


    private void testMongoSave() {

        MayuTest mayuTest = new MayuTest();
        Params params = new Params();

        params.setName("name");
        params.setPhone("18328589630");

//        mayuTest.setStatus("1");
        mayuTest.setTaskId("BBBBB");
        mayuTest.setParams(params);

        // 测试结果: 没有赋值的字段在save后 会消失。
        mongoTemplate.save(mayuTest);
    }

    // 更新不存在的taskId数据
    private void testES() {
        updateEsDataAtSameTime();
    }

    // 同时更新2个es数据状态
    public void updateEsDataAtSameTime() {
        BulkRequest bulkRequest = new BulkRequest();
        String taskId = "scsj20220617552235";
//        String taskId = "scsj555555555555";
        String oldNickName = "殷蕊松";
        if (StringUtils.isNotBlank(oldNickName) && !oldNickName.endsWith("==")) {
            String newSaleName = AESUtils.encrypt(oldNickName);
            UpdateRequest fawReq = new UpdateRequest(fawIndex, "_doc", taskId);
            String str = "ctx._source.saleName='" + newSaleName + "';";
            fawReq.script(new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, str, Collections.emptyMap()));

            UpdateRequest voiceReq = new UpdateRequest(indexVoiceIndex, "doc", taskId);
            voiceReq.script(new Script(ScriptType.INLINE, Script.DEFAULT_SCRIPT_LANG, str, Collections.emptyMap()));

            bulkRequest.add(fawReq);
            bulkRequest.add(voiceReq);
        }

        // 同步
        bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
        try {
            // 同步等待响应 此处可能出现异常
            BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulkResponse.hasFailures()) {
                for (BulkItemResponse itemResponse : bulkResponse.getItems()) {
                    BulkItemResponse.Failure failure = itemResponse.getFailure();
                    if (null != failure && null != failure.getStatus()) {
                        if (RestStatus.NOT_FOUND == failure.getStatus()) {
                            log.warn("NOT_FOUND");
                        } else {
                            log.warn("es批量更新数据，存在失败: {}-{}", failure.getStatus(), failure.getCause());
                        }
                    }
                }
            }

        } catch (IOException e) {
            log.error("es批量同步数据失败:{}", e.getMessage(), e);
            throw new RuntimeException("es批量同步数据失败");
        }
    }


    public static void main(String[] args) {
//        String customerName = "马瑜晕";
//        if (customerName.length() == 2) {
//            customerName = customerName.charAt(0) + "*";
//        } else if (customerName.length() == 3) {
//            customerName = customerName.charAt(0)  + "*" + customerName.charAt(2);
//        } else {
//            int disCount = customerName.length() / 2;
//            System.out.println(disCount);
//            customerName = customerName.substring(0, customerName.length() - 1) + "*";
//        }
//        System.out.println(customerName);

//        ts();
//        tt();
//        ddd();
        ttt();
    }

    private static void ddd(){
        String str = "/car/e2bb1ac7be3f489f9051f25ef06baece.wav?1165";
        String[] pieces = str.split("/");
        String bucket = str.startsWith("http") ? pieces[3] : pieces[1];
        System.out.println(bucket);
        int index = str.indexOf(bucket);
        int wavIndex = str.indexOf(".wav");
        System.out.println(index);
        System.out.println(wavIndex);
        String objectName = str.substring(index+bucket.length()+1, wavIndex);
        System.out.println(objectName);
    }

    private static void ts(){
        List<MayuTest> labelList = new ArrayList<>();
        MayuTest test1 = new MayuTest();
        MayuTest test2 = new MayuTest();

        test1.setSeqNo(1L);

        labelList.add(test1);
        labelList.add(test2);

        System.out.println(labelList.stream()
                .sorted(Comparator.comparing(MayuTest::getSeqNo)).collect(Collectors.toList()));
    }


    public static void ttt() {
        String audioUrl = "http://192.168.147.80:9000/test/793f239a0bf941c7a17bbcd23e5c9861.wav?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=minioadmin%2F20230419%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20230419T112151Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=705331f4db2c23c7abe67a052cf6b8452d518fe6fd50ba5fa9a34d8d38a53f50";
        int index = audioUrl.indexOf(".mp3");
        System.out.println(index);

    }

    public static void tt() {
        int begin = 4127120;
        int end = 4128280;

        double beginSec = (double) begin / 1000D;
        double endSec = (double) end / 1000D;
        int beginIndex = BigDecimal.valueOf(Math.floor(beginSec)).intValue();
        int endIndex =BigDecimal.valueOf(Math.floor(endSec)).intValue();
        System.out.println("beginSec:" + beginSec);
        System.out.println("endSec=" + endSec);
        System.out.println("=======");
        System.out.println(beginIndex);
        System.out.println(endIndex);
    }
}
