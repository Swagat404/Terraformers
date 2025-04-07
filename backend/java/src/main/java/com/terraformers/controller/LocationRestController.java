package com.terraformers.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.terraformers.dto.LocationDTO;
import com.terraformers.model.Location;
import com.terraformers.service.LocationService;

@RestController
@RequestMapping("/api/locations")
public class LocationRestController {

    @Autowired
    private LocationService locationService;

    // --- Helper: Entity -> DTO ---
    private LocationDTO convertToDto(Location location) {
        if (location == null) return null;
        LocationDTO dto = new LocationDTO();
        dto.setLocationID(location.getLocationID());
        dto.setLongitude(location.getLongitude());
        dto.setLatitude(location.getLatitude());
        dto.setAltitude(location.getAltitude());
        dto.setSlope(location.getSlope());
        if (location.getTerrain() != null) {
            dto.setTerrainId(location.getTerrain().getTerrainID());
        }
        if (location.getAvatar() != null) {
            dto.setAvatarId(location.getAvatar().getAvatarID());
        }
        return dto;
    }

     // --- Helper: DTO -> Entity (Basic - Service handles association loading) ---
     private Location convertToEntity(LocationDTO dto) {
         if (dto == null) return null;
         Location location = new Location();
         location.setLocationID(dto.getLocationID()); // Service decides whether to use/ignore
         location.setLongitude(dto.getLongitude());
         location.setLatitude(dto.getLatitude());
         location.setAltitude(dto.getAltitude());
         location.setSlope(dto.getSlope());
         // Terrain/Avatar associations set via IDs in the Service layer
         return location;
     }

    // GET /api/locations - Get all
    @GetMapping
    public List<LocationDTO> getAllLocations() {
        return locationService.getAllLocations().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // GET /api/locations/{id} - Get one by ID
    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable int id) {
        return locationService.getLocationById(id)
                .map(location -> ResponseEntity.ok(convertToDto(location)))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/locations - Create new
    // Needs Terrain ID, could be path variable or in request body/DTO
    // Let's assume it's required in the DTO for simplicity here.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDTO createLocation(@RequestBody LocationDTO locationDTO) {
        if (locationDTO.getTerrainId() == null) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required terrainId for creating location");
        }
        try {
            Location locationDetails = convertToEntity(locationDTO);
            Location savedLocation = locationService.createLocation(locationDetails, locationDTO.getTerrainId());
            return convertToDto(savedLocation);
        } catch (IllegalArgumentException e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RuntimeException e) { // Catch "not found" for terrain, etc.
             if (e.getMessage().contains("Terrain not found")) {
                  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e); // Bad Request because linked entity doesn't exist
             }
             // Log other errors
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating location", e);
        }
    }

    // PUT /api/locations/{id} - Update existing
    // Optionally allow changing terrain via query param or DTO field
    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(
            @PathVariable int id,
            @RequestBody LocationDTO locationDTO,
            @RequestParam(required = false) Integer newTerrainId) { // Allow optional terrain change via param
            // Alternatively, include newTerrainId in the LocationDTO itself

        if (locationDTO.getTerrainId() != null && newTerrainId == null) {
            // If terrainId is in DTO but not param, use the DTO one
            newTerrainId = locationDTO.getTerrainId();
        } else if (locationDTO.getTerrainId() != null && newTerrainId != null && !locationDTO.getTerrainId().equals(newTerrainId)){
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Terrain ID mismatch between request body and query parameter.");
        }

        try {
             Location locationDetails = convertToEntity(locationDTO);
             Location updatedLocation = locationService.updateLocation(id, locationDetails, newTerrainId);
             return ResponseEntity.ok(convertToDto(updatedLocation));
        } catch (RuntimeException e) { // Catch "not found" from service
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating location", e);
        }
    }

    // DELETE /api/locations/{id} - Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable int id) {
        try {
            locationService.deleteLocation(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception e) { // Catch specific exceptions e.g. EmptyResultDataAccessException
            // Log error
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Location not found with id: " + id, e);
        }
    }

     // GET /api/terrains/{terrainId}/locations - Get locations for a specific terrain
     // Example of a nested resource endpoint
     @GetMapping(params = "terrainId") // Filter by query parameter ?terrainId=...
     public List<LocationDTO> getLocationsByTerrain(@RequestParam int terrainId) {
         return locationService.getLocationsByTerrainId(terrainId).stream()
                 .map(this::convertToDto)
                 .collect(Collectors.toList());
     }
     // Alternatively, define this in TerrainRestController: @GetMapping("/{id}/locations")

}