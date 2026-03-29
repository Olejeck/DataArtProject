package com.ai_project.dataart.controller;

import com.ai_project.dataart.entity.Message;
import com.ai_project.dataart.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;

    @MessageMapping("/chat/{roomId}")
    public void processMessage(@DestinationVariable Long roomId, @Payload String content) {
        // 1. Тут пізніше додамо збереження в БД (Message entity)
        // 2. Відправляємо повідомлення всім, хто підписаний на цю кімнату
        messagingTemplate.convertAndSend("/topic/room." + roomId, content);
    }
}