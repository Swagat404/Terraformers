package com.terraformers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.terraformers.model.AvatarLog;

@Repository
public interface AvatarLogRepository extends JpaRepository<AvatarLog, Integer> {
    // Add custom query methods here later if needed.
}