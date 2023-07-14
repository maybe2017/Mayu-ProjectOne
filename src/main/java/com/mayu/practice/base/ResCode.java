package com.mayu.practice.base;

/**
 * define the response code
 *
 * @author lazycece
 */
public interface ResCode {
    String SUCCESS = "0000";
    String PARAM_ERROR = "0001";// 请求参数错误(缺少必要字段、数据大小超过限制等)
    String INVALID_SIGN = "0002";// sign校验未通过
    String UPLOAD_SLICE_OFFSET_ERROR = "8002";
    String INVALID_TOKEN = "8003"; // Auth token fail, token is null
    String FAIL = "9999";// server inner error
}
