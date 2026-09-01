package com.smahjoub.stockute.domain.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("users_in_roles")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserInRole {

    public UserInRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }

    @Id
    @Column("id")
    private Long id;

    @Column("user_id")
    private Long userId;

    @Column("role_id")
    private Long roleId;

    @CreatedDate
    @Column("created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column("last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Version
    @Column("version")
    private Long version;
}