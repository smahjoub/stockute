package com.smahjoub.stockute.adapters.restful.membership.controller;

import com.smahjoub.stockute.adapters.restful.membership.dto.ChangePasswordRequest;
import com.smahjoub.stockute.adapters.restful.membership.dto.UpdateProfileRequest;
import com.smahjoub.stockute.adapters.restful.membership.dto.UserDTO;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

@ExtendWith(MockitoExtension.class)
class ProfileControllerTest {

    @Mock
    private UserUseCase userUseCase;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ProfileController controller;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToController(controller)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testGetProfile() {
        User user = new User();
        user.setUsername("admin");
        user.setEmail("admin@example.com");
        user.setFirstName("Admin");
        user.setLastName("User");

        LocalDateTime now = LocalDateTime.now();
        UserDTO expectedDTO = new UserDTO(1L, "admin", "admin@example.com", "Admin", "User", false, List.of("USER", "ADMIN"), now, now, 1L);

        when(userUseCase.getUserByUsername(anyString()))
                .thenReturn(Mono.just(user));
        when(userMapper.toUserDTO(user)).thenReturn(expectedDTO);

        webTestClient.mutateWith(mockUser("admin")
                        .authorities(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ADMIN")))
                .get()
                .uri("/v1/profile")
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .isEqualTo(expectedDTO);

        verify(userUseCase).getUserByUsername("admin");
        verify(userMapper).toUserDTO(user);
    }

    @Test
    void testUpdateProfile() {
        UpdateProfileRequest request = new UpdateProfileRequest("new@example.com", "John", "Doe");
        User updatedUser = new User();
        updatedUser.setUsername("admin");
        updatedUser.setEmail("new@example.com");
        updatedUser.setFirstName("John");
        updatedUser.setLastName("Doe");

        LocalDateTime now = LocalDateTime.now();
        UserDTO expectedDTO = new UserDTO(1L, "admin", "new@example.com", "John", "Doe", false, List.of("USER", "ADMIN"), now, now, 1L);

        when(userUseCase.updateProfile(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Mono.just(updatedUser));
        when(userMapper.toUserDTO(updatedUser)).thenReturn(expectedDTO);

        webTestClient.mutateWith(mockUser("admin")
                        .authorities(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ADMIN")))
                .put()
                .uri("/v1/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserDTO.class)
                .isEqualTo(expectedDTO);

        verify(userUseCase).updateProfile("admin", "new@example.com", "John", "Doe");
        verify(userMapper).toUserDTO(updatedUser);
    }

    @Test
    void testChangePassword() {
        ChangePasswordRequest request = new ChangePasswordRequest("oldPassword", "newPassword");

        when(userUseCase.changePassword(anyString(), anyString(), anyString()))
                .thenReturn(Mono.empty());

        webTestClient.mutateWith(mockUser("admin")
                        .authorities(new SimpleGrantedAuthority("USER"), new SimpleGrantedAuthority("ADMIN")))
                .put()
                .uri("/v1/profile/password")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NO_CONTENT.value());

        verify(userUseCase).changePassword("admin", "oldPassword", "newPassword");
    }
}