package com.ai_project.dataart.service;

import com.ai_project.dataart.dto.ChatRoomDto;
import com.ai_project.dataart.entity.ChatRoom;
import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.repository.ChatRoomRepository;
import com.ai_project.dataart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public ChatRoom createRoom(ChatRoomDto dto, User owner) {
        if (chatRoomRepository.existsByName(dto.getName())) {
            throw new RuntimeException("Room name already exists");
        }

        ChatRoom room = new ChatRoom();
        room.setName(dto.getName());
        room.setDescription(dto.getDescription());
        room.setPrivate(dto.isPrivate());
        room.setOwner(owner);
        
        // Власник автоматично стає учасником
        room.getMembers().add(owner);
        
        return chatRoomRepository.save(room);
    }

    public List<ChatRoom> getAllPublicRooms() {
        return chatRoomRepository.findAll().stream()
                .filter(room -> !room.isPrivate())
                .toList();
    }
    @Transactional
    public ChatRoom getOrCreatePrivateChat(User user1, String username2) {
        User user2 = userRepository.findByUsername(username2)
                .orElseThrow(() -> new RuntimeException("Юзера не знайдено"));

        // Шукаємо існуючу приватну кімнату між цими двома
        return chatRoomRepository.findAll().stream()
                .filter(r -> r.isPrivate() && r.getMembers().size() == 2)
                .filter(r -> r.getMembers().contains(user1) && r.getMembers().contains(user2))
                .findFirst()
                .orElseGet(() -> {
                    // Якщо не знайшли — створюємо нову
                    ChatRoom room = new ChatRoom();
                    room.setName("DM_" + user1.getUsername() + "_" + user2.getUsername());
                    room.setPrivate(true);
                    room.setOwner(user1);
                    room.getMembers().add(user1);
                    room.getMembers().add(user2);
                    return chatRoomRepository.save(room);
                });
    }
}