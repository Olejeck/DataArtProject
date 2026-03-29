package com.ai_project.dataart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

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
    private Set<User> admins; // Адміни можуть видаляти повідомлення та банити [cite: 117-124]
    @JsonIgnore
    @ManyToMany
    private Set<User> members;
}
