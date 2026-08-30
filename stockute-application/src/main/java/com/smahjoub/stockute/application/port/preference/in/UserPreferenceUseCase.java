package com.smahjoub.stockute.application.port.preference.in;

import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Use case (in-port) defining user preference operations.
 *
 * <p>The preference value is treated as a generic {@link Object} at the use case
 * boundary. The service implementation handles JSON serialization/deserialization
 * to convert between this generic object and the JSON string stored in the database.</p>
 */
public interface UserPreferenceUseCase {

    /**
     * Fetches all preferences for a given user.
     *
     * @param userId the user identifier
     * @return a map of preference keys to their deserialized values
     */
    Mono<Map<String, Object>> getAllPreferences(Long userId);

    /**
     * Fetches a single preference by key for a given user.
     *
     * @param userId the user identifier
     * @param key    the preference key
     * @param <T>    the expected type of the preference value
     * @param type   the class of the expected type, used for type-safe deserialization
     * @return the deserialized preference value
     */
    <T> Mono<T> getPreference(Long userId, String key, Class<T> type);

    /**
     * Saves or updates a preference for a given user. If a preference with the
     * same key already exists, it is updated; otherwise, a new one is created.
     *
     * @param userId the user identifier
     * @param key    the preference key
     * @param value  the preference value (any JSON-serializable object)
     * @return the saved preference value (echoed back)
     */
    Mono<Object> savePreference(Long userId, String key, Object value);
}