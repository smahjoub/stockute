package com.smahjoub.stockute.application.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration properties for user preferences.
 * 
 * <p>Binds to the {@code stockute.user-preferences} prefix in application.yml.
 * Defines which preference keys are allowed to be saved in the system.</p>
 * 
 * <p>Example configuration:
 * <pre>
 * stockute:
 *   user-preferences:
 *     allowed-keys:
 *       - theme
 *       - email-notification
 * </pre>
 * </p>
 */
@Data
@Component
@ConfigurationProperties(prefix = "stockute.user-preferences")
public class UserPreferenceProperties {
    
    /**
     * List of allowed preference keys that can be saved.
     * Any attempt to save a preference with a key not in this list will be rejected.
     */
    private List<String> allowedKeys = new ArrayList<>();
}
