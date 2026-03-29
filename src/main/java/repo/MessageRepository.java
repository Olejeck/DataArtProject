package repo;

import Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // Для історії повідомлень (хронологічний порядок) [cite: 164]
    List<Message> findByRoomIdOrderByTimestampAsc(Long roomId);
}