package com.smahjoub.stockute.adapters.persistence.membership;

import com.smahjoub.stockute.application.port.membership.out.UserInRolePort;
import com.smahjoub.stockute.domain.model.Role;
import com.smahjoub.stockute.domain.model.UserInRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
@Component
public class UserInRoleAdapter implements UserInRolePort {
    private final RoleRepository roleRepository;
    private final UserInRoleRepository userInRoleRepository;

    @Override
    public Mono<List<Role>> findRolesByUserName(String userName) {
        return roleRepository.getUserRoles(userName).collectList();
    }

    @Override
    public Mono<Role> findRoleByName(String name) {
        return roleRepository.findByName(name)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Role not found: " + name)));
    }

    @Override
    public Mono<Void> assignRoleToUser(long roleId, long userId) {
        UserInRole userInRole = new UserInRole(userId, roleId);
        return userInRoleRepository.save(userInRole).then();
    }
}
