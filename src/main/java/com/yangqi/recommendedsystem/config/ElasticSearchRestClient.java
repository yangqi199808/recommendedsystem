package com.yangqi.recommendedsystem.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaoer
 * @date 2020/2/29 22:45
 */
@Configuration
public class ElasticSearchRestClient {
    @Value("${elasticsearch.ip}")
    String ipAddress;

    @Bean(name = "highLevelClient")
    public RestHighLevelClient highLevelClient() {
        String[] address = ipAddress.split(":");
        String ip = address[0];
        Integer port = Integer.valueOf(address[1]);
        HttpHost httpHost = new HttpHost(ip, port, "http");
        return new RestHighLevelClient(RestClient.builder(httpHost));
    }
}
