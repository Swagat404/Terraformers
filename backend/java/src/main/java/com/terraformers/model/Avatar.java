package com.terraformers.model;

// Import JPA and other necessary classes
import java.util.ArrayList;
import java.util.List;
import java.util.Objects; // Keep if used by AvatarLog association method (though usually better handled via service)

import javax.persistence.CascadeType; // Keep if used by AvatarLog association method
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

// line 109 "model.ump"
// line 230 "model.ump"

@Entity
@Table(name = "avatars")
public class Avatar {

    //------------------------
    // ENUMERATIONS
    //------------------------
    // Keep enums as they are defined within the class

    public enum LogType { Info, Warning, Error } // Note: LogType is also in Log class, ensure consistency or remove redundancy
    public enum AvatarColor { Red, Blue, Green, Yellow }
    public enum SensorMountPosition { Front, Back, Left, Right } // Note: Also in Sensor class
    public enum SensorStatus { Active, Inactive, Faulty } // Note: Also in Sensor class
    public enum SensorType { Type1, Type2 } // Note: Also in Sensor class

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    // --- Avatar Attributes ---
    @Id
    // Assuming manual ID setting based on constructor for now
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avatar_id", nullable = false)
    private int avatarID;

    @Column(name = "avatar_name", length = 100) // Optional: Define length
    private String avatarName;

    @Enumerated(EnumType.STRING)
    @Column(name = "avatar_color", length = 20)
    private AvatarColor avatarColor;

    // --- Avatar Associations ---

    // One-to-One with Location: Assuming Avatar has the FK column
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Cascade options depend on ownership/lifecycle needs
    @JoinColumn(name = "location_id", referencedColumnName = "locationID", unique = true) // FK in avatars table, must be unique
    private Location location;

