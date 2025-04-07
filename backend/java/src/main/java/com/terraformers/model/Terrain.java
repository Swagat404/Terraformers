package com.terraformers.model;

// Import JPA and other necessary classes
import java.util.ArrayList; // Core JPA annotations
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Classes
 * Represents a terrain area in the Mars simulation.
 * Now annotated as a JPA Entity.
 */
// line 73 "model.ump"
// line 206 "model.ump"

@Entity                  // Marks this class as a JPA entity (represents a table row)
@Table(name = "terrains") // Specifies the database table name (optional if class name matches table name)
public class Terrain {

    //------------------------
    // ENUMERATIONS
    //------------------------

    public enum TerrainType {Hilly, Flat, Rocky}

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    // --- Terrain Attributes ---

    @Id // Designates this field as the primary key
    // If your DB generates IDs (e.g., SERIAL), uncomment @GeneratedValue.
    // If you set IDs manually (like in constructor), keep it commented out.
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terrain_id", nullable = false) // Map to column, ensure not null if PK
    private int terrainID;

    @Enumerated(EnumType.STRING) // Store the enum constant's name (e.g., "Rocky") as a string
    @Column(name = "terrain_type", length = 50) // Specify column name and optionally length
    private TerrainType terrainType;

    @Column(name = "surface_hardness")
    private float surfaceHardness;

    // Use @Lob for potentially large text fields if needed, otherwise default VARCHAR is fine
    // @Lob
    private String description;

    // --- Terrain Associations ---

    // One Terrain has Many Locations. Location entity owns the relationship via its 'terrain' field.
    @OneToMany(
            mappedBy = "terrain",          // Refers to the 'terrain' field in the Location entity
            cascade = CascadeType.ALL,     // Operations on Terrain (save, delete) cascade to associated Locations
            orphanRemoval = true,          // If a Location is removed from this list, delete it from DB
            fetch = FetchType.LAZY           // Load locations only when getLocations() is called
    )
    private List<Location> locations = new ArrayList<>(); // Initialize the list

    //------------------------
    // CONSTRUCTOR
    //------------------------

    // JPA REQUIRES a no-argument constructor (public or protected)
    public Terrain() {
    }

    // Your original constructor (can be used for manual creation or testing)
    public Terrain(int aTerrainID, TerrainType aTerrainType, float aSurfaceHardness, String aDescription) {
        this.terrainID = aTerrainID;
        this.terrainType = aTerrainType;
        this.surfaceHardness = aSurfaceHardness;
        this.description = aDescription;
        // List is initialized above
    }

    //------------------------
    // INTERFACE (Getters/Setters)
    //------------------------
    // Keep the standard getters and setters. JPA uses them.
    // The UMPLE boolean return type on setters isn't standard for JavaBeans/JPA,
    // but they should still work. Consider changing them to void if cleaning up.

    public boolean setTerrainID(int aTerrainID) {
        this.terrainID = aTerrainID;
        return true;
    }
    public int getTerrainID() { return terrainID; }

    public boolean setTerrainType(TerrainType aTerrainType) {
        this.terrainType = aTerrainType;
        return true;
    }
    public TerrainType getTerrainType() { return terrainType; }

    public boolean setSurfaceHardness(float aSurfaceHardness) {
        this.surfaceHardness = aSurfaceHardness;
        return true;
    }
    public float getSurfaceHardness() { return surfaceHardness; }

    public boolean setDescription(String aDescription) {
        this.description = aDescription;
        return true;
    }
    public String getDescription() { return description; }

    // --- Association Accessors/Mutators ---

    /* Code from template association_GetMany */
    // Not typically needed with direct list access, but harmless
    public Location getLocation(int index) {
        // Add bounds checking for safety if keeping this method
        if (index >= 0 && index < locations.size()) {
            return locations.get(index);
        }
        return null; // Or throw IndexOutOfBoundsException
    }

    /**
     * Association: One Terrain is linked with many Locations.
     * Provides access to the list of associated locations.
     * NOTE: Returning the direct list allows JPA's lazy loading proxy to work.
     * Returning an unmodifiable list requires careful handling of detached entities.
     */
    public List<Location> getLocations() {
        // For JPA lazy loading, direct access is usually preferred over unmodifiableList
        // return Collections.unmodifiableList(locations);
         return locations;
    }

    public int numberOfLocations() {
        return this.locations.size(); // Use the actual list
    }

    public boolean hasLocations() {
        return !this.locations.isEmpty();
    }

    public int indexOfLocation(Location aLocation) {
        return this.locations.indexOf(aLocation);
    }

    /* Code from template association_MinimumNumberOfMethod */
    // This might still be relevant for business validation in the Service layer,
    // but isn't directly enforced by JPA annotations here (though DB constraints could be used).
    public static int minimumNumberOfLocations() {
        return 1;
    }

