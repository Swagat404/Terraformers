package com.terraformers.dto;

import java.util.Objects; // Import needed for the Enum only

import com.terraformers.model.Terrain;

// No JPA or Spring annotations needed here - this is a simple data carrier.
public class TerrainDTO {

    private int terrainID; // Keep ID for identifying resources
    private Terrain.TerrainType terrainType; // Use the existing Enum
    private float surfaceHardness;
    private String description;

    // We typically EXCLUDE collections like 'locations' from the main DTO.
    // If a client needs the locations for a specific terrain, they would usually
    // call a separate endpoint like GET /api/terrains/{id}/locations.
    // private List<LocationDTO> locations; // Excluded for now

    // --- Constructors ---

    // No-arg constructor (often needed by frameworks like Jackson for JSON deserialization)
    public TerrainDTO() {
    }

    // Optional: Constructor for convenience
    public TerrainDTO(int terrainID, Terrain.TerrainType terrainType, float surfaceHardness, String description) {
        this.terrainID = terrainID;
        this.terrainType = terrainType;
        this.surfaceHardness = surfaceHardness;
        this.description = description;
    }


    // --- Getters and Setters ---
    // Needed for frameworks (like Jackson JSON mapper) and for manual conversion

    public int getTerrainID() {
        return terrainID;
    }

    public void setTerrainID(int terrainID) {
        this.terrainID = terrainID;
    }

    public Terrain.TerrainType getTerrainType() {
        return terrainType;
    }

    public void setTerrainType(Terrain.TerrainType terrainType) {
        this.terrainType = terrainType;
    }

    public float getSurfaceHardness() {
        return surfaceHardness;
    }

    public void setSurfaceHardness(float surfaceHardness) {
        this.surfaceHardness = surfaceHardness;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // --- Optional: toString(), equals(), hashCode() ---
    // Useful for debugging and testing DTOs themselves

    @Override
    public String toString() {
        return "TerrainDTO{" +
                "terrainID=" + terrainID +
                ", terrainType=" + terrainType +
                ", surfaceHardness=" + surfaceHardness +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerrainDTO that = (TerrainDTO) o;
        return terrainID == that.terrainID &&
               Float.compare(that.surfaceHardness, surfaceHardness) == 0 &&
               terrainType == that.terrainType &&
               Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terrainID, terrainType, surfaceHardness, description);
    }
}