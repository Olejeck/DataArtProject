package com.ai_project.dataart.service;

import com.ai_project.dataart.entity.Friendship;
import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.repository.FriendshipRepository;
import com.ai_project.dataart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendFriendRequest(User requester, String recipientUsername) {
        User recipient = userRepository.findByUsername(recipientUsername)
                .orElseThrow(() -> new RuntimeException("Користувача не знайдено"));

        if (requester.equals(recipient)) {
            throw new RuntimeException("Ви не можете додати себе у друзі");
        }

        // Перевірка, чи запит вже існує
        if (friendshipRepository.existsByRequesterAndRecipient(requester, recipient) ||
            friendshipRepository.existsByRequesterAndRecipient(recipient, requester)) {
            throw new RuntimeException("Запит вже надіслано або ви вже друзі");
        }

        Friendship friendship = new Friendship();
        friendship.setRequester(requester);
        friendship.setRecipient(recipient);
        friendship.setAccepted(false);
        friendshipRepository.save(friendship);
    }

    @Transactional
    public void acceptFriendRequest(User recipient, String requesterUsername) {
        User requester = userRepository.findByUsername(requesterUsername)
                .orElseThrow(() -> new RuntimeException("Користувача не знайдено"));

        Friendship friendship = friendshipRepository.findByRequesterAndRecipient(requester, recipient)
                .orElseThrow(() -> new RuntimeException("Запит на дружбу не знайдено"));

        friendship.setAccepted(true);
        friendshipRepository.save(friendship);
    }

    public List<User> getFriends(User user) {
        // Отримуємо всіх, де користувач є ініціатором або отримувачем, і статус accepted = true
        List<Friendship> friendships = friendshipRepository.findAllByRequesterOrRecipient(user, user);
        
        return friendships.stream()
                .filter(Friendship::isAccepted)
                .map(f -> f.getRequester().equals(user) ? f.getRecipient() : f.getRequester())
                .collect(Collectors.toList());
    }
}