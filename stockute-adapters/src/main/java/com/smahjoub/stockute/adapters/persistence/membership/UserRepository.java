package com.smahjoub.stockute.adapters.persistence.membership;

import com.smahjoub.stockute.domain.model.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long> {

    Mono<User> findUserByUsername(String userName);

    Mono<User> findUserByEmail(String email);

    @Query("SELECT roles.name  FROM users " +
            "INNER JOIN users_in_roles " +
            "ON users_in_roles.user_id = users.user_id " +
            "AND users.username = :userName " +
            "INNER JOIN roles " +
            "ON roles.role_id = users_in_roles.role_id ")
    Flux<String> getUserRoles(String userName);
}
