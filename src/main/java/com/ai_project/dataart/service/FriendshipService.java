package com.ai_project.dataart.service;

import com.ai_project.dataart.entity.Friendship;
import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.repository.FriendshipRepository;
import com.ai_project.dataart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
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

        // ПРАВИЛЬНО: Використовуємо isPresent() для Optional
        if (friendshipRepository.findRelation(requester, recipient).isPresent()) {
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

        // Шукаємо існуючий запит, де поточний юзер є отримувачем
        Friendship friendship = friendshipRepository.findRelation(requester, recipient)
                .orElseThrow(() -> new RuntimeException("Запит на дружбу не знайдено"));

        if (friendship.isAccepted()) {
            throw new RuntimeException("Ви вже друзі");
        }

        friendship.setAccepted(true);
        friendshipRepository.save(friendship);
    }

    public List<User> getFriends(User user) {
        // ПРАВИЛЬНО: Викликаємо метод, який ми створили в репозиторії
        List<Friendship> friendships = friendshipRepository.findAcceptedFriends(user);

        return friendships.stream()
                .map(f -> f.getRequester().equals(user) ? f.getRecipient() : f.getRequester())
                .collect(Collectors.toList());
    }

    public List<User> getIncomingRequests(User user) {
        return friendshipRepository.findAllByRecipientAndAcceptedFalse(user)
                .stream()
                .map(Friendship::getRequester)
                .collect(Collectors.toList());
    }
}