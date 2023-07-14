package com.mayu.practice.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

}
