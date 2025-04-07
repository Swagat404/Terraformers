package com.terraformers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.terraformers.model.MissionLog;

@Repository
public interface MissionLogRepository extends JpaRepository<MissionLog, Integer> {
    // Add custom query methods here later if needed.
}