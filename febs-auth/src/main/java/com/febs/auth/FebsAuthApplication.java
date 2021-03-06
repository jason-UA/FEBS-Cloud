package com.febs.auth;

import com.febs.common.annotation.EnableFebsAuthExceptionHandler;
import com.febs.common.annotation.EnableFebsLettuceRedis;
import com.febs.common.annotation.FebsCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@FebsCloudApplication
@SpringBootApplication
@EnableFebsLettuceRedis
@EnableFebsAuthExceptionHandler
@MapperScan("com.febs.auth.mapper")
public class FebsAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(FebsAuthApplication.class, args);
    }
}