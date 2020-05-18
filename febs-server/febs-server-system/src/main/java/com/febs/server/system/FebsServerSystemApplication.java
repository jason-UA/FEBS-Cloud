package com.febs.server.system;

import com.febs.common.annotation.EnableFebsAuthExceptionHandler;
import com.febs.common.annotation.EnableFebsServerProtect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFebsAuthExceptionHandler
@EnableFebsServerProtect
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class FebsServerSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FebsServerSystemApplication.class, args);
    }
}