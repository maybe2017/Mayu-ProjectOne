package com.mayu.practice.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * @Author: 马瑜
 * @Date: 2024/7/18 15:40
 * @Description: 4A管控平台，要访问本服务接口（同步账号数据）
 */
@WebService
public interface Login4AService {

    /**
     * 查询所有用户数据(同步给4A平台)
     */
    @WebMethod
    String queryUsers();
}
