package com.smahjoub.stockute.adapters.persistence.preference;

import com.smahjoub.stockute.domain.model.UserPreference;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserPreferenceRepository extends R2dbcRepository<UserPreference, Long> {

    Flux<UserPreference> findAllByUserId(Long userId);

    @Query("SELECT * FROM user_preferences WHERE user_id = :userId AND preference_key = :key")
    Mono<UserPreference> findByUserIdAndKey(Long userId, String key);
}