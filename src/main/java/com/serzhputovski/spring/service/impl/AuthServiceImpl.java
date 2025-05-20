package com.serzhputovski.spring.service.impl;

import com.serzhputovski.spring.entity.User;
import com.serzhputovski.spring.service.AuthService;
import com.serzhputovski.spring.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private static final Logger log = LogManager.getLogger(AuthServiceImpl.class);

    public AuthServiceImpl(PasswordEncoder passwordEncoder,
                           UserService userService,
                           AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User register(User user) {
        log.debug("Registering user {}", user.getUsername());
        String rawPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(rawPassword));
        User newUser = userService.save(user);
        log.info("User registered: {}", newUser.getUsername());

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(newUser.getUsername(), rawPassword);

        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("Authentication set for user {}", newUser.getUsername());

        return newUser;
    }


    @Override
    public Authentication login(String username, String password) {
        log.debug("User {} attempting login", username);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("User {} logged in successfully", username);

        return authentication;
    }
}
