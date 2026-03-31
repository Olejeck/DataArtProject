package com.ai_project.dataart.controller;

import com.ai_project.dataart.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = fileService.saveFile(file);
            // Повертаємо шлях, за яким можна буде відкрити файл
            return ResponseEntity.ok(Map.of("url", "/api/files/download/" + fileName));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Помилка завантаження: " + e.getMessage());
        }
    }
}