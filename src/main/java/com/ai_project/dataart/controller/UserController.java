package com.ai_project.dataart.controller;

import com.ai_project.dataart.dto.UserLoginDto;
import com.ai_project.dataart.dto.UserRegistrationDto;
import com.ai_project.dataart.service.UserService;
import com.ai_project.dataart.entity.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            User user = userService.registerNewUser(registrationDto);
            return ResponseEntity.ok("User registered: " + user.getUsername());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto loginDto) {
        try {
            User user = userService.authenticate(loginDto);
            // Поки що просто повертаємо успіх.
            // Пізніше ми тут будемо створювати сесію або токен.
            return ResponseEntity.ok("Welcome, " + user.getUsername() + "!");
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}