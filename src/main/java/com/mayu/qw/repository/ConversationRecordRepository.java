package com.mayu.qw.repository;

import com.mayu.qw.po.ConversationRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Slf4j
@Repository
public class ConversationRecordRepository extends BaseRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询管家发送的消息
     *
     * @param startTime 消息发送时间区间
     * @param endTime   消息发送时间区间
     * @return 有限字段的数据
     */
    public List<ConversationRecord> listMicroConversationRecordByMsgTime(String startTime, String endTime) {
        Query query = new Query();
        query.addCriteria(Criteria.where("msgTime").gte(startTime).lte(endTime));
        query.addCriteria(Criteria.where("sendUserType").is("0"));
        query.fields()
                .include("msgId")
                .include("msgContent")
                .include("msgTime")
                .include("sendUser")
                .include("acceptUser")
                .include("conversationId");
        return mongoTemplate.find(query, ConversationRecord.class);
    }

    public List<ConversationRecord> findWithoutConversationId() {
        Query query = new Query();
        query.addCriteria(Criteria.where("conversationId").is(""));
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("microManagerId").exists(false), Criteria.where("microManagerId").is(null));
        query.addCriteria(criteria);
        query.with(Sort.by(new Sort.Order(Sort.Direction.ASC, "msgTime"))
                .and(Sort.by(new Sort.Order(Sort.Direction.ASC, "msgSeq")))
        );
        query.limit(10000);
        return mongoTemplate.find(query, ConversationRecord.class);
    }
}
