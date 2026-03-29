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
        // Захист від неавторизованого доступу
        /*if (principal == null) {
            System.out.println("Помилка: Користувач не авторизований!");
            return;
        }*/

        User sender = userRepository.findByUsername(userRepository.findAll().get(0).getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setRoom(room);
        message.setTimestamp(LocalDateTime.now());

        messageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/room." + roomId, message);
    }
}