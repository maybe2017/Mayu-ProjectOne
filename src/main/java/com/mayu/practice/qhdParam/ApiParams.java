package com.mayu.practice.qhdParam;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: 马瑜
 * @Date: 2023/3/3 16:49
 * @Description:
 */
public class ApiParams implements Validator {
    private Long time;
    private String salt;
    /**
     * params signature
     */
    private String sign;
    /**
     * encode data, maybe null
     */
    private String data;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean validate() {
        return this.time != null
                && !StringUtils.isBlank(this.salt)
                && !StringUtils.isBlank(this.sign);
    }
}