     /* Code from template association_IsNumberOfValidMethod */
    // Useful for validation in Service layer.
    public boolean isNumberOfLocationsValid() {
        return numberOfLocations() >= minimumNumberOfLocations();
    }


    /* Code from template association_AddMandatoryManyToOne */
    // This factory method might not be directly used when working with JPA entities.
    // Usually, you create a Location separately and then add it.
    // public Location addLocation(int aLocationID, float aLongitude, float aLatitude, float aAltitude, float aSlope)
    // {
    //   Location aNewLocation = new Location(aLocationID, aLongitude, aLatitude, aAltitude, aSlope, this);
    //   // Should likely call the other addLocation here too if keeping this method
    //   // this.addLocation(aNewLocation);
    //   return aNewLocation;
    // }


    // --- JPA-aware Association Management Methods ---
    // Use these helper methods to maintain bidirectional consistency reliably.

        public void addLocation(Location aLocation) // Using original parameter name
    {
        // 1. Check for null or duplicates first
        if (aLocation == null || locations.contains(aLocation)) {
            return; // Do nothing
        }

        Terrain existingTerrain = aLocation.getTerrain();
        boolean isTransfer = existingTerrain != null && !this.equals(existingTerrain);

        // 2. Check minimum constraint specifically IF it's a transfer
        if (isTransfer && existingTerrain.numberOfLocations() <= minimumNumberOfLocations()) {
            // Cannot transfer if the source drops below minimum
            return; // <<< EXIT EARLY - DO NOT ADD
        }

        // 3. If we passed the checks, NOW add the location to this list
        locations.add(aLocation); // <<< Add happens ONLY if checks pass

        // 4. If it was a transfer, also update the location's back-reference
        if (isTransfer) {
            aLocation.setTerrain(this);
        }
        // If not a transfer, back-ref assumed handled elsewhere (e.g., Location's constructor/setter)
    }

    // In Terrain.java
public void removeLocation(Location location) {
    // Check 1: Is the location non-null and actually in the list?
    if (location == null || !this.locations.contains(location)) {
        return; // Nothing to remove or invalid input
    }

    // Check 2: Prevent removal if it violates minimum constraint AND it's the location's current terrain
    // This prevents orphaning the location from its *required* terrain when at minimum count.
    if (this.equals(location.getTerrain())) { // Check only if it currently belongs here
        if (numberOfLocations() <= minimumNumberOfLocations()) {
            // Cannot remove because this terrain requires at least one location,
            // and this location currently belongs to this terrain.
            return; // Do not remove
        }
    }

    // If checks pass, proceed with removal
    boolean removed = this.locations.remove(location);

    // If removal was successful, attempt to break the link IF it still points here
    // (It might have changed between the check and now, though unlikely in single thread)
    if (removed && this.equals(location.getTerrain())) {
        location.setTerrain(null);
    }
}

    // The UMPLE addLocationAt/addOrMoveLocationAt methods often duplicate logic
    // and might conflict with JPA's collection management. Generally remove them
    // or ensure they correctly use the primary add/remove methods above.
    // public boolean addLocationAt(Location aLocation, int index) { ... }
    // public boolean addOrMoveLocationAt(Location aLocation, int index) { ... }


    // --- Delete Method ---
    // The UMPLE delete method is usually unnecessary with JPA.
    // repository.delete(terrain) or repository.deleteById(id) handles it.
    // CascadeType.ALL on the 'locations' association handles deleting children.
    // public void delete() { ... }


    // --- equals() and hashCode() based on Primary Key (ID) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Use instanceof for better type checking, especially with proxies
        if (!(o instanceof Terrain)) return false;
        Terrain terrain = (Terrain) o;
        // If IDs are assigned only upon persistence and can be 0 before,
        // rely on reference equality for transient instances.
        if (terrainID == 0 && terrain.terrainID == 0) {
             return this == o; // Or super.equals(o) if appropriate
        }
        // Once IDs are set (non-zero assumed for persisted entities), use ID.
        return terrainID == terrain.terrainID;
    }

    @Override
    public int hashCode() {
        // Consistent with equals: use ID if set (non-zero),
        // otherwise use default hashcode for transient instances.
        // return terrainID != 0 ? Integer.hashCode(terrainID) : super.hashCode();
        // Or using Objects.hash if ID is Integer wrapper type:
         return Objects.hash(terrainID); // Handles null if ID was Integer, fine for int 0 too
    }

    // --- toString() ---
    // Keep or modify as needed for logging/debugging
     @Override
    public String toString() {
        return "Terrain{" +
                "terrainID=" + terrainID +
                ", terrainType=" + terrainType +
                ", surfaceHardness=" + surfaceHardness +
                ", description='" + description + '\'' +
                '}';
        // Avoid printing collections in toString() to prevent lazy loading issues
        // or infinite loops in bidirectional relationships.
    }
}