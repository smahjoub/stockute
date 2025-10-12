package com.smahjoub.stockute.adapters.restful.membership.controller;

import com.smahjoub.stockute.adapters.config.JWTUtil;
import com.smahjoub.stockute.adapters.restful.membership.dto.AuthRequest;
import com.smahjoub.stockute.adapters.restful.membership.mapper.UserMapper;
import com.smahjoub.stockute.application.port.membership.in.UserUseCase;
import com.smahjoub.stockute.domain.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AuthenticationControllerTest {
    @Mock
    private JWTUtil jwtUtil;
    @Mock
    private UserUseCase userUseCase;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private AuthenticationController controller;
    private WebTestClient webTestClient;
    private AutoCloseable mocks;
    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @AfterEach()
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void testAuthenticateSuccess() {
        AuthRequest request = new AuthRequest("test@example.com", "password");
        User user = new User();
        when(userUseCase.authenticate(anyString(), anyString())).thenReturn(Mono.just(user));
        when(userMapper.toUserDTO(any())).thenReturn(null);
        when(jwtUtil.generateToken(any())).thenReturn("token");
        webTestClient.post().uri("/authenticate")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testAuthenticateFailure() {
        AuthRequest request = new AuthRequest("test@example.com", "wrong");
        when(userUseCase.authenticate(anyString(), anyString())).thenReturn(Mono.empty());
        webTestClient.post().uri("/authenticate")
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}

