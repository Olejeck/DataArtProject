package com.ai_project.dataart.controller;

import com.ai_project.dataart.entity.ChatRoom;
import com.ai_project.dataart.entity.Message;
import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RoomBanRepository roomBanRepository;
    private final UserBanRepository userBanRepository;

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

        // 1. ПЕРЕВІРКА ГЛОБАЛЬНОГО БАНУ (на весь месенджер)
        // Якщо у тебе є UserBanRepository, перевіряємо тут
        if (userBanRepository.existsByBannedUserId(sender.getId())) {
            throw new RuntimeException("Ви забанені цілком і повністю!");
        }

        // 2. ПЕРЕВІРКА БАНУ В КОНКРЕТНІЙ КІМНАТІ (твій код)
        if (roomBanRepository.existsByRoomIdAndBannedUserId(roomId, sender.getId())) {
            throw new RuntimeException("Ви забанені в цій кімнаті!");
        }

        // 3. ПЕРЕВІРКА ДОСТУПУ ДО ПРИВАТНОЇ КІМНАТИ
        if (room.isPrivate()) {
            // Перевіряємо, чи є юзер у списку members
            // Примітка: для цього members має бути завантажено (Eager або через Join)
            boolean isMember = room.getMembers().contains(sender);
            if (!isMember) {
                throw new RuntimeException("У вас немає доступу до цієї приватної кімнати!");
            }
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
        room.getMembers().forEach(member -> {
            if (!member.getUsername().equals(sender.getUsername())) {
                // Відправляємо сповіщення кожному учаснику особисто
                messagingTemplate.convertAndSendToUser(
                        member.getUsername(),
                        "/queue/notifications",
                        Map.of("roomId", roomId, "sender", sender.getUsername())
                );
            }
        });
    }
}