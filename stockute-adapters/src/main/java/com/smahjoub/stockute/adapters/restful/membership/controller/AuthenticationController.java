package com.smahjoub.stockute.adapters.restful.membership.controller;


import com.smahjoub.stockute.adapters.config.JWTUtil;
import com.smahjoub.stockute.adapters.restful.membership.dto.AuthRequest;
import com.smahjoub.stockute.adapters.restful.membership.dto.AuthResponse;
import com.smahjoub.stockute.adapters.restful.membership.mapper.UserMapper;
import com.smahjoub.stockute.application.port.membership.in.UserUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final JWTUtil jwtUtil;
    private final UserUseCase userUseCase;
    private final UserMapper userMapper;

    @PostMapping("/authenticate")
    public Mono<ResponseEntity<AuthResponse>> authenticate(@RequestBody AuthRequest request) {
        return userUseCase.authenticate(request.email(), request.password())
            .map(user -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userMapper.toUserDTO(user)))))
            .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

}
