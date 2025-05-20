package com.mayu.practice.dto;

import lombok.Data;

/**
 * @Author: 马瑜
 * @Date: 2024/7/9 19:02
 * @Description: 投诉处理结果解析
 */
@Data
public class ComplaintDetailsParseRes {

    // 微管家是否有处理投诉 MircIfComplaintEnum
    private String mircIfComplaint;

    // 投诉处理满意度 ComplaintSatisfactionEnum
    private String complaintSatisfaction;

    // 投诉处理不满意原因
    private String complaintDissatisfactionCause;

}
