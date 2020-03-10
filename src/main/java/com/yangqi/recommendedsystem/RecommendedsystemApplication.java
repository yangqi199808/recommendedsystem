package com.yangqi.recommendedsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 如果没有指定则默认扫描所有
 */
@SpringBootApplication(scanBasePackages = {"com.yangqi.recommendedsystem"})
@MapperScan("com.yangqi.recommendedsystem.dal")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
public class RecommendedsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecommendedsystemApplication.class, args);
    }

}

