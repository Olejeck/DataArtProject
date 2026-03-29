package Entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Column(unique = true, updatable = false) // Незмінний [cite: 26]
    private String username;

    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;

    @NotBlank
    private String password; // Будемо зберігати хеш
}
