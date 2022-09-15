package com.example.client.helper.zitadel;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ZitadelLogoutHandler implements LogoutHandler {

    public static final String END_SESSION_ENDPOINT = "<instance url>/oidc/v1/end_session";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {

        var principalUser = (DefaultOidcUser) auth.getPrincipal();
        var idToken = principalUser.getIdToken();

        var idTokenValue = idToken.getTokenValue();
        var redirectUri = generateUriFromRequest(request);
        var logoutUrl = createLogoutUrl(idTokenValue, redirectUri);

        try {
            //and then redirect to this URL after logging out
            response.sendRedirect(logoutUrl);
        } catch (IOException e) {
            //create your exception handling logic here
        }
    }

    private String generateUriFromRequest(HttpServletRequest request) {
        var hostname = request.getServerName() + ":" + request.getServerPort();
        var isStandardHttps = "https".equals(request.getScheme()) && request.getServerPort() == 443;
        var isStandardHttp = "http".equals(request.getScheme()) && request.getServerPort() == 80;
        if (isStandardHttps || isStandardHttp) {
            hostname = request.getServerName();
        }
        return request.getScheme() + "://" + hostname + request.getContextPath();
    }

    private String createLogoutUrl(String idTokenValue, String postRedirectUri) {
        return END_SESSION_ENDPOINT + //
                "?id_token_hint=" + idTokenValue //
                + "&post_logout_redirect_uri=" + postRedirectUri;
    }
}
