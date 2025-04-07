package com.terraformers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.terraformers.model.AvatarBrain;

@Repository
public interface AvatarBrainRepository extends JpaRepository<AvatarBrain, Integer> {
    // Add custom query methods here later if needed.
}