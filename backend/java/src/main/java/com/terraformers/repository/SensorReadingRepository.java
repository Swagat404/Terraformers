package com.terraformers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.terraformers.model.SensorReading;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Integer> {
    // Add custom query methods here later if needed.
}