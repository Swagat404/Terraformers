package com.terraformers.dto;

import java.util.Objects;

// DTO for Location API data transfer
public class LocationDTO {

    private int locationID;
    private float longitude;
    private float latitude;
    private float altitude;
    private float slope;

    // Include ID of the associated Terrain
    private Integer terrainId; // Use Integer wrapper for potential null (although likely mandatory)

    // Exclude the Avatar object - maybe include avatarId if needed for display?
    private Integer avatarId; // Optional: ID of avatar currently at this location

    // --- Constructors ---
    public LocationDTO() {
    }

    // --- Getters and Setters ---
    public int getLocationID() { return locationID; }
    public void setLocationID(int locationID) { this.locationID = locationID; }
    public float getLongitude() { return longitude; }
    public void setLongitude(float longitude) { this.longitude = longitude; }
    public float getLatitude() { return latitude; }
    public void setLatitude(float latitude) { this.latitude = latitude; }
    public float getAltitude() { return altitude; }
    public void setAltitude(float altitude) { this.altitude = altitude; }
    public float getSlope() { return slope; }
    public void setSlope(float slope) { this.slope = slope; }
    public Integer getTerrainId() { return terrainId; }
    public void setTerrainId(Integer terrainId) { this.terrainId = terrainId; }
    public Integer getAvatarId() { return avatarId; }
    public void setAvatarId(Integer avatarId) { this.avatarId = avatarId; }

    // --- Optional: equals, hashCode, toString ---
     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationDTO that = (LocationDTO) o;
        return locationID == that.locationID && Float.compare(that.longitude, longitude) == 0 && Float.compare(that.latitude, latitude) == 0 && Float.compare(that.altitude, altitude) == 0 && Float.compare(that.slope, slope) == 0 && Objects.equals(terrainId, that.terrainId) && Objects.equals(avatarId, that.avatarId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationID, longitude, latitude, altitude, slope, terrainId, avatarId);
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
                "locationID=" + locationID +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", altitude=" + altitude +
                ", slope=" + slope +
                ", terrainId=" + terrainId +
                ", avatarId=" + avatarId +
                '}';
    }
}