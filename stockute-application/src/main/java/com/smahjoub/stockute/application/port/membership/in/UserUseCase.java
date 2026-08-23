package com.smahjoub.stockute.application.port.membership.in;

import com.smahjoub.stockute.domain.model.User;
import reactor.core.publisher.Mono;

public interface UserUseCase {

    Mono<User> authenticate(String email, String password);

    Mono<User> getUserByUsername(String userName);

    Mono<User> updateProfile(String username, String email, String firstName, String lastName);

    Mono<Void> changePassword(String username, String currentPassword, String newPassword);
}
