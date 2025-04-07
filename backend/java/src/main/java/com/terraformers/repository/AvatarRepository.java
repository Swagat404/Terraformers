package com.terraformers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.terraformers.model.Avatar;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Integer> {
    // Add custom query methods here later if needed.
}