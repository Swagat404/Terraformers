package com.terraformers.repository;

import com.terraformers.model.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Integer> {
    // Add custom query methods here later if needed.
}