package com.smahjoub.stockute.adapters.common;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        final SecurityContext context = SecurityContextHolder.createEmptyContext();
        String username = annotation.username();
        User principal = new User(username, "", getAuthorities(annotation.roles()));

        Authentication auth = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities()
        );
        context.setAuthentication(auth);
        return context;
    }

    private Collection<GrantedAuthority> getAuthorities(String[] roles) {
        return Stream.of(roles)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
