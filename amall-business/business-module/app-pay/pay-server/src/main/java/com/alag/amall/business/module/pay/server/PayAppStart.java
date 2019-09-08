package com.alag.amall.business.module.pay.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = {
        "com.alag.amall.business.module.message.feign.fallback",
        "com.alag.amall.business.module.order.feign.fallback",
        "com.alag.amall.business.module.pay.server"})
@EnableEurekaClient
@EnableTransactionManagement
@EnableFeignClients(basePackages = {
        "com.alag.amall.business.module.order.feign.controller",
        "com.alag.amall.business.module.message.feign.controller"})
public class PayAppStart {
    public static void main(String[] args) {
        SpringApplication.run(PayAppStart.class, args);
    }
}
