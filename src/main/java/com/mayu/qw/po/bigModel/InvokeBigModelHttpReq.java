package com.mayu.qw.po.bigModel;

import lombok.Data;

import java.util.List;

@Data
public class InvokeBigModelHttpReq {

    private String model;
    private Float temperature;
    private Integer max_tokens;
    private Integer top_k;
    private List<InvokeBigModelMsgItem> messages;
}

