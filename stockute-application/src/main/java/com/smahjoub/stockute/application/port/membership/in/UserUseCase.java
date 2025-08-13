package com.smahjoub.stockute.application.port.membership.in;

import com.smahjoub.stockute.domain.model.User;
import reactor.core.publisher.Mono;

public interface UserUseCase {

    Mono<User> authenticate(String email, String password);
}
