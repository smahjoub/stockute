package com.smahjoub.stockute.adapters.restful.membership.mapper;

import com.smahjoub.stockute.adapters.restful.membership.dto.CreateUserRequest;
import com.smahjoub.stockute.domain.model.User;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
@Slf4j
public abstract class CreateUserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    public abstract User toUser(CreateUserRequest request);

    @BeforeMapping
    protected void setDefaults(CreateUserRequest request, @MappingTarget User user) {
        log.info("CreateUserMapper: setting defaults for user: {}", request.username());
        final var now = LocalDateTime.now();
        user.setCreatedDate(now);
        user.setLastModifiedDate(now);
        user.setVersion(0L);
    }
}