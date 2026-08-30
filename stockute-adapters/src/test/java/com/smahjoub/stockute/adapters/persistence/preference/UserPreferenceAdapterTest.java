package com.smahjoub.stockute.adapters.persistence.preference;

import com.smahjoub.stockute.domain.model.UserPreference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPreferenceAdapterTest {

    @Mock
    private UserPreferenceRepository userPreferenceRepository;

    @InjectMocks
    private UserPreferenceAdapter adapter;

    private UserPreference themePreference;
    private UserPreference emailPreference;

    @BeforeEach
    void setup() {
        themePreference = UserPreference.builder()
                .id(1L)
                .userId(99L)
                .preferenceKey("theme")
                .preferenceValue("{\"color\":\"dark\"}")
                .build();

        emailPreference = UserPreference.builder()
                .id(2L)
                .userId(99L)
                .preferenceKey("email-notification")
                .preferenceValue("{\"enabled\":true}")
                .build();
    }

    @Test
    void save_newPreference_ShouldCallSave() {
        UserPreference newPreference = UserPreference.builder()
                .userId(99L)
                .preferenceKey("theme")
                .preferenceValue("{\"color\":\"dark\"}")
                .build();

        UserPreference savedPreference = UserPreference.builder()
                .id(1L)
                .userId(99L)
                .preferenceKey("theme")
                .preferenceValue("{\"color\":\"dark\"}")
                .build();

        when(userPreferenceRepository.save(newPreference))
                .thenReturn(Mono.just(savedPreference));

        StepVerifier.create(adapter.save(newPreference))
                .expectNext(savedPreference)
                .verifyComplete();

        verify(userPreferenceRepository).save(newPreference);
    }

    @Test
    void save_existingPreference_ShouldCallSave() {
        UserPreference updatedPreference = UserPreference.builder()
                .id(1L)
                .userId(99L)
                .preferenceKey("theme")
                .preferenceValue("{\"color\":\"light\"}")
                .build();

        when(userPreferenceRepository.save(updatedPreference))
                .thenReturn(Mono.just(updatedPreference));

        StepVerifier.create(adapter.save(updatedPreference))
                .expectNext(updatedPreference)
                .verifyComplete();

        verify(userPreferenceRepository).save(updatedPreference);
    }

    @Test
    void findAllByUserId_WhenFound_ReturnsPreferences() {
        when(userPreferenceRepository.findAllByUserId(99L))
                .thenReturn(Flux.just(themePreference, emailPreference));

        StepVerifier.create(adapter.findAllByUserId(99L))
                .expectNext(themePreference)
                .expectNext(emailPreference)
                .verifyComplete();

        verify(userPreferenceRepository).findAllByUserId(99L);
    }

    @Test
    void findAllByUserId_WhenNotFound_ReturnsEmpty() {
        when(userPreferenceRepository.findAllByUserId(999L))
                .thenReturn(Flux.empty());

        StepVerifier.create(adapter.findAllByUserId(999L))
                .verifyComplete();

        verify(userPreferenceRepository).findAllByUserId(999L);
    }

    @Test
    void findByUserIdAndKey_WhenFound_ReturnsPreference() {
        when(userPreferenceRepository.findByUserIdAndKey(99L, "theme"))
                .thenReturn(Mono.just(themePreference));

        StepVerifier.create(adapter.findByUserIdAndKey(99L, "theme"))
                .expectNext(themePreference)
                .verifyComplete();

        verify(userPreferenceRepository).findByUserIdAndKey(99L, "theme");
    }

    @Test
    void findByUserIdAndKey_WhenNotFound_ReturnsEmpty() {
        when(userPreferenceRepository.findByUserIdAndKey(99L, "non-existent"))
                .thenReturn(Mono.empty());

        StepVerifier.create(adapter.findByUserIdAndKey(99L, "non-existent"))
                .verifyComplete();

        verify(userPreferenceRepository).findByUserIdAndKey(99L, "non-existent");
    }

    @Test
    void deleteByUserIdAndKey_WhenFound_DeletesPreference() {
        when(userPreferenceRepository.findByUserIdAndKey(99L, "theme"))
                .thenReturn(Mono.just(themePreference));
        when(userPreferenceRepository.deleteById(1L))
                .thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteByUserIdAndKey(99L, "theme"))
                .verifyComplete();

        verify(userPreferenceRepository).findByUserIdAndKey(99L, "theme");
        verify(userPreferenceRepository).deleteById(1L);
    }

    @Test
    void deleteByUserIdAndKey_WhenNotFound_CompletesWithoutDeleting() {
        when(userPreferenceRepository.findByUserIdAndKey(99L, "non-existent"))
                .thenReturn(Mono.empty());

        StepVerifier.create(adapter.deleteByUserIdAndKey(99L, "non-existent"))
                .verifyComplete();

        verify(userPreferenceRepository).findByUserIdAndKey(99L, "non-existent");
    }

    @Test
    void upsert_shouldDelegateToRepository() {
        when(userPreferenceRepository.upsert(99L, "theme", "{\"color\":\"dark\"}"))
                .thenReturn(Mono.just(true));

        StepVerifier.create(adapter.upsert(99L, "theme", "{\"color\":\"dark\"}"))
                .verifyComplete();

        verify(userPreferenceRepository).upsert(99L, "theme", "{\"color\":\"dark\"}");
    }
}
