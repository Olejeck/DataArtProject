package com.ai_project.dataart.repository;

import com.ai_project.dataart.entity.Friendship;
import com.ai_project.dataart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    @Query("SELECT f FROM Friendship f WHERE (f.requester = :u1 AND f.recipient = :u2) OR (f.requester = :u2 AND f.recipient = :u1)")
    Optional<Friendship> findRelation(User u1, User u2);

    // Список усіх підтверджених друзів
    @Query("SELECT f FROM Friendship f WHERE (f.requester = :user OR f.recipient = :user) AND f.accepted = true")
    List<Friendship> findAcceptedFriends(User user);

    // Список вхідних запитів (де я отримувач і ще не підтвердив)
    List<Friendship> findAllByRecipientAndAcceptedFalse(User user);
}