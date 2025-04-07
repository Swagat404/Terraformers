package com.terraformers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.terraformers.model.Motor;

@Repository
public interface MotorRepository extends JpaRepository<Motor, Integer> {
    // Add custom query methods here later if needed.
}