package com.ai_project.dataart.controller;

import com.ai_project.dataart.entity.ChatRoom;
import com.ai_project.dataart.entity.Message;
import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.repository.ChatRoomRepository;
import com.ai_project.dataart.repository.MessageRepository;
import com.ai_project.dataart.repository.RoomBanRepository;
import com.ai_project.dataart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
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
    private final RoomBanRepository roomBanRepository;

    @MessageMapping("/chat/{roomId}")
    public void processMessage(@DestinationVariable Long roomId, @Payload String content, Principal principal) {
        // 1. Перевірка авторизації
        if (principal == null) {
            throw new RuntimeException("Помилка: Користувач не авторизований!");
        }

        // 2. Отримання реального відправника
        User sender = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Відправника не знайдено"));

        // 3. Отримання конкретної кімнати за ID
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Кімнату не знайдено з ID: " + roomId));

        // TODO: Додати перевірку доступу (чи не забанений юзер, чи є мембером приватної кімнати)
        if (roomBanRepository.existsByRoomIdAndBannedUserId(roomId, sender.getId())) {
            throw new RuntimeException("Помилка: Користувач забанений!");
        }
        // 4. Формування та збереження повідомлення
        Message message = new Message();
        message.setContent(content);
        message.setSender(sender);
        message.setRoom(room);
        message.setTimestamp(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);

        // 5. Відправка повідомлення підписникам кімнати
        messagingTemplate.convertAndSend("/topic/room." + roomId, savedMessage);
    }
}