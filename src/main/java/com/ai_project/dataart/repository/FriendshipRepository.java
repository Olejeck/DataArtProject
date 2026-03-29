package com.ai_project.dataart.repository;

import com.ai_project.dataart.entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    Optional<Friendship> findByName(String name);
    boolean existsByName(String name);
}