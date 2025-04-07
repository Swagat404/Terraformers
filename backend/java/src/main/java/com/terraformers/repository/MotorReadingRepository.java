package com.terraformers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.terraformers.model.MotorReading;

@Repository
public interface MotorReadingRepository extends JpaRepository<MotorReading, Integer> {
    // Add custom query methods here later if needed.
}