package com.terraformers.model;

// Import JPA and other necessary classes
import java.util.Objects; // Core JPA annotations

import javax.persistence.CascadeType; // For Objects.hash/equals
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Represents a specific location point on the terrain.
 * Now annotated as a JPA Entity.
 */
// line 83 "model.ump"
// line 212 "model.ump"

@Entity                   // Marks this class as a JPA entity
@Table(name = "locations")  // Specifies the database table name
public class Location {

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    // --- Location Attributes ---

    @Id // Designates this field as the primary key
    // Assuming manual ID setting based on original constructor
    // @GeneratedValue(strategy = GenerationType.IDENTITY) // Uncomment if DB generates IDs
    @Column(name = "location_id", nullable = false)
    private int locationID;

    private float longitude; // Assumes column name matches
    private float latitude;
    private float altitude;
    private float slope;

    // --- Location Associations ---

    // Many Locations belong to One Terrain. This side owns the Foreign Key.
    @ManyToOne(fetch = FetchType.LAZY) // Many locations can be on one terrain. Lazy load terrain.
    @JoinColumn(name = "terrain_id", nullable = false) // Specifies the FK column in the 'locations' table. Assumed not nullable.
    private Terrain terrain;

    // One Location has (at most) one Avatar. Mapped by the 'location' field in Avatar entity.
    // The Avatar entity typically "owns" this OneToOne if it has the FK column.
    // If Location has the FK to Avatar instead, the mapping here changes.
    // Assuming Avatar has the FK based on previous Avatar entity example.
    @OneToOne(mappedBy = "location", // Refers to the 'location' field in the Avatar entity
              fetch = FetchType.LAZY,
              cascade = CascadeType.PERSIST) // Cascade PERSIST *from Location to Avatar* might be useful, others less so. Use ALL with caution.
    private Avatar avatar;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    // JPA REQUIRES a no-argument constructor
    public Location() {
    }

    // Original UMPLE constructor
    public Location(int aLocationID, float aLongitude, float aLatitude, float aAltitude, float aSlope, Terrain aTerrain) {
        this.locationID = aLocationID;
        this.longitude = aLongitude;
        this.latitude = aLatitude;
        this.altitude = aAltitude;
        this.slope = aSlope;
        // Use the JPA-aware setter to establish the bidirectional link correctly
        this.setTerrain(aTerrain);
        // Avatar starts as null
    }

    //------------------------
    // INTERFACE (Getters/Setters)
    //------------------------
    // Standard Getters/Setters are needed by JPA

    public boolean setLocationID(int aLocationID) { this.locationID = aLocationID; return true; }
    public int getLocationID() { return locationID; }

    public boolean setLongitude(float aLongitude) { this.longitude = aLongitude; return true; }
    public float getLongitude() { return longitude; }

    public boolean setLatitude(float aLatitude) { this.latitude = aLatitude; return true; }
    public float getLatitude() { return latitude; }

    public boolean setAltitude(float aAltitude) { this.altitude = aAltitude; return true; }
    public float getAltitude() { return altitude; }

    public boolean setSlope(float aSlope) { this.slope = aSlope; return true; }
    public float getSlope() { return slope; }

    // --- Association Accessors/Mutators ---

    public Terrain getTerrain() { return terrain; }
    public Avatar getAvatar() { return avatar; }
    public boolean hasAvatar() { return avatar != null; }

    // --- JPA-aware Association Setters ---

    /**
     * Sets the Terrain for this Location, maintaining bidirectional consistency.
     * The complex logic from UMPLE regarding minimums/removals is simplified;
     * such business rules are often better placed in the Service layer.
     * This method ensures the links are set correctly from this side.
     *
     * @param newTerrain The Terrain to associate this Location with.
     * @return boolean always true if successful (can be changed to void).
     */
    public boolean setTerrain(Terrain newTerrain) {
        // Check if setting to the same terrain to avoid infinite loops/unnecessary work
        if (Objects.equals(this.terrain, newTerrain)) {
            return true; // Or false if indicating no change occurred
        }

        // If currently associated with a different terrain, remove from its list
        if (this.terrain != null) {
             // Use the helper method on the *old* terrain
            this.terrain.removeLocation(this);
        }

        // Set the new terrain reference on this location
        this.terrain = newTerrain;

        // If the new terrain is not null, add this location to its list
        if (newTerrain != null) {
             // Use the helper method on the *new* terrain
            newTerrain.addLocation(this);
        }

        return true; // Indicate success
    }


    /**
     * Sets the Avatar for this Location, maintaining bidirectional consistency.
     * Uses the corrected logic without the flawed orphan check.
     *
     * @param newAvatar The new Avatar to associate, or null to remove association.
     * @return boolean always true if successful.
     */
     // You already pasted the corrected version of this, keep it.
    public boolean setAvatar(Avatar newAvatar) {
        boolean wasSet = false;

        // --- Corrected Logic (No flawed orphan check) ---

        Avatar existingAvatar = avatar; // Store old one if needed for other logic
        avatar = newAvatar; // Set the internal field

        // Update the other side of the association
        Location anOldLocation = newAvatar != null ? newAvatar.getLocation() : null;
        if (!this.equals(anOldLocation)) {
            if (anOldLocation != null) {
                anOldLocation.avatar = null; // Null link on NEW avatar's OLD location
            }
            if (avatar != null) { // 'avatar' is the new value
                avatar.setLocation(this); // Make NEW avatar point back here
            }
        }
        wasSet = true;
        return wasSet;
    }

    // --- Delete Method ---
    // UMPLE delete logic is usually replaced by JPA repository operations.
    // Cascade settings on Terrain and Avatar handle related deletions if configured.
    // public void delete() { ... }


    // --- equals() and hashCode() based on Primary Key (ID) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false; // Use instanceof
        Location location = (Location) o;
        // Handle transient instances (ID=0) by reference equality
        if (locationID == 0 && location.locationID == 0) {
             return this == o; // Or super.equals(o)
        }
        return locationID == location.locationID;
    }

    @Override
    public int hashCode() {
        // Consistent with equals
        // return locationID != 0 ? Integer.hashCode(locationID) : super.hashCode();
         return Objects.hash(locationID); // Handles 0 okay if ID is Integer wrapper, also fine for int
    }

    // --- toString() ---
    @Override
    public String toString() {
        return "Location{" +
                "locationID=" + locationID +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", altitude=" + altitude +
                ", slope=" + slope +
                // Avoid printing associated entities directly to prevent issues
                ", terrainId=" + (terrain != null ? terrain.getTerrainID() : "null") +
                ", avatarId=" + (avatar != null ? avatar.getAvatarID() : "null") +
                '}';
    }
}