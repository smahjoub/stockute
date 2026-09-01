package com.smahjoub.stockute.adapters.restful.membership.controller;

import com.smahjoub.stockute.adapters.restful.membership.dto.CreateUserRequest;
import com.smahjoub.stockute.adapters.restful.membership.dto.UserDTO;
import com.smahjoub.stockute.adapters.restful.membership.mapper.CreateUserMapper;
import com.smahjoub.stockute.adapters.restful.membership.mapper.UserMapper;
import com.smahjoub.stockute.application.port.membership.in.UserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/users")
@PreAuthorize("hasAuthority('ADMIN')")
@AllArgsConstructor
public class UserController {

    private final UserUseCase userUseCase;
    private final CreateUserMapper createUserMapper;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDTO> createUser(@RequestBody final CreateUserRequest request) {
        return userUseCase.createUser(createUserMapper.toUser(request))
                .map(userMapper::toUserDTO);
    }
}