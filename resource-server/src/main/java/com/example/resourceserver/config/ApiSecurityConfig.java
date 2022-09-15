package com.example.resourceserver.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
class ApiSecurityConfig {

    @Bean
    public SecurityFilterChain securityChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable();

        httpSecurity.sessionManagement(it -> {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        });

        httpSecurity.authorizeRequests(it -> {
            // declarative route configuration
            // .mvcMatchers("/api").hasAuthority("ROLE_ACCESS")
            it.mvcMatchers("/api/**").authenticated();
            // add additional routes
            it.anyRequest().authenticated(); //
        });
        httpSecurity.oauth2ResourceServer().opaqueToken();

        return httpSecurity.build();
    }

}