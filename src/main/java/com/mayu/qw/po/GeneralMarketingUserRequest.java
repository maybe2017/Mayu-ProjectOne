package com.mayu.qw.po;

import lombok.Data;
@Data
public class GeneralMarketingUserRequest {

    /**
     * 微管家userid
     */
    private String userId;

    /**
     * 目标客户量（亚信侧同步机制：目标量变化时，进行同步）
     */
    private String personCont;
}
