package com.ai_project.dataart.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    // Папка, де будуть лежати файли (створи її в корені проекту)
    private final String uploadDir = "uploads/";

    public String saveFile(MultipartFile file) throws IOException {
        // Створюємо папку, якщо її немає
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Генеруємо унікальне ім'я, щоб уникнути конфліктів
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Копіюємо файл
        Files.copy(file.getInputStream(), filePath);

        return fileName; // Повертаємо ім'я файлу
    }
}
