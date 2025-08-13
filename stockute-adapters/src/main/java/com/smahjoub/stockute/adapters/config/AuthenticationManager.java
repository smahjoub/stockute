package com.smahjoub.stockute.adapters.config;

import com.smahjoub.stockute.adapters.exception.UnauthorizedAccessException;
import com.smahjoub.stockute.adapters.persistence.membership.UserRepository;
import io.vavr.control.Try;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Slf4j
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTUtil jwtUtil;

    private final UserRepository userRepository;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication.getCredentials().toString())
                .flatMap(authToken -> {
                    final var isValidToken = Try.of(() -> jwtUtil.validateToken(authToken)).getOrElse(false);
                    if (isValidToken) {
                        final var username = jwtUtil.getUsernameFromToken(authToken);
                        return userRepository.findUserByUsername(username).flatMap(user -> {
                            if (user.isEnabled()) {
                                return userRepository
                                        .getUserRoles(username)
                                        .collectList()
                                        .map(roles -> {
                                            final var claims = jwtUtil.getAllClaimsFromToken(authToken);
                                            return new UsernamePasswordAuthenticationToken(
                                                    username,
                                                    claims,
                                                    roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                                        });
                            } else {
                                return Mono.error(new UnauthorizedAccessException("User account is not active."));
                            }

                        });
                    } else {
                        return Mono.error(new UnauthorizedAccessException("Failed to validated jwt token."));
                    }
                });
    }


}
