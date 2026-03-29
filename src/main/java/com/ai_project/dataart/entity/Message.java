package com.ai_project.dataart.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 3072) // Ліміт 3 KB
    @NotBlank
    private String content;

    private LocalDateTime timestamp = LocalDateTime.now();
    private boolean edited = false; // Для індикатора "edited" [cite: 157]

    @ManyToOne
    private User sender;
    @ManyToOne
    private ChatRoom room; // Це поле вказує, в якій кімнаті або діалозі знаходиться повідомлення
}
