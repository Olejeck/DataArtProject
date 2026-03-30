package com.ai_project.dataart.controller;

import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friends")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/request/{username}")
    public ResponseEntity<?> sendRequest(@PathVariable String username, @AuthenticationPrincipal User user) {
        try {
            friendshipService.sendFriendRequest(user, username);
            return ResponseEntity.ok("Запит надіслано користувачу " + username);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/accept/{username}")
    public ResponseEntity<?> acceptRequest(@PathVariable String username, @AuthenticationPrincipal User user) {
        try {
            friendshipService.acceptFriendRequest(user, username);
            return ResponseEntity.ok("Тепер ви друзі з " + username);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> myFriends(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(friendshipService.getFriends(user));
    }
}