package com.ai_project.dataart.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;
    private boolean isPrivate;

    @ManyToOne
    private User owner;

    @ManyToMany
    private Set<User> admins; // Адміни можуть видаляти повідомлення та банити [cite: 117-124]

    @ManyToMany
    private Set<User> members;
}
