package com.serzhputovski.spring.service;

import com.serzhputovski.spring.entity.User;
import org.springframework.security.core.Authentication;

public interface AuthService {
    User register (User user);
    Authentication login(String username, String password);
}
