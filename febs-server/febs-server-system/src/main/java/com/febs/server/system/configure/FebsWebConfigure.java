package com.febs.server.system.configure;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.febs.server.system.properties.FebsServerSystemProperties;
import com.febs.server.system.properties.FebsSwaggerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class FebsWebConfigure {

    @Autowired
    private FebsServerSystemProperties properties;

    @Bean
    public Docket swaggerApi() {
        FebsSwaggerProperties swaggerProperties = properties.getSwagger();
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.febs.server.system.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo(swaggerProperties))
                .securitySchemes(Collections.singletonList(securityScheme(swaggerProperties)))
                .securityContexts(Collections.singletonList(securityContext(swaggerProperties)));
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        List<ISqlParser> sqlParserList = new ArrayList<>();
        sqlParserList.add(new BlockAttackSqlParser());
        paginationInterceptor.setSqlParserList(sqlParserList);
        return paginationInterceptor;
    }

    private ApiInfo apiInfo(FebsSwaggerProperties properties) {
        return new ApiInfo(
                properties.getTitle(),
                properties.getDescription(),
                properties.getVersion(),
                null,
                new Contact(properties.getAuthor(), properties.getUrl(), properties.getEmail()),
                properties.getLicense(), properties.getLicenseUrl(), Collections.emptyList());
    }

    private SecurityScheme securityScheme(FebsSwaggerProperties properties) {
        GrantType grantType = new ResourceOwnerPasswordCredentialsGrant("http://localhost:8301/auth/oauth/token");

        return new OAuthBuilder()
                .name(properties.getName())
                .grantTypes(Collections.singletonList(grantType))
                .scopes(Arrays.asList(scopes(properties)))
                .build();
    }

    private SecurityContext securityContext(FebsSwaggerProperties properties) {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference(properties.getName(), scopes(properties))))
                .forPaths(PathSelectors.any())
                .build();
    }

    private AuthorizationScope[] scopes(FebsSwaggerProperties properties) {
        return new AuthorizationScope[]{
                new AuthorizationScope(properties.getScope(), "")
        };
    }
}
