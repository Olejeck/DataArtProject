package com.ai_project.dataart.controller;

import com.ai_project.dataart.dto.ChatRoomDto;
import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.repository.MessageRepository;
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
    // У ChatRoomController.java
    private final MessageRepository messageRepository;

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<?> getRoomMessages(@PathVariable Long roomId) {
        // Повертаємо історію повідомлень у хронологічному порядку [cite: 164]
        return ResponseEntity.ok(messageRepository.findByRoomIdOrderByTimestampAsc(roomId));
    }
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
}