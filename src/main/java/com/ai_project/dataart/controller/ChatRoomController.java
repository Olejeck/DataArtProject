package com.ai_project.dataart.controller;

import com.ai_project.dataart.dto.ChatRoomDto;
import com.ai_project.dataart.entity.ChatRoom;
import com.ai_project.dataart.entity.Message;
import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.repository.MessageRepository;
import com.ai_project.dataart.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final MessageRepository messageRepository;

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

    @PostMapping("/private/{username}")
    public ResponseEntity<?> getPrivateRoom(@PathVariable String username, @AuthenticationPrincipal User user) {
        try {
            ChatRoom room = chatRoomService.getOrCreatePrivateChat(user, username);
            return ResponseEntity.ok(room);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ЗАЛИШАЄМО ТІЛЬКИ ЦЕЙ МЕТОД ДЛЯ ПОВІДОМЛЕНЬ
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long roomId, @AuthenticationPrincipal User user) {
        ChatRoom room = chatRoomService.getRoomById(roomId);

        // Якщо кімната приватна, користувач МАЄ бути її учасником
        if (room.isPrivate() && !room.getMembers().contains(user)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(messageRepository.findByRoomIdOrderByTimestampAsc(room.getId()));
    }
}