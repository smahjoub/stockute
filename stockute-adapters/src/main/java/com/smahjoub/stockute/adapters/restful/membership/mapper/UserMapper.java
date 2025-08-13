package com.smahjoub.stockute.adapters.restful.membership.mapper;

import com.smahjoub.stockute.adapters.restful.membership.dto.UserDTO;
import com.smahjoub.stockute.domain.model.Role;
import com.smahjoub.stockute.domain.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default UserDTO toUserDTO(User user) {
     return new UserDTO(user.getId(), user.getUsername(), user.getEmail(), user.getFirstName(),
                        user.getLastName(), user.isAccountNonLocked(), user.getRoles().stream().map(Role::getName).toList());
    }

}

