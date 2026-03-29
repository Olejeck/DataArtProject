package com.ai_project.dataart.repository;

import com.ai_project.dataart.entity.RoomBan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoomBanRepository extends JpaRepository<RoomBan, Long> {
    Optional<RoomBan> findByName(String name);
    boolean existsByName(String name);
}