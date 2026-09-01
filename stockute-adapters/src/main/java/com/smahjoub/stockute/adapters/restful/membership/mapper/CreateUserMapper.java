package com.smahjoub.stockute.adapters.restful.membership.mapper;

import com.smahjoub.stockute.adapters.restful.membership.dto.CreateUserRequest;
import com.smahjoub.stockute.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class CreateUserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    public abstract User toUser(CreateUserRequest request);
}