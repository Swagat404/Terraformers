package com.terraformers.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import Transactional

import com.terraformers.model.Terrain;
import com.terraformers.repository.TerrainRepository;

// Import a potential custom exception for not found scenarios (Optional)
// You would need to create this exception class elsewhere
// import com.terraformers.exception.ResourceNotFoundException;

@Service // Marks this class as a Spring service component
public class TerrainService {

    // Inject the repository using Spring's dependency injection
    @Autowired
    private TerrainRepository terrainRepository;

    /**
     * Retrieves all Terrain entities.
     * @return List of all Terrains.
     */
    @Transactional(readOnly = true) // Use read-only transaction for query methods
    public List<Terrain> getAllTerrains() {
        return terrainRepository.findAll();
    }

    /**
     * Retrieves a specific Terrain by its ID.
     * @param id The ID of the Terrain to retrieve.
     * @return An Optional containing the Terrain if found, or empty otherwise.
     */
    @Transactional(readOnly = true)
    public Optional<Terrain> getTerrainById(int id) {
        // findById returns Optional<T>
        return terrainRepository.findById(id);
    }

    /**
     * Creates a new Terrain entity.
     * Assumes the ID is either manually set correctly before calling,
     * or the database/JPA is configured for ID generation.
     * @param terrain The Terrain object to create.
     * @return The saved Terrain entity (potentially with generated ID).
     */
    @Transactional // Default transaction (readWrite=true)
    public Terrain createTerrain(Terrain terrain) {
        // Basic validation example (can be expanded)
        if (terrain == null) {
            throw new IllegalArgumentException("Terrain object cannot be null");
        }
        // You might add more validation here (e.g., check if ID is already used if manually setting)
        // Ensure ID is 0 or null if using DB generation, or handle appropriately
        // terrain.setTerrainID(0); // Example if ID is auto-generated

        return terrainRepository.save(terrain);
    }

    /**
     * Updates an existing Terrain entity.
     * Finds the terrain by ID and updates its fields based on the provided details.
     * Does not typically update associated collections directly here; that's often
     * handled by specific add/remove service methods or careful merging.
     * @param id The ID of the Terrain to update.
     * @param terrainDetails A Terrain object containing the new details (ID is ignored, fields are copied).
     * @return The updated Terrain entity.
     * @throws RuntimeException (or a custom ResourceNotFoundException) if the terrain with the given ID is not found.
     */
    @Transactional
    public Terrain updateTerrain(int id, Terrain terrainDetails) {
         // Find the existing entity or throw an exception if not found
         Terrain existingTerrain = terrainRepository.findById(id)
                 // Using a simple RuntimeException here, replace with custom exception if preferred
                 .orElseThrow(() -> new RuntimeException("Terrain not found with id: " + id));
                 // .orElseThrow(() -> new ResourceNotFoundException("Terrain", "id", id)); // Example custom

        // Validate incoming details
        if (terrainDetails == null) {
             throw new IllegalArgumentException("Terrain details cannot be null for update");
        }

         // Update fields from the details object
         existingTerrain.setTerrainType(terrainDetails.getTerrainType());
         existingTerrain.setSurfaceHardness(terrainDetails.getSurfaceHardness());
         existingTerrain.setDescription(terrainDetails.getDescription());
         // NOTE: Updating the 'locations' collection is NOT done by simply setting the list.
         // If the update request needs to add/remove locations, you'd typically:
         // 1. Have separate service methods: addLocationToTerrain(terrainId, locationId), removeLocationFromTerrain(...)
         // 2. Or, process the locations list in terrainDetails: compare with existingTerrain.getLocations(),
         //    call existingTerrain.addLocation() or existingTerrain.removeLocation() as needed. This requires care.

         // Save the updated entity
         return terrainRepository.save(existingTerrain);
    }


    /**
     * Deletes a Terrain entity by its ID.
     * Associated Locations might be deleted depending on CascadeType settings (CascadeType.ALL was set).
     * @param id The ID of the Terrain to delete.
     * @throws org.springframework.dao.EmptyResultDataAccessException if the terrain with the given ID is not found.
     */
    @Transactional
    public void deleteTerrain(int id) {
        // Optional: Check if exists first to provide a clearer custom exception,
        // otherwise deleteById throws EmptyResultDataAccessException if not found.
        // if (!terrainRepository.existsById(id)) {
        //    throw new ResourceNotFoundException("Terrain", "id", id);
        // }
        terrainRepository.deleteById(id);
    }
}