package com.smahjoub.stockute.adapters.restful.membership.mapper;

import com.smahjoub.stockute.adapters.restful.membership.dto.RoleDTO;
import com.smahjoub.stockute.domain.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDTO toRoleDTO(Role role);
    Role toRole(RoleDTO dto);
}

