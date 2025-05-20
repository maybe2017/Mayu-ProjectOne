package com.mayu.practice.service.impl;

import com.mayu.practice.po.webservice.UapUserPo;
import com.mayu.practice.service.Login4AService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 马瑜
 * @Date: 2024/7/18 15:43
 * @Description: 同步账号数据
 */
@Slf4j
@Service
@WebService(targetNamespace = "http://service.practice.mayu.com/", endpointInterface = "com.mayu.practice.service.Login4AService")
public class Login4AServiceImpl implements Login4AService {

    @Value("${sync4AAccounts:admin4A-admin4A;}")
    private String sync4AAccounts;

    @Override
    public String queryUsers() {
        log.info("4A管控平台开始同步从账号数据!");
        List<UapUserPo> userPoList = new ArrayList<>();
        if (StringUtils.isNotBlank(sync4AAccounts)) {
            String[] allAccounts = sync4AAccounts.split(";");
            for (String account : allAccounts) {
                String[] accountInfo = account.split("-");
                UapUserPo userPo = new UapUserPo();
                userPo.setLoginName(accountInfo[0]);
                userPo.setName(accountInfo[1]);
                userPoList.add(userPo);
            }
        }
        return getAccountXmlStr(userPoList);
    }

    private String getAccountXmlStr(List<UapUserPo> userPoList) {
        String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        return xmlHeader + "<accounts>\n" + getAccountJointStr(userPoList) + "</accounts>";
    }

    private String getAccountJointStr(List<UapUserPo> userPoList) {
        StringBuilder allAccountXmlStr = new StringBuilder();
        for (UapUserPo userPo : userPoList) {
            String account = userPo.getLoginName();
            String name = userPo.getName();
            allAccountXmlStr
                    .append("\t<account>\n")
                    .append("\t\t<accId>")
                    .append(account)
                    .append("</accId>\n")
                    .append("\t\t<name>")
                    .append(name)
                    .append("</name>\n")
                    .append("</account>\n");
        }
        return allAccountXmlStr.toString();
    }
}
