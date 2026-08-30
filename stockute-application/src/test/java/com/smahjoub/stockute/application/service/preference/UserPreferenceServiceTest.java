package com.smahjoub.stockute.application.service.preference;

import com.smahjoub.stockute.application.config.UserPreferenceProperties;
import com.smahjoub.stockute.application.exception.InvalidPreferenceKeyException;
import com.smahjoub.stockute.application.exception.UserPreferenceNotFoundException;
import com.smahjoub.stockute.application.port.preference.out.UserPreferencePort;
import com.smahjoub.stockute.domain.model.UserPreference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPreferenceServiceTest {

    @Mock
    private UserPreferencePort userPreferencePort;

    private UserPreferenceProperties preferenceProperties;
    private UserPreferenceService userPreferenceService;

    @BeforeEach
    void setUp() {
        preferenceProperties = new UserPreferenceProperties();
        preferenceProperties.setAllowedKeys(List.of("theme", "email-notification"));
        userPreferenceService = new UserPreferenceService(userPreferencePort, preferenceProperties);
    }

    @Test
    void savePreference_withValidKey_shouldSucceed() {
        // Given
        Long userId = 1L;
        String key = "theme";
        Map<String, String> value = Map.of("color", "dark");

        when(userPreferencePort.findByUserIdAndKey(userId, key))
                .thenReturn(Mono.empty());
        when(userPreferencePort.save(any(UserPreference.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        // When & Then
        StepVerifier.create(userPreferenceService.savePreference(userId, key, value))
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result).isInstanceOf(Map.class);
                    @SuppressWarnings("unchecked")
                    Map<String, String> resultMap = (Map<String, String>) result;
                    assertThat(resultMap.get("color")).isEqualTo("dark");
                })
                .verifyComplete();
    }

    @Test
    void savePreference_withInvalidKey_shouldThrowException() {
        // Given
        Long userId = 1L;
        String key = "invalid-key";
        Map<String, String> value = Map.of("data", "test");

        // When & Then
        StepVerifier.create(userPreferenceService.savePreference(userId, key, value))
                .expectErrorMatches(error -> 
                    error instanceof InvalidPreferenceKeyException &&
                    error.getMessage().contains("Invalid preference key: invalid-key") &&
                    error.getMessage().contains("Allowed keys are: [theme, email-notification]")
                )
                .verify();
    }

    @Test
    void getPreference_existingPreference_shouldReturn() {
        // Given
        Long userId = 1L;
        String key = "theme";
        String jsonValue = "{\"color\":\"dark\"}";
        
        UserPreference preference = UserPreference.builder()
                .id(1L)
                .userId(userId)
                .preferenceKey(key)
                .preferenceValue(jsonValue)
                .build();

        when(userPreferencePort.findByUserIdAndKey(userId, key))
                .thenReturn(Mono.just(preference));

        // When & Then
        StepVerifier.create(userPreferenceService.getPreference(userId, key, Object.class))
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result).isInstanceOf(Map.class);
                    @SuppressWarnings("unchecked")
                    Map<String, String> resultMap = (Map<String, String>) result;
                    assertThat(resultMap.get("color")).isEqualTo("dark");
                })
                .verifyComplete();
    }

    @Test
    void getPreference_nonExistingPreference_shouldThrowException() {
        // Given
        Long userId = 1L;
        String key = "non-existent";

        when(userPreferencePort.findByUserIdAndKey(userId, key))
                .thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(userPreferenceService.getPreference(userId, key, Object.class))
                .expectErrorMatches(error ->
                    error instanceof UserPreferenceNotFoundException &&
                    error.getMessage().contains("Preference not found for user 1 with key: non-existent")
                )
                .verify();
    }

    @Test
    void getAllPreferences_shouldReturnMap() {
        // Given
        Long userId = 1L;
        
        UserPreference themePreference = UserPreference.builder()
                .id(1L)
                .userId(userId)
                .preferenceKey("theme")
                .preferenceValue("{\"color\":\"dark\"}")
                .build();

        UserPreference emailPreference = UserPreference.builder()
                .id(2L)
                .userId(userId)
                .preferenceKey("email-notification")
                .preferenceValue("{\"enabled\":true}")
                .build();

        when(userPreferencePort.findAllByUserId(userId))
                .thenReturn(Flux.just(themePreference, emailPreference));

        // When & Then
        StepVerifier.create(userPreferenceService.getAllPreferences(userId))
                .assertNext(result -> {
                    assertThat(result).hasSize(2);
                    assertThat(result).containsKeys("theme", "email-notification");
                })
                .verifyComplete();
    }
}
