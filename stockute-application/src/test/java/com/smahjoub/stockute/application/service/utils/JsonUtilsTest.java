package com.smahjoub.stockute.application.service.utils;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonUtilsTest {

    @Test
    void toJson_shouldSerializeObjectToJsonString() {
        // Given
        Map<String, Object> data = Map.of("name", "John", "age", 30);

        // When
        String json = JsonUtils.toJson(data);

        // Then
        assertNotNull(json);
        assertTrue(json.contains("\"name\":\"John\""));
        assertTrue(json.contains("\"age\":30"));
    }

    @Test
    void fromJson_withType_shouldDeserializeToSpecificType() {
        // Given
        String json = "{\"name\":\"John\",\"age\":30}";

        // When - Using real static implementation, no mocking
        Map result = JsonUtils.fromJson(json, Map.class);

        // Then
        assertNotNull(result);
        assertEquals("John", result.get("name"));
        assertEquals(30, result.get("age"));
    }

    @Test
    void fromJson_withoutType_shouldDeserializeToGenericObject() {
        // Given
        String json = "{\"theme\":\"dark\",\"fontSize\":14}";

        // When - Using real static implementation, no mocking
        Object result = JsonUtils.fromJson(json);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof Map);
        Map<?, ?> map = (Map<?, ?>) result;
        assertEquals("dark", map.get("theme"));
        assertEquals(14, map.get("fontSize"));
    }

    @Test
    void toJson_shouldHandleComplexObjects() {
        // Given
        Map<String, Object> complex = Map.of(
            "user", Map.of("id", 1, "name", "Alice"),
            "preferences", Map.of("theme", "dark")
        );

        // When
        String json = JsonUtils.toJson(complex);

        // Then
        assertNotNull(json);
        assertTrue(json.contains("Alice"));
        assertTrue(json.contains("dark"));
    }

    @Test
    void fromJson_withType_shouldThrowExceptionForMalformedJson() {
        // Given
        String malformedJson = "{invalid json}";

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            JsonUtils.fromJson(malformedJson, Map.class);
        });
    }

    @Test
    void fromJson_withoutType_shouldReturnRawStringForMalformedJson() {
        // Given
        String malformedJson = "{invalid json}";

        // When
        Object result = JsonUtils.fromJson(malformedJson);

        // Then - Returns raw string instead of throwing
        assertEquals(malformedJson, result);
    }
}
