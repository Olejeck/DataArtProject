package com.ai_project.dataart.repository;

import com.ai_project.dataart.entity.Friendship;
import com.ai_project.dataart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    // Перевірка, чи існує запит між двома людьми
    boolean existsByRequesterAndRecipient(User requester, User recipient);
    Optional<Friendship> findByRequesterAndRecipient(User requester, User recipient);
    List<Friendship> findAllByRequesterOrRecipient(User requester, User recipient);
}