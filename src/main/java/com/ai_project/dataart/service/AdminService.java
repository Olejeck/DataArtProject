package com.ai_project.dataart.service;

import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.entity.UserBan;
import com.ai_project.dataart.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final UserBanRepository userBanRepository;

    @Transactional
    public void banUserGlobally(String username, String reason, User admin) {
        User target = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Юзера не знайдено"));

        // Перевірка, щоб не забанити двічі
        if (userBanRepository.existsByBannedUserId(target.getId())) {
            throw new RuntimeException("Користувач вже забанений");
        }

        UserBan ban = new UserBan();
        ban.setBannedUser(target);
        ban.setBanner(admin);
        ban.setReason(reason);
        userBanRepository.save(ban);
    }

    // НОВИЙ МЕТОД РОЗБАНУ
    @Transactional
    public void unbanUserGlobally(String username) {
        User target = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Юзера не знайдено"));
        userBanRepository.deleteByBannedUserId(target.getId());
    }

    // НОВИЙ МЕТОД ДЛЯ АДМІНКИ (Повертає статус бану)
    public List<Map<String, Object>> getAllUsersWithBanStatus() {
        return userRepository.findAll().stream().map(u -> {
            // Створюємо мапу явно з типом Object
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", u.getId());
            map.put("username", u.getUsername());
            map.put("email", u.getEmail() != null ? u.getEmail() : "");
            map.put("role", u.getRole().name());
            map.put("isBanned", userBanRepository.existsByBannedUserId(u.getId()));
            return map;
        }).collect(Collectors.toList());
    }
}