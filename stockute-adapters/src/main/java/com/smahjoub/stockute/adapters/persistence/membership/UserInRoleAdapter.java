package com.smahjoub.stockute.adapters.persistence.membership;

import com.smahjoub.stockute.application.port.membership.out.UserInRolePort;
import com.smahjoub.stockute.domain.model.Role;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Component
public class UserInRoleAdapter implements UserInRolePort {
    private final RoleRepository roleRepository;

    @Override
    public Mono<List<Role>> findRolesByUserName(String userName) {
        return roleRepository.getUserRoles(userName).collectList();
    }
}
