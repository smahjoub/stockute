package com.smahjoub.stockute.adapters.persistence.membership;

import com.smahjoub.stockute.application.port.membership.out.UserInRolePort;
import com.smahjoub.stockute.domain.model.Role;
import com.smahjoub.stockute.domain.model.UserInRole;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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
        LocalDateTime now = LocalDateTime.now();
        userInRole.setCreatedDate(now);
        userInRole.setLastModifiedDate(now);
        userInRole.setVersion(0L);
        
        log.info("Saving UserInRole: userId={}, roleId={}, createdDate={}, lastModifiedDate={}, version={}", 
                userId, roleId, userInRole.getCreatedDate(), userInRole.getLastModifiedDate(), userInRole.getVersion());
        
        return userInRoleRepository.save(userInRole)
                .doOnSuccess(saved -> log.info("Saved UserInRole: id={}, createdDate={}, lastModifiedDate={}, version={}", 
                        saved.getId(), saved.getCreatedDate(), saved.getLastModifiedDate(), saved.getVersion()))
                .then();
    }
}
