package com.febs.auth;

import com.febs.common.annotation.EnableFebsAuthExceptionHandler;
import com.febs.common.annotation.EnableFebsServerProtect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@EnableFebsServerProtect
@SpringBootApplication
@EnableFebsAuthExceptionHandler
public class FebsAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(FebsAuthApplication.class, args);
    }
}