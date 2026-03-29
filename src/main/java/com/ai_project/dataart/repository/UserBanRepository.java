package com.ai_project.dataart.repository;


import com.ai_project.dataart.entity.UserBan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserBanRepository extends JpaRepository<UserBan, Long> {
    Optional<UserBan> findByName(String name);
    boolean existsByName(String name);
}