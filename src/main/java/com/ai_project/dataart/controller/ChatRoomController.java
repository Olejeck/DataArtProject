package com.ai_project.dataart.controller;

import com.ai_project.dataart.dto.ChatRoomDto;
import com.ai_project.dataart.entity.ChatRoom;
import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.repository.MessageRepository; // Переконайся, що цей імпорт є
import com.ai_project.dataart.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final MessageRepository messageRepository; // Додаємо репозиторій сюди

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestBody ChatRoomDto dto, @AuthenticationPrincipal User user) {
        try {
            return ResponseEntity.ok(chatRoomService.createRoom(dto, user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/public")
    public ResponseEntity<?> listPublicRooms() {
        return ResponseEntity.ok(chatRoomService.getAllPublicRooms());
    }

    // Цей метод вирішує проблему 404 для історії повідомлень
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getRoomMessages(@PathVariable Long roomId) {
        // Використовуємо вже існуючий метод пошуку за ID кімнати
        return ResponseEntity.ok(messageRepository.findByRoomIdOrderByTimestampAsc(roomId));
    }
    @PostMapping("/private/{username}")
    public ResponseEntity<?> getPrivateRoom(@PathVariable String username, @AuthenticationPrincipal User user) {
        try {
            ChatRoom room = chatRoomService.getOrCreatePrivateChat(user, username);
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}