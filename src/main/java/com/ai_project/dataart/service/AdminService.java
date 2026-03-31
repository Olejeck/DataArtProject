package com.ai_project.dataart.service;

import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.entity.UserBan;
import com.ai_project.dataart.repository.ChatRoomRepository;
import com.ai_project.dataart.repository.RoomBanRepository;
import com.ai_project.dataart.repository.UserBanRepository;
import com.ai_project.dataart.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;
    private final UserBanRepository userBanRepository;
    private final RoomBanRepository roomBanRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void banUserGlobally(String username, String reason, User admin) {
        User target = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Юзера не знайдено"));
        
        UserBan ban = new UserBan();
        ban.setBannedUser(target);
        ban.setBanner(admin);
        ban.setReason(reason);
        userBanRepository.save(ban);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}