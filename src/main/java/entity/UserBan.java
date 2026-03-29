package entity;

import jakarta.persistence.*;

@Entity
public class UserBan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User banner; // Хто забанив

    @ManyToOne
    private User bannedUser; // Кого забанили
}
