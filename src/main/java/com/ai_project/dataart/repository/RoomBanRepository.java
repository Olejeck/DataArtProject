package com.ai_project.dataart.repository;

import com.ai_project.dataart.entity.RoomBan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomBanRepository extends JpaRepository<RoomBan, Long> {
    boolean existsByRoomIdAndBannedUserId(Long roomId, Long bannedUserId);
}