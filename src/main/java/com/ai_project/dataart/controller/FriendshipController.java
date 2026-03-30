package com.ai_project.dataart.controller;

import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    // 1. Надіслати запит на дружбу
    @PostMapping("/request/{username}")
    public ResponseEntity<?> sendRequest(@PathVariable String username, @AuthenticationPrincipal User user) {
        try {
            friendshipService.sendFriendRequest(user, username);
            return ResponseEntity.ok("Запит надіслано користувачу " + username);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Прийняти запит на дружбу
    @PostMapping("/accept/{username}")
    public ResponseEntity<?> acceptRequest(@PathVariable String username, @AuthenticationPrincipal User user) {
        try {
            friendshipService.acceptFriendRequest(user, username);
            return ResponseEntity.ok("Тепер ви друзі з " + username);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3. Отримати список підтверджених друзів
    @GetMapping
    public ResponseEntity<List<User>> getMyFriends(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(friendshipService.getFriends(user));
    }

    // 4. Отримати список вхідних запитів (Pending)
    @GetMapping("/pending")
    public ResponseEntity<?> getPendingRequests(@AuthenticationPrincipal User user) {
        // Ми додамо цей метод у сервіс нижче
        return ResponseEntity.ok(friendshipService.getIncomingRequests(user));
    }
}