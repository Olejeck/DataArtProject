package com.ai_project.dataart.controller;

import com.ai_project.dataart.dto.UserLoginDto;
import com.ai_project.dataart.dto.UserRegistrationDto;
import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.service.JwtService;
import com.ai_project.dataart.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtService jwtService; // Інжектимо наш сервіс

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
            // Аутентифікуємо користувача
            User user = userService.authenticate(loginDto);

            // Генеруємо токен
            String jwtToken = jwtService.generateToken(user);

            // Повертаємо токен у вигляді JSON: { "token": "ey..." }
            return ResponseEntity.ok(Map.of("token", jwtToken));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}