package com.smahjoub.stockute.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Domain entity representing a user preference stored as a key-value pair.
 *
 * <p>The {@code preferenceValue} field holds the raw JSON string representation
 * of a generic object of type {@code T}. Serialization and deserialization between
 * the Java object and this JSON string is handled by the service layer using
 * Jackson's {@code ObjectMapper}.</p>
 *
 * <p>This design keeps the domain model free of serialization concerns.
 * The persistence layer (R2DBC) maps the column as a plain {@code String}
 * to/from a PostgreSQL {@code TEXT} column — no driver-specific types needed.</p>
 *
 * <p>Auditing fields ({@code createdDate}, {@code lastModifiedDate}, {@code version})
 * are inherited from the {@link Entity} base class.</p>
 */
@Table("user_preferences")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
public class UserPreference extends Entity {

    @Id
    @Column("id")
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("preference_key")
    private String preferenceKey;

    /**
     * The raw JSON string representation of the preference value.
     * This is mapped to a PostgreSQL TEXT column by the persistence layer.
     * The service layer is responsible for converting between this string
     * and the actual Java object of type {@code T}.
     */
    @Column("preference_value")
    private String preferenceValue;
}