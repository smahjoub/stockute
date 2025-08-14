package com.smahjoub.stockute.adapters.persistence.membership;

import com.smahjoub.stockute.application.port.membership.out.UserPort;
import com.smahjoub.stockute.domain.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Component
public class UserAdapter implements UserPort {
    private final UserRepository userRepository;
    @Override
    public Mono<User> findByEmail(final String email) {
        return userRepository.findUserByEmail(email)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found for email: " + email)));
    }

    @Override
    public Mono<User> findByUsername(final String username) {
        return userRepository.findUserByUsername(username)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("User not found for username: " + username)));
    }
}
