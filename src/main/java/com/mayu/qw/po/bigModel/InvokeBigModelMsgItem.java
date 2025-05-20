package com.mayu.qw.po.bigModel;

import lombok.Data;

@Data
public class InvokeBigModelMsgItem {
    // system用于设置对话背景，user表示是用户的问题，assistant表示AI的回复
    private String role;
    private String content;
}
