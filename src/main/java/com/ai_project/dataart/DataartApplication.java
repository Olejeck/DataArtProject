package com.ai_project.dataart;// Додай ці імпорти зверху
import com.ai_project.dataart.entity.ChatRoom;
import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.repository.ChatRoomRepository;
import com.ai_project.dataart.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DataartApplication {

    public static void main(String[] args) {
        System.setProperty("user.timezone", "UTC");
        SpringApplication.run(DataartApplication.class, args);
    }

    @Bean
    CommandLineRunner initData(ChatRoomRepository roomRepo, UserRepository userRepo) {
        return args -> {
            // Якщо в базі немає кімнат, але є хоча б один юзер — створюємо кімнату
            if (roomRepo.count() == 0 && userRepo.count() > 0) {
                User defaultOwner = userRepo.findAll().get(0);

                ChatRoom testRoom = new ChatRoom();
                testRoom.setName("General");
                testRoom.setDescription("Test Room for Chat");
                testRoom.setOwner(defaultOwner);

                roomRepo.save(testRoom);
                System.out.println(">>> Тестова кімната створена з ID: " + testRoom.getId());
            } else if (userRepo.count() == 0) {
                System.out.println(">>> УВАГА: Спочатку зареєструй користувача через Postman!");
            }
        };
    }
}