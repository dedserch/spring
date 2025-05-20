package com.serzhputovski.spring.service.impl;

import com.serzhputovski.spring.entity.User;
import com.serzhputovski.spring.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void register_HappyPath() {
        User input = new User();
        input.setUsername("alice");
        input.setPassword("plain");
        User saved = new User();
        saved.setUsername("alice");
        saved.setPassword("encoded");
        when(passwordEncoder.encode("plain")).thenReturn("encoded");
        when(userService.save(input)).thenReturn(saved);
        Authentication token = new UsernamePasswordAuthenticationToken("alice", "plain");
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(token);

        User result = authService.register(input);

        assertSame(saved, result);
        assertEquals("encoded", result.getPassword());
        assertSame(token, SecurityContextHolder.getContext().getAuthentication());
        verify(passwordEncoder).encode("plain");
        verify(userService).save(input);
        verify(authManager).authenticate(any());
    }

    @ParameterizedTest
    @MethodSource("loginCredentials")
    void login_ShouldAuthenticateAndSetContext(String username, String password) {
        Authentication token = new UsernamePasswordAuthenticationToken(username, password);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(token);

        Authentication auth = authService.login(username, password);

        assertSame(token, auth);
        assertSame(token, SecurityContextHolder.getContext().getAuthentication());
        verify(authManager).authenticate(argThat(t ->
                ((UsernamePasswordAuthenticationToken) t).getName().equals(username) &&
                        ((UsernamePasswordAuthenticationToken) t).getCredentials().equals(password)
        ));
    }

    static Stream<Arguments> loginCredentials() {
        return Stream.of(
                Arguments.of("bob", "pwd1"),
                Arguments.of("carol", "pwd2")
        );
    }
}
