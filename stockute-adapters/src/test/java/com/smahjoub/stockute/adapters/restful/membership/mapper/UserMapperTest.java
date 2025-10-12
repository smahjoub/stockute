package com.smahjoub.stockute.adapters.restful.membership.mapper;

import com.smahjoub.stockute.adapters.restful.membership.dto.UserDTO;
import com.smahjoub.stockute.domain.model.Role;
import com.smahjoub.stockute.domain.model.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;


import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);


    @Test
    void testToUserDTO_ValidUser_ReturnsCorrectDTO() {
        User user = new User();
        user.setId(1L);
        user.setUsername("john_doe");
        user.setEmail("john@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEnabled(true);
        Set<Role> roles = Set.of(new Role(1L, "USER", ""), new Role(2L, "ADMIN", ""));
        user.setRoles(roles);

        UserDTO result = userMapper.toUserDTO(user);

        assertNotNull(result);
        assertEquals(user.getId(), result.userId());
        assertEquals(user.getUsername(), result.userName());
        assertEquals(user.getEmail(), result.email());
        assertEquals(user.getFirstName(), result.firstName());
        assertEquals(user.getLastName(), result.lastName());
        assertEquals(user.isAccountNonLocked(), result.active()); // Assuming isAccountNonLocked maps to active
        assertEquals(List.of("USER", "ADMIN"), result.roles());
    }
}