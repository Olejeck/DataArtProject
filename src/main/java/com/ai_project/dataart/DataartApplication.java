package com.ai_project.dataart;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.TimeZone;

public static void main(String[] args) {
    // Встановлюємо системну властивість ДО запуску Spring
    System.setProperty("user.timezone", "UTC");
    SpringApplication.run(DataartApplication.class, args);
}