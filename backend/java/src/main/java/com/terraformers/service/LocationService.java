package com.terraformers.service;

import java.util.List;
import java.util.Optional; // Needed for linking
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired; // Inject if needed
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terraformers.model.Location;
import com.terraformers.model.Terrain;
import com.terraformers.repository.LocationRepository;
import com.terraformers.repository.TerrainRepository;

// Import custom exception (create this class if desired)
// import com.terraformers.exception.ResourceNotFoundException;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired // Inject Terrain repo to find Terrain when creating/updating Location
    private TerrainRepository terrainRepository;

    // Inject AvatarRepository if service needs to check for existing avatar at location
    // @Autowired
    // private AvatarRepository avatarRepository;

    /**
     * Retrieves all Location entities.
     * @return List of all Locations.
     */
    @Transactional(readOnly = true)
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    /**
     * Retrieves a specific Location by its ID.
     * @param id The ID of the Location to retrieve.
     * @return An Optional containing the Location if found, or empty otherwise.
     */
    @Transactional(readOnly = true)
    public Optional<Location> getLocationById(int id) {
        return locationRepository.findById(id);
    }

    /**
     * Creates a new Location, ensuring it's linked to a valid Terrain.
     * Assumes terrainId is provided to link the location.
     * @param locationDetails Location object potentially containing attributes like lat, lon etc. ID might be ignored if generated.
     * @param terrainId The ID of the mandatory terrain this location belongs to.
     * @return The saved Location entity.
     * @throws RuntimeException if the specified Terrain ID is not found.
     * @throws IllegalArgumentException if locationDetails or terrainId is invalid.
     */
    @Transactional
    public Location createLocation(Location locationDetails, int terrainId) {
        if (locationDetails == null) {
            throw new IllegalArgumentException("Location details cannot be null");
        }

        // Find the associated Terrain - throws exception if not found
        Terrain terrain = terrainRepository.findById(terrainId)
                .orElseThrow(() -> new RuntimeException("Terrain not found with id: " + terrainId + " while creating location"));
                // .orElseThrow(() -> new ResourceNotFoundException("Terrain", "id", terrainId)); // Using custom exception

        // Create a new Location entity
        Location newLocation = new Location();
        // newLocation.setLocationID(0); // If using DB generation @GeneratedValue

        // Copy properties from details
        newLocation.setLongitude(locationDetails.getLongitude());
        newLocation.setLatitude(locationDetails.getLatitude());
        newLocation.setAltitude(locationDetails.getAltitude());
        newLocation.setSlope(locationDetails.getSlope());
        // If using manual IDs, set it from details:
        newLocation.setLocationID(locationDetails.getLocationID());

        // Set the mandatory Terrain association using the JPA-aware setter
        // This should also add the location to terrain.getLocations() via the helper method
        newLocation.setTerrain(terrain);

        // Avatar is initially null when creating a location
        newLocation.setAvatar(null);

        return locationRepository.save(newLocation);
    }

    /**
     * Updates an existing Location entity.
     * Allows changing attributes and potentially the associated Terrain.
     * @param id The ID of the Location to update.
     * @param locationDetails Location object containing new details.
     * @param newTerrainId Optional ID of a new terrain to associate with. If null or matches current, terrain isn't changed.
     * @return The updated Location entity.
     * @throws RuntimeException if the location or newTerrainId (if provided) is not found.
     */
    @Transactional
    public Location updateLocation(int id, Location locationDetails, Integer newTerrainId) {
         // Find existing or throw exception
         Location existingLocation = locationRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
                 // .orElseThrow(() -> new ResourceNotFoundException("Location", "id", id));

         if (locationDetails == null) {
             throw new IllegalArgumentException("Location details cannot be null for update");
         }

         // Update basic attributes
         existingLocation.setLongitude(locationDetails.getLongitude());
         existingLocation.setLatitude(locationDetails.getLatitude());
         existingLocation.setAltitude(locationDetails.getAltitude());
         existingLocation.setSlope(locationDetails.getSlope());

         // Optionally update Terrain association if newTerrainId is provided AND different
         if (newTerrainId != null && (existingLocation.getTerrain() == null || existingLocation.getTerrain().getTerrainID() != newTerrainId)) {
             Terrain newTerrain = terrainRepository.findById(newTerrainId)
                     .orElseThrow(() -> new RuntimeException("New Terrain not found with id: " + newTerrainId + " while updating location"));
                     // .orElseThrow(() -> new ResourceNotFoundException("Terrain", "id", newTerrainId));

             // Use the JPA-aware setter to handle bidirectional links
             existingLocation.setTerrain(newTerrain);
         }

         // Note: Updating Avatar link is typically handled from AvatarService side.
         // Avoid setting existingLocation.setAvatar(...) here unless specifically intended by the update operation.

         return locationRepository.save(existingLocation);
    }


    /**
     * Deletes a Location entity by its ID.
     * IMPORTANT: Consider business logic - should deletion be allowed if an Avatar is present?
     * @param id The ID of the Location to delete.
     * @throws RuntimeException if location has an avatar (example constraint).
     * @throws org.springframework.dao.EmptyResultDataAccessException if the location is not found.
     */
    @Transactional
    public void deleteLocation(int id) {
        // Optional: Add business rule check before deleting
        Location location = locationRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Location not found with id: " + id)); // Or use EmptyResultDataAccessException from repo call

        if (location.getAvatar() != null) {
            throw new IllegalStateException("Cannot delete location " + id + " as it is currently occupied by Avatar " + location.getAvatar().getAvatarID());
            // Alternatively, you could automatically unset the Avatar's location:
            // location.getAvatar().setLocation(null); // Requires AvatarService/Repo potentially
            // locationRepository.save(location); // Save the change on Avatar if needed before deleting location
        }

        // If checks pass, proceed with deletion
        locationRepository.deleteById(id);
        // Link from Terrain (locations list) is handled by JPA based on Terrain's
        // @OneToMany(orphanRemoval=true) setting or requires manual update on Terrain before save.
        // Since Location owns the @ManyToOne to Terrain, deleting Location just removes the row,
        // Terrain's list will update on next load/refresh if managed correctly.
    }

     /**
      * Finds locations associated with a specific terrain ID.
      * @param terrainId The ID of the terrain.
      * @return List of Locations for that terrain.
      * @throws RuntimeException if terrain not found
      */
     @Transactional(readOnly = true)
     public List<Location> getLocationsByTerrainId(int terrainId) {
         // Check if terrain exists first for better error message
         if (!terrainRepository.existsById(terrainId)) {
              throw new RuntimeException("Terrain not found with id: " + terrainId);
              // throw new ResourceNotFoundException("Terrain", "id", terrainId);
         }
         // Could implement this query directly in LocationRepository for efficiency:
         // List<Location> findByTerrain_TerrainID(int terrainId);
         // return locationRepository.findByTerrain_TerrainID(terrainId);

         // Manual filtering alternative (less efficient if many locations):
         return locationRepository.findAll().stream()
                 .filter(loc -> loc.getTerrain() != null && loc.getTerrain().getTerrainID() == terrainId)
                 .collect(Collectors.toList());
     }
}