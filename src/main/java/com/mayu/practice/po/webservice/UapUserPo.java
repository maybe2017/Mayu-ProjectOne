package com.mayu.practice.po.webservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  UAP机构信息
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UapUserPo {

    private String id;
    private String loginName;
    private String name;
    private String passowrd;
    private Integer status;
    private String phone;
}
