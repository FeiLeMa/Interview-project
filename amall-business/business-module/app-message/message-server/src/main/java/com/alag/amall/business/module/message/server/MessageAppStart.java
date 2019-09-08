package com.alag.amall.business.module.message.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
@EnableScheduling
@EnableFeignClients(basePackages = "com.alag.amall.business.module.pay.feign")
public class MessageAppStart {
    public static void main(String[] args) {
        SpringApplication.run(MessageAppStart.class, args);
    }
}
