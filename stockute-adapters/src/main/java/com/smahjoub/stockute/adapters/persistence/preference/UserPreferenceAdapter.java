package com.smahjoub.stockute.adapters.persistence.preference;

import com.smahjoub.stockute.application.port.preference.out.UserPreferencePort;
import com.smahjoub.stockute.domain.model.UserPreference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Persistence adapter for user preferences.
 *
 * <p>Delegates to the R2DBC repository for actual database operations.
 * The {@link UserPreference} entity's {@code preferenceValue} field is stored
 * as a plain {@code String}, which R2DBC maps directly to/from the
 * PostgreSQL {@code TEXT} column with no driver-specific types required.</p>
 */
@Component
@RequiredArgsConstructor
public class UserPreferenceAdapter implements UserPreferencePort {

    private final UserPreferenceRepository userPreferenceRepository;

    @Override
    public Flux<UserPreference> findAllByUserId(Long userId) {
        return userPreferenceRepository.findAllByUserId(userId);
    }

    @Override
    public Mono<UserPreference> findByUserIdAndKey(Long userId, String key) {
        return userPreferenceRepository.findByUserIdAndKey(userId, key);
    }

    @Override
    public Mono<UserPreference> save(UserPreference preference) {
        return userPreferenceRepository.save(preference);
    }

    @Override
    public Mono<Void> deleteByUserIdAndKey(Long userId, String key) {
        return userPreferenceRepository.findByUserIdAndKey(userId, key)
                .flatMap(preference -> userPreferenceRepository.deleteById(preference.getId()));
    }

    @Override
    public Mono<Void> upsert(Long userId, String key, String serializedValue) {
        return userPreferenceRepository.upsert(userId, key, serializedValue).then();
    }
}