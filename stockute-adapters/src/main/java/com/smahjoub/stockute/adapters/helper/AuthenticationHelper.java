package com.smahjoub.stockute.adapters.helper;

import lombok.NoArgsConstructor;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class AuthenticationHelper {
    public static Mono<String> getAuthenticatedUserName() {
        return ReactiveSecurityContextHolder.getContext().flatMap(c -> Mono.just(c.getAuthentication().getName()));
    }
}
