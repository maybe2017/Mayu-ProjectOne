package com.mayu.qw.repository;

import com.mayu.qw.po.GeneralMarketingTaskInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Slf4j
@Repository
public class GeneralMarketingTaskInfoRepository {

    @Resource
    private MongoTemplate mongoTemplate;

    public GeneralMarketingTaskInfo findByTaskId(String taskId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("taskId").is(taskId));
        return mongoTemplate.findOne(query, GeneralMarketingTaskInfo.class);
    }
}
