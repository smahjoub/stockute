package com.smahjoub.stockute.adapters.persistence.membership;

import com.smahjoub.stockute.domain.model.UserInRole;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserInRoleRepository extends ReactiveCrudRepository<UserInRole, Long> {

    @Query("INSERT INTO users_in_roles(role_id, user_id) VALUES(:roleId, :userId) " +
            "ON CONFLICT (user_id, role_id) DO NOTHING")
    Mono<Void> assignRoleToUser(long roleId, long userId);
}