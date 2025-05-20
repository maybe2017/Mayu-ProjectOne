package com.mayu.practice.config;

import com.mayu.practice.service.Login4AService;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.xml.ws.Endpoint;

/**
 * @Author: 马瑜
 * @Date: 2024/7/18 15:47
 * @Description: webservice
 */
@Configuration
@Slf4j
public class WebServiceConfig {

    private Integer serverPort = 9090;
    @Resource
    private Login4AService login4AService;

    // 创建一个SpringBus Bean，作为Apache CXF的默认总线
    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    // 注册CXF Servlet，用于处理WebService请求
    @Bean(name = "cxfServlet")
    public ServletRegistrationBean<CXFServlet> cxfServlet() {
        // 创建一个ServletRegistrationBean，将CXFServlet映射到指定路径
        return new ServletRegistrationBean<>(new CXFServlet(), "/prodwebservice/*");
    }

    // 定义WebService端点
    @Bean
    public Endpoint endpoint() {
        // 创建EndpointImpl对象，并将SpringBus和WebService实现类传入
        org.apache.cxf.jaxws.EndpointImpl endpoint = new EndpointImpl(springBus(), login4AService);
        // 将端点发布到指定路径
        endpoint.publish("/login4AService");
        // 打印发布成功消息，显示服务的访问地址
        log.info("webservice服务发布成功！地址为：http://localhost:{}/prodwebservice/login4AService", 8888);
        // 返回端点对象
        return endpoint;
    }
}