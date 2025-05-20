package com.mayu.practice.service.impl;

import com.alibaba.fastjson.JSON;
import com.mayu.practice.config.ClientLoginInterceptor;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: 马瑜
 * @Date: 2024/7/22 11:03
 * @Description: 模拟webservice客户端 访问服务端 `账号数据同步接口`
 */
@Component
public class UserInfoApiClient {

    private static final String USER_NAME = "iflytek";
    private static final String USER_PASSWORD = "iflytek.com";
    private static final String ADDRESS = "http://localhost:9081/sqt-manage/prodwebservice/login4AService?wsdl";
//    private static final String ADDRESS = "http://localhost:8888/prodwebservice/login4AService?wsdl";

    /**
     * 使用动态代理
     */
    public String queryUsersWithDynamic() throws Exception {
        JaxWsDynamicClientFactory clientFactory = JaxWsDynamicClientFactory.newInstance();
        Client client = clientFactory.createClient(ADDRESS);
        client.getOutInterceptors().add(new ClientLoginInterceptor(USER_NAME, USER_PASSWORD));
        Object[] userInfos = client.invoke("queryUsers");
        return JSON.toJSONString(userInfos[0]);

    }
}

