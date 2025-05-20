package com.mayu.practice.config;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.headers.Header;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.namespace.QName;
import java.util.List;
/**
 * @Author: 马瑜
 * @Date: 2024/7/22 10:55
 * @Description: webservice客户端 访问 服务端接口时，配置的拦截器
 * header中带上认证信息，只有通过认证的请求才能访问服务端
 */
public class ClientLoginInterceptor extends AbstractPhaseInterceptor<SoapMessage> {

    private final String username;
    private final String password;
    private static final String NAME_SPACE_URI = "http://webservice.service.operate.obu.iflytek.com";

    public ClientLoginInterceptor(String username, String password) {
        super(Phase.PREPARE_SEND);
        this.username = username;
        this.password = password;
    }

    @Override
    public void handleMessage(SoapMessage soapMessage) throws Fault {
        List<Header> headers = soapMessage.getHeaders();
        Document document = DOMUtils.createDocument();
        Element authority = document.createElementNS(NAME_SPACE_URI, "authority");
        Element username = document.createElementNS(NAME_SPACE_URI, "username");
        Element password = document.createElementNS(NAME_SPACE_URI, "password");
        username.setTextContent(this.username);
        password.setTextContent(this.password);
        authority.appendChild(username);
        authority.appendChild(password);
        headers.add(0, new Header(new QName("authority"), authority));
    }
}

