package com.smahjoub.stockute.adapters.restful.membership.controller;

import com.smahjoub.stockute.adapters.helper.AuthenticationHelper;
import com.smahjoub.stockute.adapters.restful.membership.dto.ChangePasswordRequest;
import com.smahjoub.stockute.adapters.restful.membership.dto.UpdateProfileRequest;
import com.smahjoub.stockute.adapters.restful.membership.dto.UserDTO;
import com.smahjoub.stockute.adapters.restful.membership.mapper.UserMapper;
import com.smahjoub.stockute.application.port.membership.in.UserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
@AllArgsConstructor
@RequestMapping("/v1/profile")
public class ProfileController {

    private final UserUseCase userUseCase;
    private final UserMapper userMapper;

    @GetMapping
    public Mono<UserDTO> getProfile() {
        return AuthenticationHelper.getAuthenticatedUserName()
                .flatMap(userUseCase::getUserByUsername)
                .map(userMapper::toUserDTO);
    }

    @PutMapping
    public Mono<UserDTO> updateProfile(@RequestBody UpdateProfileRequest request) {
        return AuthenticationHelper.getAuthenticatedUserName()
                .flatMap(username -> userUseCase.updateProfile(username, request.email(), request.firstName(), request.lastName()))
                .map(userMapper::toUserDTO);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        return AuthenticationHelper.getAuthenticatedUserName()
                .flatMap(username -> userUseCase.changePassword(username, request.currentPassword(), request.newPassword()));
    }
}
