package com.smahjoub.stockute.application.service.membership;

import com.smahjoub.stockute.application.port.membership.in.UserUseCase;
import com.smahjoub.stockute.application.port.membership.out.UserInRolePort;
import com.smahjoub.stockute.application.port.membership.out.UserPort;
import com.smahjoub.stockute.application.service.membership.utils.PBKDF2Encoder;
import com.smahjoub.stockute.domain.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashSet;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserUseCase {

    private final UserPort userPort;
    private final UserInRolePort userInRolePort;
    private final PBKDF2Encoder passwordEncoder;

    @Override
    public Mono<User> authenticate(String email, String password) {
        return userPort.findByEmail(email)
                .zipWith(userPort.findByEmail(email).flatMap(user -> this.userInRolePort.findRolesByUserName(user.getUsername())))
                .map(result -> {
                    final User user = result.getT1();
                    user.setRoles(new HashSet<>(result.getT2()));
                    return user;
                })
                .filter(user -> passwordEncoder.encode(password).equals(user.getPassword()))
                .onErrorResume(error -> {
                    log.error("Authentication failed for {} with the following details {}", email, error.getMessage());
                    return Mono.empty();
                });
    }

    @Override
    public Mono<User> getUserByUsername(String userName) {
        return userPort.findByUsername(userName);
    }

    @Override
    public Mono<User> updateProfile(String username, String email, String firstName, String lastName) {
        return userPort.findByUsername(username)
                .flatMap(user -> {
                    user.setEmail(email);
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    return userPort.save(user);
                });
    }

    @Override
    public Mono<Void> changePassword(String username, String currentPassword, String newPassword) {
        return userPort.findByUsername(username)
                .flatMap(user -> {
                    if (!passwordEncoder.encode(currentPassword).equals(user.getPassword())) {
                        return Mono.error(new IllegalArgumentException("Current password is incorrect"));
                    }
                    user.setPassword(passwordEncoder.encode(newPassword));
                    return userPort.save(user);
                })
                .then();
    }

    @Override
    public Mono<User> createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        return userPort.save(user)
                .flatMap(savedUser -> userInRolePort.findRoleByName("USER")
                        .flatMap(role -> userInRolePort.assignRoleToUser(role.getId(), savedUser.getId())
                                .then(userInRolePort.findRolesByUserName(savedUser.getUsername()))
                                .doOnSuccess(roles -> savedUser.setRoles(new HashSet<>(roles)))
                                .thenReturn(savedUser)));
    }

}

