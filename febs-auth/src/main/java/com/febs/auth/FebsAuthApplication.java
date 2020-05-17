package com.febs.auth;

import com.febs.common.annotation.EnableFebsAuthExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFebsAuthExceptionHandler
public class FebsAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(FebsAuthApplication.class, args);
    }
}