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
    public Mono<User> findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
