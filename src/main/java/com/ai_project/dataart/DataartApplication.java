package com.ai_project.dataart;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.TimeZone;

@SpringBootApplication
public class DataartApplication {

    @PostConstruct
    public void init() {
        // Примусово встановлюємо UTC для всього додатка
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(DataartApplication.class, args);
    }
}