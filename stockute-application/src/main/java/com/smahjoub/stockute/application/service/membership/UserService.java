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
                .zipWith(this.userInRolePort.findRolesByUserName(email))
                .map(result -> {
                    final User user = result.getT1();
                    user.setRoles(new HashSet<>(result.getT2()));
                    return user;
                })
                .filter(user -> passwordEncoder.encode(password).equals(user.getPassword()))
                .onErrorResume(error -> {
                    log.error("Authentication failed for %{} with the following details {}", email, error.getMessage());
                    return Mono.empty();
                });
    }

}
