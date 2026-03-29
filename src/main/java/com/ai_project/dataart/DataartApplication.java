package com.ai_project.dataart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DataartApplication {

    public static void main(String[] args) {
        // Встановлюємо системну властивість ДО запуску Spring
        System.setProperty("user.timezone", "UTC");
        SpringApplication.run(DataartApplication.class, args);
    }
}