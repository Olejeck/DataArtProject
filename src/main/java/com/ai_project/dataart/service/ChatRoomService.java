package com.ai_project.dataart.service;

import com.ai_project.dataart.dto.ChatRoomDto;
import com.ai_project.dataart.entity.ChatRoom;
import com.ai_project.dataart.entity.User;
import com.ai_project.dataart.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

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
}