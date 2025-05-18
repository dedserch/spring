package com.serzhputovski.spring.service;

import com.serzhputovski.spring.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> findAll();

    User save(User user);

    User findByUsername(String username);
}