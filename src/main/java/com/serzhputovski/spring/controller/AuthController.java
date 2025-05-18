package com.serzhputovski.spring.controller;

import com.serzhputovski.spring.entity.User;
import com.serzhputovski.spring.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/signUp")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/signUp")
    public String processSignUp(@ModelAttribute("user") User user) {
        authService.register(user);
        return "redirect:/users";
    }

    @GetMapping("/signIn")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username or password");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out");
        }
        return "login";
    }
}
