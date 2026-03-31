package com.ai_project.dataart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet; // Додай цей імпорт
import java.util.Set;

@Data
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

    @JsonIgnore
    @ManyToMany
    private Set<User> admins = new HashSet<>(); // Ініціалізуємо порожнім HashSet

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<User> members = new HashSet<>(); // Ініціалізуємо порожнім HashSet
}