package com.smahjoub.stockute.application.service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Reusable static utility class for JSON serialization and deserialization.
 *
 * <p>Wraps Jackson's {@link ObjectMapper} with static convenience methods that convert
 * between Java objects and their JSON string representations. Designed to be
 * used by any service that needs to persist or read generic JSON data
 * (e.g., JSONB columns in PostgreSQL).</p>
 *
 * <p>This class is intentionally placed in a shared {@code utils} package
 * so that it can be reused across different service modules without creating
 * coupling between them.</p>
 *
 * <p>Usage example:
 * <pre>{@code
 * String json = JsonUtils.toJson(myObject);
 * MyType obj = JsonUtils.fromJson(json, MyType.class);
 * Object generic = JsonUtils.fromJson(json);
 * }</pre>
 * </p>
 */
@Slf4j
public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtils() {
        // Utility class - prevent instantiation
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Serializes a Java object to a JSON string for database storage.
     * This is called when saving/updating a preference.
     *
     * @param value the object to serialize
     * @return the JSON string representation
     * @throws IllegalArgumentException if serialization fails (malformed object)
     */
    public static String toJson(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize value to JSON", e);
        }
    }

    /**
     * Deserializes a JSON string from the database into the requested type.
     * This is called when reading a preference with a known target type.
     *
     * @param json the JSON string from the database
     * @param type the target class for deserialization
     * @param <T>  the target type
     * @return the deserialized object
     * @throws IllegalArgumentException if deserialization fails (malformed JSON)
     */
    public static <T> T fromJson(String json, Class<T> type) {
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to deserialize value from JSON", e);
        }
    }

    /**
     * Deserializes a JSON string from the database into a generic Object.
     * Used when the target type is unknown (e.g., fetching all preferences).
     *
     * @param json the JSON string from the database
     * @return the deserialized object (typically a Map or List)
     */
    public static Object fromJson(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, Object.class);
        } catch (JsonProcessingException e) {
            log.warn("Failed to deserialize JSON value, returning raw string: {}", json, e);
            return json;
        }
    }
}