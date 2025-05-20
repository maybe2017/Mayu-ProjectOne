package com.mayu.practice.po.statistics;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

// 河北没有安装预约
@Getter
public enum AccessSysType {

    /**
     * 工作号平台
     */
    WORK_PLAT("1", "工作号平台"),
    /**
     * 装维APP装机
     */
    APP_INSTALL("2", "装维APP装机"),
    /**
     * 装维APP投诉
     */
    APP_COMPLAINT("3", "装维APP投诉"),

    // 集客
    JIKE_PLAT("5", "集客业务");

    private final String value;
    private final String desc;

    AccessSysType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static final List<String> ALL_FTP_ACCESS_CODES = Arrays.asList(APP_INSTALL.getValue(), APP_COMPLAINT.getValue(), JIKE_PLAT.getValue());

}
