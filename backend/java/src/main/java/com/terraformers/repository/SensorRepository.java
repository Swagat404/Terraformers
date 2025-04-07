package com.terraformers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.terraformers.model.Sensor;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Integer> {
    // Add custom query methods here later if needed.
}