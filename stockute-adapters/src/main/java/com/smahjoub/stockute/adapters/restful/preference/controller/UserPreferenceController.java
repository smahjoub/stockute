package com.smahjoub.stockute.adapters.restful.preference.controller;

import com.smahjoub.stockute.application.exception.InvalidPreferenceKeyException;
import com.smahjoub.stockute.application.exception.UserPreferenceNotFoundException;
import com.smahjoub.stockute.application.port.preference.in.UserPreferenceUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * REST controller for user preference operations.
 *
 * <p>Endpoints accept and return generic JSON bodies. The controller delegates
 * to {@link UserPreferenceUseCase}, which handles serialization/deserialization
 * of the generic preference values.</p>
 *
 * <p>Error handling:</p>
 * <ul>
 *   <li>404 - Preference key not found for the given user</li>
 *   <li>400 - Malformed JSON in the request body (handled by Spring WebFlux)</li>
 * </ul>
 */
@RestController
@RequestMapping("/v1/users/{userId}/preferences")
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
@RequiredArgsConstructor
public class UserPreferenceController {

    private final UserPreferenceUseCase userPreferenceUseCase;

    /**
     * Fetches all preferences for the specified user.
     *
     * @param userId the user identifier
     * @return a map of preference keys to their values
     */
    @GetMapping
    public Mono<Map<String, Object>> getAllPreferences(@PathVariable final Long userId) {
        return userPreferenceUseCase.getAllPreferences(userId);
    }

    /**
     * Fetches a single preference by key for the specified user.
     *
     * @param userId the user identifier
     * @param key    the preference key
     * @return the preference value as a generic JSON object
     */
    @GetMapping("/{key}")
    public Mono<Object> getPreference(@PathVariable final Long userId,
                                      @PathVariable final String key) {
        return userPreferenceUseCase.<Object>getPreference(userId, key, Object.class)
                .onErrorMap(UserPreferenceNotFoundException.class,
                        ex -> new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    /**
     * Saves or updates a preference for the specified user.
     * If a preference with the given key already exists, it is updated;
     * otherwise, a new preference is created.
     *
     * @param userId the user identifier
     * @param key    the preference key
     * @param value  the preference value (any valid JSON)
     * @return the saved preference value
     */
    @PutMapping("/{key}")
    public Mono<Object> savePreference(@PathVariable final Long userId,
                                       @PathVariable final String key,
                                       @RequestBody final Object value) {
        return userPreferenceUseCase.savePreference(userId, key, value)
                .onErrorMap(InvalidPreferenceKeyException.class,
                        ex -> new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }
}