package com.terraformers.controller;

import com.terraformers.dto.TerrainDTO;
import com.terraformers.model.Terrain;
import com.terraformers.service.TerrainService;
// Import custom exception if you created one
// import com.terraformers.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // Good for simple error responses

import java.util.List;
import java.util.stream.Collectors;

@RestController // Marks this as a REST controller (includes @Controller and @ResponseBody)
@RequestMapping("/api/terrains") // Base path for all endpoints in this controller
public class TerrainRestController {

    @Autowired // Inject the TerrainService
    private TerrainService terrainService;

    // --- Helper: Entity -> DTO ---
    // (You might move this to a dedicated mapper class/component later)
    private TerrainDTO convertToDto(Terrain terrain) {
        if (terrain == null) return null;
        TerrainDTO dto = new TerrainDTO();
        dto.setTerrainID(terrain.getTerrainID());
        dto.setTerrainType(terrain.getTerrainType());
        dto.setSurfaceHardness(terrain.getSurfaceHardness());
        dto.setDescription(terrain.getDescription());
        // Exclude locations list from DTO
        return dto;
    }

    // --- Helper: DTO -> Entity (Basic) ---
    // (Service layer should ideally handle loading associations if needed)
     private Terrain convertToEntity(TerrainDTO dto) {
         if (dto == null) return null;
         Terrain terrain = new Terrain();
         // ID might be set for updates, service handles if it's ignored for creates
         terrain.setTerrainID(dto.getTerrainID());
         terrain.setTerrainType(dto.getTerrainType());
         terrain.setSurfaceHardness(dto.getSurfaceHardness());
         terrain.setDescription(dto.getDescription());
         // Note: Does not handle associated locations list
         return terrain;
     }

    // --- API Endpoints ---

    /**
     * GET /api/terrains : Get all terrains.
     * @return List of TerrainDTOs.
     */
    @GetMapping
    public List<TerrainDTO> getAllTerrains() {
        return terrainService.getAllTerrains().stream()
                .map(this::convertToDto) // Convert each Terrain entity to TerrainDTO
                .collect(Collectors.toList());
    }

    /**
     * GET /api/terrains/{id} : Get a specific terrain by ID.
     * @param id The ID of the terrain.
     * @return ResponseEntity containing TerrainDTO if found (200 OK), or 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TerrainDTO> getTerrainById(@PathVariable int id) {
        return terrainService.getTerrainById(id) // Service returns Optional<Terrain>
                .map(terrain -> ResponseEntity.ok(convertToDto(terrain))) // If present, map to DTO and wrap in 200 OK
                .orElse(ResponseEntity.notFound().build()); // If empty, return 404 Not Found
    }

    /**
     * POST /api/terrains : Create a new terrain.
     * @param terrainDTO The terrain data from the request body.
     * @return The created TerrainDTO with assigned ID and 201 Created status.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Set default success status to 201 Created
    public TerrainDTO createTerrain(@RequestBody TerrainDTO terrainDTO) {
        try {
            Terrain terrainToCreate = convertToEntity(terrainDTO);
            // Assuming service/DB handles ID generation or manual ID is intended from DTO
            // If DB generates ID, ensure DTO's ID is ignored or service sets it to 0/null
            Terrain savedTerrain = terrainService.createTerrain(terrainToCreate);
            return convertToDto(savedTerrain);
        } catch (IllegalArgumentException e) {
             // Example basic error handling for validation errors from service
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
             // Catch broader exceptions for unexpected errors
             // Log the error server-side
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating terrain", e);
        }
    }

    /**
     * PUT /api/terrains/{id} : Update an existing terrain.
     * @param id The ID of the terrain to update.
     * @param terrainDTO The updated terrain data from the request body.
     * @return ResponseEntity with updated TerrainDTO (200 OK), or 404 Not Found if ID doesn't exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TerrainDTO> updateTerrain(@PathVariable int id, @RequestBody TerrainDTO terrainDTO) {
        try {
             Terrain terrainDetails = convertToEntity(terrainDTO);
             Terrain updatedTerrain = terrainService.updateTerrain(id, terrainDetails);
             return ResponseEntity.ok(convertToDto(updatedTerrain));
        } catch (RuntimeException e) { // Catch exception thrown by service if ID not found
            // Or catch specific ResourceNotFoundException if using custom exceptions
             if (e.getMessage().contains("not found")) { // Basic check, improve if using custom exceptions
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
             }
             // Handle other potential exceptions (e.g., validation)
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating terrain", e);
        }
    }

    /**
     * DELETE /api/terrains/{id} : Delete a terrain by ID.
     * @param id The ID of the terrain to delete.
     * @return ResponseEntity with 204 No Content on success, or 404 Not Found if ID doesn't exist.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerrain(@PathVariable int id) {
        try {
            terrainService.deleteTerrain(id);
            return ResponseEntity.noContent().build(); // HTTP 204 No Content
        } catch (Exception e) { // Catch specific exceptions like EmptyResultDataAccessException if preferred
            // Log the error server-side
            // Assume exception means "not found" for this example, refine as needed
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Terrain not found with id: " + id, e);
        }
    }
}