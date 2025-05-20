package com.serzhputovski.spring.controller;

import com.serzhputovski.spring.entity.User;
import com.serzhputovski.spring.service.AuthService;
import com.serzhputovski.spring.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/signUp")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/signUp")
    public String processSignUp(@Valid @ModelAttribute("user") User user,
                                BindingResult bindingResult,
                                Model model) {

        if (bindingResult.hasErrors()) return "register";

        if (userService.existsByUsername(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Username taken");
            return "register";
        }

        if (userService.existsByEmail(user.getEmail())) {
            bindingResult.rejectValue("email", "error.user", "Email registered");
            return "register";
        }

        authService.register(user);
        return "redirect:/users";
    }

    @GetMapping("/signIn")
    public String showLoginForm(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) model.addAttribute("errorMessage", "Invalid credentials");
        if (logout != null) model.addAttribute("logoutMessage", "Logged out");
        return "login";
    }
}