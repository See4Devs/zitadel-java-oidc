package com.example.client.config;

import com.example.client.helper.zitadel.ZitadelGrantedAuthoritiesMapper;
import com.example.client.helper.zitadel.ZitadelLogoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
class ClientSecurityConfig {

    private final ZitadelLogoutHandler zitadelLogoutHandler;

    @Bean
    public SecurityFilterChain securityChain(HttpSecurity httpSecurity, ClientRegistrationRepository clientRegistrationRepository) throws Exception {

        httpSecurity.authorizeRequests(it -> {
            it.antMatchers("/webjars/**", "/resources/**", "/css/**").permitAll();
            it.anyRequest().fullyAuthenticated();
        });

        httpSecurity.oauth2Client(it -> {
            var oauth2AuthRequestResolver = new DefaultOAuth2AuthorizationRequestResolver( 
                    clientRegistrationRepository,
                    OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI //
            );

            it.authorizationCodeGrant().authorizationRequestResolver(oauth2AuthRequestResolver);
        });

        httpSecurity.oauth2Login(it -> {
            it.userInfoEndpoint().userAuthoritiesMapper(zitadelMapper());
        });
        httpSecurity.logout(it -> {
            it.addLogoutHandler(zitadelLogoutHandler);
        });

        return httpSecurity.build();
    }

    private GrantedAuthoritiesMapper zitadelMapper() {
        return new ZitadelGrantedAuthoritiesMapper();
    }
}