package com.ai_project.dataart.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User implements org.springframework.security.core.userdetails.UserDetails {
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

    private boolean isOnline = false;

    // src/main/java/com/ai_project/dataart/entity/User.java

    public enum Role { USER, ADMIN }

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // За замовчуванням всі - юзери

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }
    @Override public int hashCode() {
        return id.hashCode();
    }
}
