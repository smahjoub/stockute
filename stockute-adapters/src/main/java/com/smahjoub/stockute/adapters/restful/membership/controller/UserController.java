package com.smahjoub.stockute.adapters.restful.membership.controller;

import com.smahjoub.stockute.adapters.restful.membership.dto.CreateUserRequest;
import com.smahjoub.stockute.adapters.restful.membership.dto.UserDTO;
import com.smahjoub.stockute.adapters.restful.membership.mapper.CreateUserMapper;
import com.smahjoub.stockute.adapters.restful.membership.mapper.UserMapper;
import com.smahjoub.stockute.application.port.membership.in.UserUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/users")
@PreAuthorize("hasAuthority('ADMIN')")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserUseCase userUseCase;
    private final CreateUserMapper createUserMapper;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDTO> createUser(@RequestBody final CreateUserRequest request) {
        log.info("UserController.createUser called for username: {}", request.username());
        return userUseCase.createUser(createUserMapper.toUser(request))
                .map(userMapper::toUserDTO)
                .doOnSuccess(userDTO -> log.info("UserController.createUser success: userId={}", userDTO.userId()))
                .doOnError(error -> log.error("UserController.createUser failed: {}", error.getMessage(), error));
    }
}