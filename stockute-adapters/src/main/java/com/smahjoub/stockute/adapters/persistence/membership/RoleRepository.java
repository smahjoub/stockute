package com.smahjoub.stockute.adapters.persistence.membership;

import com.smahjoub.stockute.domain.model.Role;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RoleRepository extends ReactiveCrudRepository<Role, Long> {

    Mono<Role> findByName(String name);

    @Query(value = "INSERT INTO users_in_roles(role_id, user_id) VALUES(:roleId, :userId)")
    Mono<Void> assignRoleToUser(long roleId, long userId);


    @Query("SELECT roles.role_id, roles.name, roles.version, roles.created_date, roles.last_modified_date FROM users " +
            "INNER JOIN users_in_roles " +
            "ON users_in_roles.user_id = users.user_id " +
            "AND users.email = :email " +
            "INNER JOIN roles " +
            "ON roles.role_id = users_in_roles.role_id ")
    Flux<Role> getUserRoles(String email);
}