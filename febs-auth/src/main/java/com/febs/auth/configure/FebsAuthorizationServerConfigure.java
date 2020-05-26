package com.febs.auth.configure;


import com.febs.auth.properties.FebsAuthProperties;
import com.febs.auth.properties.FebsClientsProperties;
import com.febs.auth.service.FebsUserDetailService;
import com.febs.auth.translator.FebsWebResponseExceptionTranslator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

@Configuration
@EnableAuthorizationServer
public class FebsAuthorizationServerConfigure extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private FebsUserDetailService userDetailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private FebsAuthProperties authProperties;
    @Autowired
    private FebsWebResponseExceptionTranslator exceptionTranslator;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        FebsClientsProperties[] clientsProperties = authProperties.getClients();
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
        if (ArrayUtils.isNotEmpty(clientsProperties)) {
            for (FebsClientsProperties properties : clientsProperties) {
                if (StringUtils.isBlank(properties.getClient())) {
                    throw new Exception("client不能为空");
                }
                if (StringUtils.isBlank(properties.getSecret())) {
                    throw new Exception("secret不能为空");
                }
                String[] grantTypes = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getGrantType(), ",");
                builder.withClient(properties.getClient())
                        .secret(passwordEncoder.encode(properties.getSecret()))
                        .authorizedGrantTypes(grantTypes)
                        .scopes(properties.getScope());
            }

        }
    }

    @Override
    @SuppressWarnings("all")
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
                .accessTokenConverter(jwtAccessTokenConverter())
                .userDetailsService(userDetailService)
                .authenticationManager(authenticationManager)
                .exceptionTranslator(exceptionTranslator);
    }

    @Bean TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        DefaultAccessTokenConverter defaultAccessTokenConverter = (DefaultAccessTokenConverter)
                jwtAccessTokenConverter.getAccessTokenConverter();
        DefaultUserAuthenticationConverter userAuthenticationConverter = new DefaultUserAuthenticationConverter();
        userAuthenticationConverter.setUserDetailsService(userDetailService);
        defaultAccessTokenConverter.setUserTokenConverter(userAuthenticationConverter);
        jwtAccessTokenConverter.setSigningKey("febs");
        return jwtAccessTokenConverter;
    }
}
