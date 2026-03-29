package entity;
import jakarta.persistence.*;
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
