package com.mayu.qw.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "conversationRecord_history_202409")
public class ConversationRecord implements Comparable<ConversationRecord>{
    /**
     * 地市
     */
    private String cityId;
    /**
     * 地市名称
     */
    private String cityName;
    /**
     * 微管家ID
     */
    private String microManagerId;
    /**
     * 客户ID（微信号）
     */
    private String customerId;
    /**
     * 消息编号
     */
    private String msgId;
    /**
     * 消息序列
     */
    private String msgSeq;
    /**
     * 消息动作(send(发送消息)/recall(撤回消息)/switch(切换企业日志))
     */
    private String msgActive;
    /**
     * 群消息ID
     */
    private String roomid;
    /**
     * 发送方
     */
    private String sendUser;
    /**
     * 发送方类型（(0:微管家 1:客户)）
     */
    private String sendUserType;
    /**
     * 接受方
     */
    private String acceptUser;
    /**
     * 消息时间
     */
    private String msgTime;
    /**
     * 消息类型
     */
    private String msgType;
    /**
     * 消息内容
     */
    private String msgContent;
    /**
     * 加密消息内容
     */
    private String msgContentDespwd;
    /**
     * 入库时间
     */
    private String createTime;
    /**
     * 媒体链接
     */
    private String mediaLink;
    /**
     * 备注1
     */
    private String ext1;
    /**
     * 备注2
     */
    private String ext2;
    /**
     * 备注3
     */
    private String ext3;
    /**
     * 会话ID
     */
    private String conversationId;

    /**
     * 导入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date importDate;
    /**
     * 导入者
     */
    private String importUser;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;
    /**
     * 更新者
     */
    private String updateUser;
    /**
     * 情绪 EmotionTypeEnum
     */
    private String msgEmotion;

    /**
     * 情绪(系统)
     */
    private String msgOriginalEmotion;

    /**
     * 地市信息更新标志
     */
    private String cityInfoFlg;

    // 如果消息是voice类型，该字段标识voice是否已经转写文本完成？
    // 在引擎转写后的回调方法里面 更新该字段值为true
//    private boolean contentTransFlag;

    @Override
    public int compareTo(ConversationRecord param) {
        if(this.msgTime.compareTo(param.msgTime) > 0){
            return 1;
        } else if (this.msgTime.compareTo(param.msgTime) < 0){
            return -1;
        } else {
            return 0;
        }
    }
}


