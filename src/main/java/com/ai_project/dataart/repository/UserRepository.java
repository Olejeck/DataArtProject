package com.ai_project.dataart.repository;

import com.ai_project.dataart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository<Тип сутності, Тип її ID>
public interface UserRepository extends JpaRepository<User, Long> {
    // Spring сам зробить запит: SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);

    // Перевірка унікальності перед реєстрацією
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}