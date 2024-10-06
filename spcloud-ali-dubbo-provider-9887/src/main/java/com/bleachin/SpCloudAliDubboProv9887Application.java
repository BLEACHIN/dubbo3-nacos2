package com.bleachin;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
// dubbo3需要使用nacos 2.0以上的版本
public class SpCloudAliDubboProv9887Application {
    public static void main(String[] args) {
        SpringApplication.run(SpCloudAliDubboProv9887Application.class, args);
    }
}
