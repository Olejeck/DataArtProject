package com.ai_project.dataart.repository;

import com.ai_project.dataart.entity.UserBan;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBanRepository extends JpaRepository<UserBan, Long> {
    // Перевірка, чи banner забанив bannedUser
    boolean existsByBannedUserId(Long bannedUserId);
    @Transactional
    void deleteByBannedUserId(Long bannedUserId);
}