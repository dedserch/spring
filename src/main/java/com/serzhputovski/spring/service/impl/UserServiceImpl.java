package com.serzhputovski.spring.service.impl;

import com.serzhputovski.spring.entity.Role;
import com.serzhputovski.spring.entity.User;
import com.serzhputovski.spring.exception.ResourceNotFoundException;
import com.serzhputovski.spring.repository.UserRepository;
import com.serzhputovski.spring.service.RoleService;
import com.serzhputovski.spring.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public List<User> findAll() {
        log.debug("Retrieving all users");
        List<User> users = userRepository.findAll();
        log.info("Found {} users", users.size());
        return users;
    }

    @Override
    @Transactional
    public User save(User user) {
        log.debug("Saving user {}", user.getUsername());
        Role role = roleService.findByRoleName("ROLE_USER");
        user.addRole(role);
        User saved = userRepository.save(user);
        log.info("User saved: {}", saved.getUsername());
        return saved;
    }

    @Override
    public User findByUsername(String username) {
        log.debug("Finding user by username {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    String msg = "User not found: " + username;
                    log.warn(msg);
                    return new ResourceNotFoundException(msg);
                });
        log.info("User found: {}", username);
        return user;
    }

    @Override
    public boolean existsByUsername(String username) {
        log.debug("Checking existsByUsername {}", username);
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.debug("Checking existsByEmail {}", email);
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading UserDetails for {}", username);
        User user = findByUsername(username);
        UserDetails details = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );
        log.info("UserDetails loaded for {}", username);
        return details;
    }
}