package com.smahjoub.stockute.application.port.preference.out;

import com.smahjoub.stockute.domain.model.UserPreference;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Out-port defining data access operations for user preferences.
 *
 * <p>Implementations of this port (adapters) handle the actual persistence
 * to the database. The {@link UserPreference} entity carries the preference
 * value as a raw JSON string, so no type conversion is needed at this layer.</p>
 */
public interface UserPreferencePort {

    /**
     * Finds all preferences for a given user.
     *
     * @param userId the user identifier
     * @return a flux of user preference entities
     */
    Flux<UserPreference> findAllByUserId(Long userId);

    /**
     * Finds a single preference by user ID and key.
     *
     * @param userId the user identifier
     * @param key    the preference key
     * @return a mono containing the preference, or empty if not found
     */
    Mono<UserPreference> findByUserIdAndKey(Long userId, String key);

    /**
     * Saves (insert or update) a user preference.
     *
     * @param preference the preference entity to save
     * @return a mono containing the saved preference
     */
    Mono<UserPreference> save(UserPreference preference);

    /**
     * Deletes a user preference by user ID and key.
     *
     * @param userId the user identifier
     * @param key    the preference key
     * @return a mono completing when the deletion is done
     */
    Mono<Void> deleteByUserIdAndKey(Long userId, String key);
}