package com.example.resourceserver.api;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
class PingController {

    @GetMapping("/api/ping/me")
    Object pingMe(Authentication auth) {
        var tokenDetails = ((BearerTokenAuthentication) auth).getTokenAttributes();
        var pingEcho = "Hello, " + tokenDetails.get(StandardClaimNames.PREFERRED_USERNAME) + " Ping successful.";
        return Map.of("ping_echo", pingEcho);
    }
}
