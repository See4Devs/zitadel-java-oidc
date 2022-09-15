package com.example.client.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenAccessor {

    private final OAuth2AuthorizedClientService oauth2ClientService;

    public OAuth2AccessToken retrieveAccessToken() {
        return getAccessToken(SecurityContextHolder.getContext().getAuthentication());
    }

    public OAuth2AccessToken getAccessToken(Authentication clientAuth) {

        var authToken = (OAuth2AuthenticationToken) clientAuth;
        var clientId = authToken.getAuthorizedClientRegistrationId();
        var username = clientAuth.getName();
        var authorizedClient = oauth2ClientService.loadAuthorizedClient(clientId, username);

        if (authorizedClient == null) {
            return null;
        }else{
            var clientAccessToken = authorizedClient.getAccessToken();
            return clientAccessToken;
        }

    }
}
