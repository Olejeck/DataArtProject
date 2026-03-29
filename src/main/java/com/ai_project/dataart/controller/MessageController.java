package com.ai_project.dataart.controller;

import com.ai_project.dataart.entity.ChatRoom;
import com.ai_project.dataart.entity.Message;
import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.repository.ChatRoomRepository;
import com.ai_project.dataart.repository.MessageRepository;
import com.ai_project.dataart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/{roomId}")
    public void processMessage(@DestinationVariable Long roomId, @Payload String content, Principal principal) {
        // 1. Знаходимо користувача за його ім'ям (Principal автоматично заповнюється Spring Security)
        User sender = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Знаходимо кімнату
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // 3. Створюємо та зберігаємо повідомлення
        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setRoom(room);
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);

        // 4. Розсилаємо збережене повідомлення всім учасникам
        messagingTemplate.convertAndSend("/topic/room." + roomId, message);
    }
}