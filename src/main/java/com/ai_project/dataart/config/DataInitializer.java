package com.ai_project.dataart.config;

import com.ai_project.dataart.entity.ChatRoom;
import com.ai_project.dataart.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public void run(String... args) {
        // Перевіряємо, чи існує загальна кімната (з ID 1 або назвою "Загальний чат")
        if (chatRoomRepository.findById(1L).isEmpty()) {
            ChatRoom generalRoom = new ChatRoom();
            generalRoom.setId(1L); // Примусово ставимо 1 для стабільності фронтенда
            generalRoom.setName("Загальний чат");
            generalRoom.setDescription("Кімната для всіх нових користувачів");
            generalRoom.setPrivate(false);
            chatRoomRepository.save(generalRoom);
            System.out.println(">>>> Створено загальну кімнату з ID: 1");
        }
    }
}