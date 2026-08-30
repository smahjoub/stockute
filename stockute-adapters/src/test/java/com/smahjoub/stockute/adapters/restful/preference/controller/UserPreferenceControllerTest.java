package com.smahjoub.stockute.adapters.restful.preference.controller;

import com.smahjoub.stockute.application.exception.InvalidPreferenceKeyException;
import com.smahjoub.stockute.application.exception.UserPreferenceNotFoundException;
import com.smahjoub.stockute.application.port.preference.in.UserPreferenceUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPreferenceControllerTest {

    @Mock
    private UserPreferenceUseCase userPreferenceUseCase;

    @InjectMocks
    private UserPreferenceController controller;

    @Test
    void getAllPreferences_returnsPreferencesMap() {
        Long userId = 1L;
        Map<String, Object> preferences = Map.of(
                "theme", Map.of("color", "dark"),
                "email-notification", Map.of("enabled", true)
        );

        when(userPreferenceUseCase.getAllPreferences(userId))
                .thenReturn(Mono.just(preferences));

        StepVerifier.create(controller.getAllPreferences(userId))
                .assertNext(result -> {
                    assertThat(result).hasSize(2);
                    assertThat(result).containsKeys("theme", "email-notification");
                })
                .verifyComplete();

        verify(userPreferenceUseCase).getAllPreferences(userId);
    }

    @Test
    void getAllPreferences_whenEmpty_returnsEmptyMap() {
        Long userId = 999L;

        when(userPreferenceUseCase.getAllPreferences(userId))
                .thenReturn(Mono.just(Map.of()));

        StepVerifier.create(controller.getAllPreferences(userId))
                .assertNext(result -> assertThat(result).isEmpty())
                .verifyComplete();

        verify(userPreferenceUseCase).getAllPreferences(userId);
    }

    @Test
    void getPreference_whenFound_returnsValue() {
        Long userId = 1L;
        String key = "theme";
        Map<String, String> value = Map.of("color", "dark");

        when(userPreferenceUseCase.getPreference(userId, key, Object.class))
                .thenReturn(Mono.just(value));

        StepVerifier.create(controller.getPreference(userId, key))
                .assertNext(result -> {
                    assertThat(result).isInstanceOf(Map.class);
                    @SuppressWarnings("unchecked")
                    Map<String, String> resultMap = (Map<String, String>) result;
                    assertThat(resultMap.get("color")).isEqualTo("dark");
                })
                .verifyComplete();

        verify(userPreferenceUseCase).getPreference(userId, key, Object.class);
    }

    @Test
    void getPreference_whenNotFound_mapsTo404() {
        Long userId = 1L;
        String key = "non-existent";

        when(userPreferenceUseCase.getPreference(userId, key, Object.class))
                .thenReturn(Mono.error(new UserPreferenceNotFoundException(
                        "Preference not found for user 1 with key: non-existent")));

        StepVerifier.create(controller.getPreference(userId, key))
                .expectErrorMatches(error ->
                        error instanceof ResponseStatusException rse
                                && rse.getStatusCode().value() == 404
                                && rse.getReason().contains("Preference not found")
                )
                .verify();

        verify(userPreferenceUseCase).getPreference(userId, key, Object.class);
    }

    @Test
    void savePreference_withValidKey_returnsSavedValue() {
        Long userId = 1L;
        String key = "theme";
        Map<String, String> value = Map.of("color", "dark");

        when(userPreferenceUseCase.savePreference(userId, key, value))
                .thenReturn(Mono.just(value));

        StepVerifier.create(controller.savePreference(userId, key, value))
                .assertNext(result -> {
                    assertThat(result).isInstanceOf(Map.class);
                    @SuppressWarnings("unchecked")
                    Map<String, String> resultMap = (Map<String, String>) result;
                    assertThat(resultMap.get("color")).isEqualTo("dark");
                })
                .verifyComplete();

        verify(userPreferenceUseCase).savePreference(userId, key, value);
    }

    @Test
    void savePreference_withInvalidKey_mapsTo400() {
        Long userId = 1L;
        String key = "invalid-key";
        Map<String, String> value = Map.of("data", "test");

        when(userPreferenceUseCase.savePreference(userId, key, value))
                .thenReturn(Mono.error(new InvalidPreferenceKeyException(
                        "Invalid preference key: invalid-key. Allowed keys are: [theme, email-notification]")));

        StepVerifier.create(controller.savePreference(userId, key, value))
                .expectErrorMatches(error ->
                        error instanceof ResponseStatusException rse
                                && rse.getStatusCode().value() == 400
                                && rse.getReason().contains("Invalid preference key")
                )
                .verify();

        verify(userPreferenceUseCase).savePreference(userId, key, value);
    }
}
