package com.serzhputovski.spring.controller;

import com.serzhputovski.spring.entity.User;
import com.serzhputovski.spring.service.FileStorageService;
import com.serzhputovski.spring.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final FileStorageService storageService;

    public ProfileController(UserService userService, FileStorageService storageService) {
        this.userService = userService;
        this.storageService = storageService;
    }

    @GetMapping
    public String editProfile(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping
    public String updateProfile(@ModelAttribute("user") User form,
                                @RequestParam("avatar") MultipartFile avatar,
                                Principal principal) {
        User user = userService.findByUsername(principal.getName());
        user.setUsername(form.getUsername());
        if (avatar != null && !avatar.isEmpty()) {
            String filename = storageService.store(avatar);
            user.setAvatarUrl(filename);
        }
        userService.save(user);
        return "redirect:/profile?success";
    }

    @GetMapping("/avatar/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveAvatar(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }
}
