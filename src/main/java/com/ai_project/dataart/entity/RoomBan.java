package com.ai_project.dataart.entity;
import jakarta.persistence.*;
import lombok.Data;

@Data

@Entity
public class RoomBan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatRoom room;

    @ManyToOne
    private User bannedUser;

    @ManyToOne
    private User bannedBy; // Потрібно знати, який саме адмін забанив [cite: 122]
}
