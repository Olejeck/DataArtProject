package com.ai_project.dataart.repository;

import com.ai_project.dataart.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // Для історії повідомлень (хронологічний порядок) [cite: 164]
    List<Message> findByRoomIdOrderByTimestampAsc(Long roomId);
}