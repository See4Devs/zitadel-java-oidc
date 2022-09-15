package com.example.client.helper.zitadel;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;

import java.util.Collection;
import java.util.HashSet;

public class ZitadelGrantedAuthoritiesMapper implements GrantedAuthoritiesMapper {

    public static final String ZITADEL_ROLES_CLAIM = "urn:zitadel:iam:org:project:roles";

    @Override
    public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> grantedAuthorities) {
        var mappedAuthorities = new HashSet<GrantedAuthority>();

        grantedAuthorities.forEach(authority -> {
            
            if (authority instanceof SimpleGrantedAuthority) {//standard scopes
                mappedAuthorities.add(authority);

            }else if (authority instanceof OidcUserAuthority) {//reserved scopes
                mapFromUserInfo(mappedAuthorities, (OidcUserAuthority) authority);
            }

        });

        return mappedAuthorities;
    }

    private void mapFromUserInfo(HashSet<GrantedAuthority> mappedAuthorities, OidcUserAuthority oidcUserAuthority) {

        var userInfo = oidcUserAuthority.getUserInfo();

        var roleInfo = userInfo.getClaimAsMap(ZITADEL_ROLES_CLAIM);
        if (roleInfo == null || roleInfo.isEmpty()) {
            return;
        }

        roleInfo.keySet().forEach(zitadelRole -> {
            mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + zitadelRole));
        });
    }

}
