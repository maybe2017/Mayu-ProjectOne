package com.mayu.practice.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: 马瑜
 * @Date: 2023/2/24 10:14
 * @Description:
 */
@Document("mayuTest")
@Data
public class MayuTest {

    @Id
    private String id;

    private String taskId;

    private String status;

    private Params params;

    private Long seqNo;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createTime;

    // jackson的注解@JsonFormat 一般java8的时间需要配合引入 jsr310的jar才能使序列化成想要的格式
    // 但是我们已经手动在JacksonUtil类中进行了对java8时间模块JavaTimeModule的引用 所以不用额外引入了
    // fastjson 要用@JSONField注解
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime updateTime;

}
