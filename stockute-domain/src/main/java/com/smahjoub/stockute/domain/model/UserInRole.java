package com.smahjoub.stockute.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

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
}