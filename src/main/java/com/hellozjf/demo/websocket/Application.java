package com.hellozjf.demo.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

// SpringBoot主入口
@Configuration
@ComponentScan(basePackages = {"com.hellozjf.demo.websocket"})
@EnableAutoConfiguration
//@SpringBootApplication
// 使能任务计划
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}