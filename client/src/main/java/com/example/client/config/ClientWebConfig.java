package com.example.client.config;

import com.example.client.helper.AccessTokenInterceptor;
import com.example.client.helper.TokenAccessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
class ClientWebConfig {

    private final TokenAccessor tokenAccessor;

    @Bean
    @Qualifier("zitadel")
    RestTemplate restTemplate() {
        return new RestTemplateBuilder() 
                .interceptors(new AccessTokenInterceptor(tokenAccessor))
                .build();
    }
}
