package com.ai_project.dataart;// Додай ці імпорти зверху
import com.ai_project.dataart.entity.ChatRoom;
import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.repository.ChatRoomRepository;
import com.ai_project.dataart.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.TimeZone;

@SpringBootApplication
public class DataartApplication {

    public static void main(String[] args) {
        // Це має бути першим рядком
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.setProperty("user.timezone", "UTC");
        SpringApplication.run(DataartApplication.class, args);
    }
}