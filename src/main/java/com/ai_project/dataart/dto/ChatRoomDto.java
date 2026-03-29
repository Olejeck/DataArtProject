package com.ai_project.dataart.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ChatRoomDto {
    @NotBlank(message = "Room name is required")
    private String name;

    private String description;

    private boolean isPrivate; // true для приватних кімнат
}
