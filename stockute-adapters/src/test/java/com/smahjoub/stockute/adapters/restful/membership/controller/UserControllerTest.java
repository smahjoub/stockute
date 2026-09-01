package com.smahjoub.stockute.adapters.restful.membership.controller;

import com.smahjoub.stockute.adapters.common.WithMockCustomUser;
import com.smahjoub.stockute.adapters.restful.membership.dto.CreateUserRequest;
import com.smahjoub.stockute.adapters.restful.membership.dto.UserDTO;
import com.smahjoub.stockute.adapters.restful.membership.mapper.CreateUserMapper;
import com.smahjoub.stockute.adapters.restful.membership.mapper.UserMapper;
import com.smahjoub.stockute.application.port.membership.in.UserUseCase;
import com.smahjoub.stockute.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private CreateUserMapper createUserMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController controller;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    @WithMockCustomUser
    void testCreateUser() {
        final var request = new CreateUserRequest(
                "john@example.com", "john", "secret", "John", "Doe", "TN");

        final var user = new User();
        user.setId(1L);
        user.setEmail("john@example.com");
        user.setUsername("john");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setTaxResidencyCountry("TN");

        LocalDateTime now = LocalDateTime.now();
        final var expectedDTO = new UserDTO(1L, "john", "john@example.com", "John", "Doe", false, List.of(), now, now, 1L);

        when(createUserMapper.toUser(any(CreateUserRequest.class))).thenReturn(user);
        when(userUseCase.createUser(user)).thenReturn(Mono.just(user));
        when(userMapper.toUserDTO(user)).thenReturn(expectedDTO);

        webTestClient.post()
                .uri("/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED)
                .expectBody(UserDTO.class)
                .isEqualTo(expectedDTO);

        verify(createUserMapper).toUser(request);
        verify(userUseCase).createUser(user);
        verify(userMapper).toUserDTO(user);
    }
}