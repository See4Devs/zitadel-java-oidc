package com.example.client.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@RequiredArgsConstructor
public class AccessTokenInterceptor implements ClientHttpRequestInterceptor {

    private final TokenAccessor tokenAccessor;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        var grantedAccessToken = tokenAccessor.retrieveAccessToken();

        //adding retrieved access token to Bearer Auth header
        if (grantedAccessToken != null) {
            request.getHeaders().add(HttpHeaders.AUTHORIZATION, "Bearer " + grantedAccessToken.getTokenValue());
        }

        return execution.execute(request, body);
    }
}
