package com.ai_project.dataart.entity;

import jakarta.persistence.*;
@Entity
public class Friendship {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User requester;

    @ManyToOne
    private User recipient;

    private boolean accepted = false; // Дружба потребує підтвердження
}