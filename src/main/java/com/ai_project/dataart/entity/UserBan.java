package com.ai_project.dataart.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class UserBan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User banner; // Хто забанив

    @ManyToOne
    private User bannedUser; // Кого забанили
}
