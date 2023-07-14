package com.mayu.practice.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

@Configuration
@Slf4j
public class ElasticSearchConfig {

    @Value("${elasticsearch.ip.restHighLevelClient}")
    private String restHighLevelClientIP;


    @Value("${elasticsearch.port.restHighLevelClient}")
    private int restHighLevelClientPort;

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(restHighLevelClientIP, restHighLevelClientPort, "http")
                ));
    }

//    @Bean
//    public ElasticsearchClient elasticsearchClient() {
//        // 如果你的集群名称不是默认的elasticsearch，需要以下这步
//        Settings settings = Settings.builder().put("cluster.name", "elasticsearch")
//                .put("client.transport.ignore_cluster_name", true)
//                .build();
//        ElasticsearchClient client = null;
//        try {
//            client = new PreBuiltTransportClient(settings)
//                    .addTransportAddress(new TransportAddress(InetAddress.getByName(restHighLevelClientIP), 9300));
//        } catch (Exception e) {
//            log.error("创建ElasticsearchClient实例失败! e: {}", e.getMessage(), e);
//        } finally {
//            if (client != null) {
//                log.info("创建ElasticsearchClient连接实例成功！");
//            }
//        }
//        return client;
//    }

}
