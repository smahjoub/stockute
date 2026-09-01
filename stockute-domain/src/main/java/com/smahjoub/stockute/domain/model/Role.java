package com.smahjoub.stockute.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("roles")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Role extends Entity {
    @Id
    @Column("role_id")
    private Long id;

    private String name;

    private String description;
}