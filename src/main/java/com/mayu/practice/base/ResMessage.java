package com.mayu.practice.base;

/**
 * define the response message
 *
 * @author lazycece
 */
public interface ResMessage {
    String SUCCESS = "成功";
    String PARAM_ERROR = "请求参数错误";
    String INVALID_SIGN = "sign校验未通过";
    String FAIL = "其他";
}
