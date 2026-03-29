package com.ai_project.dataart.repository;

import com.ai_project.dataart.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    // Перевірка, чи існує запит між двома людьми
    boolean existsByRequesterIdAndRecipientId(Long requesterId, Long recipientId);
}