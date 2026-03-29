package com.ai_project.dataart;

import com.ai_project.dataart.entity.ChatRoom;
import com.ai_project.dataart.repository.ChatRoomRepository;
import com.ai_project.dataart.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DataartApplication {

    public static void main(String[] args) {
        // Встановлюємо системну властивість ДО запуску Spring
        System.setProperty("user.timezone", "UTC");
        SpringApplication.run(DataartApplication.class, args);
    }
    @Bean
    CommandLineRunner initData(ChatRoomRepository repository, UserRepository userRepo) {
        return args -> {
            if (repository.count() == 0 && userRepo.count() > 0) {
                ChatRoom room = new ChatRoom();
                room.setName("Test Room");
                room.setOwner(userRepo.findAll().get(0));
                repository.save(room);
                System.out.println("Test room created with ID: " + room.getId());
            }
        };
    }
}