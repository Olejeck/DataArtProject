package com.ai_project.dataart.controller;

import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // Отримати список юзерів
    @GetMapping("/users")
    public ResponseEntity<?> listAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsersWithBanStatus());
    }

    // Забанити
    @PostMapping("/ban/{username}")
    public ResponseEntity<?> banUser(@PathVariable String username, @RequestBody(required = false) String reason, @AuthenticationPrincipal User admin) {
        try {
            adminService.banUserGlobally(username, reason != null ? reason : "Порушення правил", admin);
            return ResponseEntity.ok("Користувача " + username + " забанено");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // РОЗБАНИТИ
    @PostMapping("/unban/{username}")
    public ResponseEntity<?> unbanUser(@PathVariable String username) {
        try {
            adminService.unbanUserGlobally(username);
            return ResponseEntity.ok("Користувача " + username + " розбанено");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}