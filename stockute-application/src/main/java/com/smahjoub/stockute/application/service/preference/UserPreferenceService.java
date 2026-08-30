package com.smahjoub.stockute.application.service.preference;

import com.smahjoub.stockute.application.config.UserPreferenceProperties;
import com.smahjoub.stockute.application.exception.InvalidPreferenceKeyException;
import com.smahjoub.stockute.application.exception.UserPreferenceNotFoundException;
import com.smahjoub.stockute.application.port.preference.in.UserPreferenceUseCase;
import com.smahjoub.stockute.application.port.preference.out.UserPreferencePort;
import com.smahjoub.stockute.application.service.utils.JsonUtils;
import com.smahjoub.stockute.domain.model.UserPreference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Service implementing user preference business logic.
 *
 * <h3>How the generic type {@code T} is handled:</h3>
 * <p>When <strong>writing</strong> to the database, the service uses
 * {@link JsonUtils#toJson(Object)} to serialize any Java object into a JSON string,
 * which is then stored in the {@code preference_value} column
 * (PostgreSQL TEXT via R2DBC String mapping).</p>
 *
 * <p>When <strong>reading</strong> from the database, the service uses
 * {@link JsonUtils#fromJson(String, Class)} to deserialize the JSON string
 * back into the requested Java type. The caller specifies the expected type
 * via a {@code Class<T>} parameter, ensuring type safety at the service boundary.</p>
 *
 * <p>For the "fetch all" endpoint, values are deserialized as generic
 * {@link Object} instances via {@link JsonUtils#fromJson(String)} (typically
 * {@code Map<String, Object>} or {@code List<Object>} depending on the JSON structure).</p>
 */
@Service
@RequiredArgsConstructor
public class UserPreferenceService implements UserPreferenceUseCase {

    private final UserPreferencePort userPreferencePort;
    private final UserPreferenceProperties preferenceProperties;

    @Override
    public Mono<Map<String, Object>> getAllPreferences(Long userId) {
        return userPreferencePort.findAllByUserId(userId)
                .collectMap(
                        UserPreference::getPreferenceKey,
                        preference -> JsonUtils.fromJson(preference.getPreferenceValue())
                );
    }

    @Override
    public <T> Mono<T> getPreference(Long userId, String key, Class<T> type) {
        return userPreferencePort.findByUserIdAndKey(userId, key)
                .switchIfEmpty(Mono.error(new UserPreferenceNotFoundException(
                        "Preference not found for user " + userId + " with key: " + key)))
                .map(preference -> JsonUtils.fromJson(preference.getPreferenceValue(), type));
    }

    @Override
    public Mono<Object> savePreference(Long userId, String key, Object value) {
        // Validate that the key is in the allowed list
        if (!preferenceProperties.getAllowedKeys().contains(key)) {
            return Mono.error(new InvalidPreferenceKeyException(
                    "Invalid preference key: " + key + ". Allowed keys are: " + 
                    preferenceProperties.getAllowedKeys()));
        }

        // Check if a preference with this key already exists for the user
        return userPreferencePort.findByUserIdAndKey(userId, key)
                .flatMap(existing -> {
                    // Update existing preference
                    existing.setPreferenceValue(JsonUtils.toJson(value));
                    return userPreferencePort.save(existing);
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // Create new preference
                    UserPreference newPreference = UserPreference.builder()
                            .userId(userId)
                            .preferenceKey(key)
                            .preferenceValue(JsonUtils.toJson(value))
                            .build();
                    return userPreferencePort.save(newPreference);
                }))
                .map(saved -> JsonUtils.fromJson(saved.getPreferenceValue()));
    }
}