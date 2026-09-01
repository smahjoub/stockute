package com.smahjoub.stockute.application.service.membership;

import com.smahjoub.stockute.application.port.membership.out.UserInRolePort;
import com.smahjoub.stockute.application.port.membership.out.UserPort;
import com.smahjoub.stockute.application.service.membership.utils.PBKDF2Encoder;
import com.smahjoub.stockute.domain.model.Role;
import com.smahjoub.stockute.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserPort userPort;
    private UserInRolePort userInRolePort;
    private PBKDF2Encoder encoder;
    private UserService service;

    @BeforeEach
    void setup() {
        userPort = mock(UserPort.class);
        userInRolePort = mock(UserInRolePort.class);
        encoder = mock(PBKDF2Encoder.class);
        service = new UserService(userPort, userInRolePort, encoder);
    }

    @Test
    void authenticate_whenPasswordMatches_returnsUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@mail.com");
        user.setPassword("encoded");

        Role r = new Role();
        r.setName("ADMIN");

        when(userPort.findByEmail("test@mail.com"))
                .thenReturn(Mono.just(user));

        when(userInRolePort.findRolesByUserName("testuser"))
                .thenReturn(Mono.just(List.of(r)));

        when(encoder.encode("raw")).thenReturn("encoded");

        StepVerifier.create(service.authenticate("test@mail.com", "raw"))
                .expectNextMatches(u ->
                        u.getEmail().equals("test@mail.com") &&
                                u.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"))
                )
                .verifyComplete();

        verify(userPort, times(2)).findByEmail("test@mail.com");
        verify(userInRolePort).findRolesByUserName("testuser");
        verify(encoder).encode("raw");
    }

    @Test
    void authenticate_whenPasswordDoesNotMatch_returnsEmpty() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@mail.com");
        user.setPassword("encoded");

        Role r = new Role();
        r.setName("USER");

        when(userPort.findByEmail("test@mail.com"))
                .thenReturn(Mono.just(user));

        when(userInRolePort.findRolesByUserName("testuser"))
                .thenReturn(Mono.just(List.of(r)));

        when(encoder.encode("wrong")).thenReturn("not-matching");

        StepVerifier.create(service.authenticate("test@mail.com", "wrong"))
                .verifyComplete();

        verify(userPort, times(2)).findByEmail("test@mail.com");
        verify(userInRolePort).findRolesByUserName("testuser");
        verify(encoder).encode("wrong");
    }


    @Test
    void getUserByUsername_delegatesToPort() {
        User user = new User();
        user.setUsername("john");

        when(userPort.findByUsername("john"))
                .thenReturn(Mono.just(user));

        StepVerifier.create(service.getUserByUsername("john"))
                .expectNext(user)
                .verifyComplete();

        verify(userPort).findByUsername("john");
    }

    @Test
    void createUser_encodesPassword_savesAndAssignsRole() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("raw");
        user.setEmail("john@example.com");

        Role userRole = new Role();
        userRole.setId(200L);
        userRole.setName("USER");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("john");
        savedUser.setPassword("encoded");
        savedUser.setEmail("john@example.com");

        when(encoder.encode("raw")).thenReturn("encoded");
        when(userPort.save(any(User.class))).thenReturn(Mono.just(savedUser));
        when(userInRolePort.findRoleByName("USER")).thenReturn(Mono.just(userRole));
        when(userInRolePort.assignRoleToUser(200L, 1L)).thenReturn(Mono.empty());
        when(userInRolePort.findRolesByUserName("john")).thenReturn(Mono.just(List.of(userRole)));

        StepVerifier.create(service.createUser(user))
                .expectNextMatches(u -> 
                    u.getId().equals(1L) && 
                    u.getUsername().equals("john") &&
                    u.getRoles().size() == 1 &&
                    u.getRoles().iterator().next().getName().equals("USER")
                )
                .verifyComplete();

        verify(encoder).encode("raw");
        verify(userPort).save(user);
        verify(userInRolePort).findRoleByName("USER");
        verify(userInRolePort).assignRoleToUser(200L, 1L);
        verify(userInRolePort).findRolesByUserName("john");
    }
}
