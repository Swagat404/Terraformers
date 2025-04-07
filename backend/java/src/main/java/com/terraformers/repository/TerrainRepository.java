package com.terraformers.repository;

import com.terraformers.model.Terrain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerrainRepository extends JpaRepository<Terrain, Integer> {
    // Add custom query methods here later if needed.
}