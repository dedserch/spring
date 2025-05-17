package com.serzhputovski.spring.service.impl;

import com.serzhputovski.spring.entity.Role;
import com.serzhputovski.spring.entity.User;
import com.serzhputovski.spring.exception.ResourceNotFoundException;
import com.serzhputovski.spring.repository.UserRepository;
import com.serzhputovski.spring.service.RoleService;
import com.serzhputovski.spring.service.UserService;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public List<User> findAll() {
        log.info("Find all users");

        var users = this.userRepository.findAll();

        log.info("Found {} users", users.size());
        return users;
    }

    @Override
    @Transactional
    public User save(User user) {
        log.debug("Attempting to save user {}", user);
        var role = this.roleService.findByRoleName("ROLE_USER");
        user.addRole(role);
        var newUser =  this.userRepository.save(user);
        log.info("Saved user {}", newUser);
        return newUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Attempting to load user {}", username);

        var user = this.userRepository.findByUsername(username).orElseThrow((
                () -> {
                    var message = "User with username: '%s' was not fouund".formatted(username);
                    log.warn(message);
                    return new ResourceNotFoundException(message);
                }
        ));

        log.info("Loaded user {}", user);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(Role::getName)
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );
    }
}
