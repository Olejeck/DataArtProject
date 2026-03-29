package com.ai_project.dataart.repository;

import com.ai_project.dataart.entity.UserBan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBanRepository extends JpaRepository<UserBan, Long> {
    // Перевірка, чи banner забанив bannedUser
    boolean existsByBannerIdAndBannedUserId(Long bannerId, Long bannedUserId);
}