    // One-to-Many with AvatarLog: AvatarLog has the FK ('avatar' field)
    @OneToMany(mappedBy = "avatar", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AvatarLog> avatarLogs = new ArrayList<>();

    // One-to-Many with Sensor: Sensor has the FK ('avatar' field)
    @OneToMany(mappedBy = "avatar", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Sensor> sensors = new ArrayList<>();

    // One-to-One with AvatarBrain: Assuming Avatar has the FK column
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "avatar_brain_id", referencedColumnName = "avatarBrainID", unique = true) // FK in avatars table
    private AvatarBrain avatarBrain;

    // Many-to-One with Mission: Avatar has the FK column
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id") // FK column in avatars table, nullability depends on requirements
    private Mission mission;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    // JPA requires a no-arg constructor
    public Avatar() {
    }

    // Original UMPLE constructor - Use with caution with JPA association management
    // It's often better to set associations via JPA-aware setters or service logic after creation.
    public Avatar(int aAvatarID, String aAvatarName, AvatarColor aAvatarColor, Location aLocation, AvatarBrain aAvatarBrain, Mission aMission) {
        this.avatarID = aAvatarID;
        this.avatarName = aAvatarName;
        this.avatarColor = aAvatarColor;
        // Initialize lists
        this.avatarLogs = new ArrayList<>();
        this.sensors = new ArrayList<>();
        // Set associations using JPA-aware methods if possible, or handle in service
        this.setLocation(aLocation); // Requires setLocation to be JPA-aware
        this.setAvatarBrain(aAvatarBrain); // Requires setAvatarBrain to be JPA-aware
        this.setMission(aMission); // Requires setMission to be JPA-aware
        // The constructor checks (!didAdd...) are problematic with JPA as persistence context manages relationships.
    }

    //------------------------
    // INTERFACE (Getters/Setters)
    //------------------------
    // Standard getters and setters are needed

    public boolean setAvatarID(int aAvatarID) { this.avatarID = aAvatarID; return true; }
    public int getAvatarID() { return avatarID; }

    public boolean setAvatarName(String aAvatarName) { this.avatarName = aAvatarName; return true; }
    public String getAvatarName() { return avatarName; }

    public boolean setAvatarColor(AvatarColor aAvatarColor) { this.avatarColor = aAvatarColor; return true; }
    public AvatarColor getAvatarColor() { return avatarColor; }

    // --- Association Accessors/Mutators (Review/Replace UMPLE versions) ---

    public Location getLocation() { return location; }
    public AvatarBrain getAvatarBrain() { return avatarBrain; }
    public Mission getMission() { return mission; }

    // Getters for lists - return direct list for JPA lazy loading
    public List<AvatarLog> getAvatarLogs() { return avatarLogs; }
    public List<Sensor> getSensors() { return sensors; }

    // List query methods (UMPL E) - These are fine
    public int numberOfAvatarLogs() { return avatarLogs.size(); }
    public boolean hasAvatarLogs() { return !avatarLogs.isEmpty(); }
    public int indexOfAvatarLog(AvatarLog aAvatarLog) { return avatarLogs.indexOf(aAvatarLog); }
    public AvatarLog getAvatarLog(int index) { /* Add bounds check */ return avatarLogs.get(index); }

    public int numberOfSensors() { return sensors.size(); }
    public boolean hasSensors() { return !sensors.isEmpty(); }
    public int indexOfSensor(Sensor aSensor) { return sensors.indexOf(aSensor); }
    public Sensor getSensor(int index) { /* Add bounds check */ return sensors.get(index); }

    // --- JPA-aware Setters for Associations ---

    /**
     * Sets the Location for this Avatar, maintaining bidirectional consistency for OneToOne.
     * Assumes Avatar owns the foreign key via @JoinColumn("location_id").
     * Handles unsetting the previous location's avatar link.
     */
    public boolean setLocation(Location newLocation) {
        // Avoid self-assignment infinite loop
        if (Objects.equals(this.location, newLocation)) {
            return true;
        }

        Location oldLocation = this.location;

        // Break link with old location *if* it still pointed to this avatar
        if (oldLocation != null && this.equals(oldLocation.getAvatar())) {
             oldLocation.setAvatar(null); // Tell old location it no longer has this avatar
        }

        // Set the new location here
        this.location = newLocation;

        // If setting a new location, ensure its avatar link points back to this avatar
        if (newLocation != null) {
            // Check if the new location already points to a *different* avatar (potential conflict)
            if (newLocation.getAvatar() != null && !this.equals(newLocation.getAvatar())) {
                 // Handle conflict: throw exception, log warning, or overwrite?
                 // For now, let's attempt overwrite but log it. Ideally service layer handles this.
                 System.err.println("Warning: Setting location on Avatar " + this.avatarID +
                                    " which conflicts with existing Avatar " + newLocation.getAvatar().getAvatarID() +
                                    " on Location " + newLocation.getLocationID());
                 // Force the other avatar to lose this location
                 // newLocation.getAvatar().setLocation(null); // Risky without transaction context
            }
             // Ensure the new location points back to this avatar
            if (!this.equals(newLocation.getAvatar())) { // Avoid infinite loop
                newLocation.setAvatar(this);
            }
        }
        return true; // Assuming success if no exception
    }


     /**
      * Sets the AvatarBrain for this Avatar, managing the bidirectional OneToOne link.
      * Assumes Avatar owns the foreign key via @JoinColumn("avatar_brain_id").
      */
    public boolean setAvatarBrain(AvatarBrain newAvatarBrain) {
        // Check for null if AvatarBrain is mandatory (based on UMPLE/business rules)
         if (newAvatarBrain == null) {
             // Maybe throw exception or handle based on requirements
              // return false; // UMPLE original check
              // For now, allow unsetting, but nullify old link if needed
         }

        // Avoid self-assignment loop
        if (Objects.equals(this.avatarBrain, newAvatarBrain)) {
            return true;
        }

        AvatarBrain oldAvatarBrain = this.avatarBrain;

        // Break link with old brain *if* it still pointed here
        if (oldAvatarBrain != null && this.equals(oldAvatarBrain.getAvatar())) {
            oldAvatarBrain.setAvatar(null);
        }

        this.avatarBrain = newAvatarBrain;

        // Set link on new brain
        if (newAvatarBrain != null) {
            // Handle potential conflict if new brain already has an avatar
             if (newAvatarBrain.getAvatar() != null && !this.equals(newAvatarBrain.getAvatar())) {
                 System.err.println("Warning: Setting brain on Avatar " + this.avatarID +
                                    " which conflicts with existing Avatar " + newAvatarBrain.getAvatar().getAvatarID() +
                                    " on Brain " + newAvatarBrain.getAvatarBrainID());
                // Force the other avatar off the brain? Risky.
                // newAvatarBrain.getAvatar().setAvatarBrain(null);
             }
            // Make new brain point back
            if (!this.equals(newAvatarBrain.getAvatar())) {
                newAvatarBrain.setAvatar(this);
            }
        }
        return true;
    }


    /**
     * Sets the Mission for this Avatar, managing the bidirectional ManyToOne/OneToMany link.
     * Assumes Avatar has the @ManyToOne FK side.
     */
    public boolean setMission(Mission newMission) {
        // Check for null if mission is mandatory
        // if (newMission == null) { return false; // As per UMPLE }

        // Avoid self-assignment loop
        if (Objects.equals(this.mission, newMission)) {
            return true;
        }

        Mission oldMission = this.mission;

        // Remove this avatar from the old mission's list
        if (oldMission != null) {
            oldMission.removeAvatar(this); // Use helper on Mission
        }

        // Set the new mission here
        this.mission = newMission;

        // Add this avatar to the new mission's list
        if (newMission != null) {
            newMission.addAvatar(this); // Use helper on Mission
        }
        return true;
    }

    // --- JPA-aware Add/Remove methods for Collections ---

    public void addAvatarLog(AvatarLog avatarLog) {
        if (avatarLog != null && !this.avatarLogs.contains(avatarLog)) {
            this.avatarLogs.add(avatarLog);
            if (!this.equals(avatarLog.getAvatar())) { // Avoid loop
                avatarLog.setAvatar(this);
            }
        }
    }

    public void removeAvatarLog(AvatarLog avatarLog) {
        if (avatarLog != null && this.avatarLogs.contains(avatarLog)) {
            this.avatarLogs.remove(avatarLog);
             if (this.equals(avatarLog.getAvatar())) { // Break other side if needed
                avatarLog.setAvatar(null);
             }
        }
    }

    public void addSensor(Sensor sensor) {
        if (sensor != null && !this.sensors.contains(sensor)) {
            this.sensors.add(sensor);
            if (!this.equals(sensor.getAvatar())) { // Avoid loop
                sensor.setAvatar(this);
            }
        }
    }

    public void removeSensor(Sensor sensor) {
         if (sensor != null && this.sensors.contains(sensor)) {
            this.sensors.remove(sensor);
             if (this.equals(sensor.getAvatar())) { // Break other side if needed
                sensor.setAvatar(null);
             }
        }
    }

    // UMPLE factory methods and index methods usually removed for JPA entities
    // public AvatarLog addAvatarLog(...) { ... }
    // public boolean addAvatarLogAt(...) { ... }
    // public Sensor addSensor(...) { ... }
    // public boolean addSensorAt(...) { ... }


    // --- Delete Method ---
    // Usually handled by repository.delete(avatar)
    // Cascade settings manage associated entities.
    // UMPLE delete logic might conflict with JPA lifecycle.
    // public void delete() { ... }


    // --- equals() and hashCode() ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Avatar)) return false;
        Avatar avatar = (Avatar) o;
        if (avatarID == 0 && avatar.avatarID == 0) return this == o;
        return avatarID == avatar.avatarID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(avatarID);
    }

    // --- toString() ---
    @Override
    public String toString() {
        return "Avatar{" +
                "avatarID=" + avatarID +
                ", avatarName='" + avatarName + '\'' +
                ", avatarColor=" + avatarColor +
                ", locationId=" + (location != null ? location.getLocationID() : "null") +
                ", avatarBrainId=" + (avatarBrain != null ? avatarBrain.getAvatarBrainID() : "null") +
                ", missionId=" + (mission != null ? mission.getMissionID() : "null") +
                '}';
    }
}