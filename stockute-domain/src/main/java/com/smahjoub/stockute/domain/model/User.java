package com.smahjoub.stockute.domain.model;



import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

import java.util.Set;

@Table("users")
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class User extends Entity implements UserDetails  {
    @Id
    @Column("user_id")
    private Long id;

    @Column("email")
    private String email;
    @Column("username")
    private String username;
    @Column("password")
    private String password;
    @Column("is_enabled")
    private Boolean enabled;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @CreatedDate
    @Column("created_date")
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column("last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Version
    @Column("version")
    private Long version;

    @Transient
    private Set<Role> roles = new HashSet<>();


    public User(String username, String password, Boolean enabled, Set<Role> roles, Long version) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.roles = roles;
    }


    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(authority -> new SimpleGrantedAuthority(authority.getName())).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }
}
