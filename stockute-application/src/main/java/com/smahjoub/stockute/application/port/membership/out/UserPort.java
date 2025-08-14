package com.smahjoub.stockute.application.port.membership.out;

import com.smahjoub.stockute.domain.model.User;
import reactor.core.publisher.Mono;

public interface UserPort {

    Mono<User> findByEmail(String email);

    Mono<User> findByUsername(String username);
}
