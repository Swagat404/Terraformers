package com.terraformers.model;

// Import JPA and other necessary classes
import java.util.ArrayList;
import java.util.List; // Keep if used by factory method
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

// line 158 "model.ump"
// line 256 "model.ump"

@Entity
@Table(name = "sensors")
public class Sensor {

    //------------------------
    // ENUMERATIONS
    //------------------------

    public enum SensorMountPosition { Front, Back, Left, Right }
    public enum SensorStatus { Active, Inactive, Faulty }
    public enum SensorType { Type1, Type2 }

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    // --- Sensor Attributes ---
    @Id
    // Assuming manual ID for now
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_id", nullable = false)
    private int sensorID;

    @Enumerated(EnumType.STRING)
    @Column(name = "mount_position", length = 10)
    private SensorMountPosition mountPosition;

    @Enumerated(EnumType.STRING)
    @Column(length = 20) // Active, Inactive, Faulty
    private SensorStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "sensor_type", length = 10) // Type1, Type2
    private SensorType sensorType;

    // --- Sensor Associations ---

    // One Sensor has Many SensorReadings. SensorReading entity has the FK.
    @OneToMany(
            mappedBy = "sensor",           // Refers to 'sensor' field in SensorReading entity
            cascade = CascadeType.ALL,     // Operations on Sensor cascade to its readings
            orphanRemoval = true,          // Removing reading from list deletes it
            fetch = FetchType.LAZY
    )
    private List<SensorReading> sensorReadings = new ArrayList<>();

    // Many Sensors belong to One Avatar. This side owns the Foreign Key.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id", nullable = false) // FK column in 'sensors' table
    private Avatar avatar;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    // JPA requires a no-arg constructor
    public Sensor() {
    }

    // Original UMPLE constructor
    public Sensor(int aSensorID, SensorMountPosition aMountPosition, SensorStatus aStatus, SensorType aSensorType, Avatar aAvatar) {
        this.sensorID = aSensorID;
        this.mountPosition = aMountPosition;
        this.status = aStatus;
        this.sensorType = aSensorType;
        this.sensorReadings = new ArrayList<>(); // Initialize list
        // Use JPA-aware setter
        this.setAvatar(aAvatar);
        // Constructor check (!didAdd...) usually not needed
    }

    //------------------------
    // INTERFACE (Getters/Setters)
    //------------------------
    // Keep standard getters and setters

    public boolean setSensorID(int aSensorID) { this.sensorID = aSensorID; return true; }
    public int getSensorID() { return sensorID; }

    public boolean setMountPosition(SensorMountPosition aMountPosition) { this.mountPosition = aMountPosition; return true; }
    public SensorMountPosition getMountPosition() { return mountPosition; }

    public boolean setStatus(SensorStatus aStatus) { this.status = aStatus; return true; }
    public SensorStatus getStatus() { return status; }

    public boolean setSensorType(SensorType aSensorType) { this.sensorType = aSensorType; return true; }
    public SensorType getSensorType() { return sensorType; }


    // --- Association Accessors/Mutators (Review/Replace UMPLE versions) ---

    public List<SensorReading> getSensorReadings() {
        return sensorReadings; // Direct list for JPA lazy loading
    }
    public int numberOfSensorReadings() { return this.sensorReadings.size(); }
    public boolean hasSensorReadings() { return !this.sensorReadings.isEmpty(); }
    public int indexOfSensorReading(SensorReading aSensorReading) { return this.sensorReadings.indexOf(aSensorReading); }
    public SensorReading getSensorReading(int index) { /* Add bounds check */ return sensorReadings.get(index); }
    public static int minimumNumberOfSensorReadings() { return 0; } // Keep if needed for service logic


    public Avatar getAvatar() { return avatar; }

    // --- JPA-aware Add/Remove for SensorReadings collection ---

    public void addSensorReading(SensorReading sensorReading) {
        if (sensorReading != null && !this.sensorReadings.contains(sensorReading)) {
            this.sensorReadings.add(sensorReading);
            if (!this.equals(sensorReading.getSensor())) { // Avoid loop
                sensorReading.setSensor(this);
            }
        }
    }

    public void removeSensorReading(SensorReading sensorReading) {
         if (sensorReading != null && this.sensorReadings.contains(sensorReading)) {
            this.sensorReadings.remove(sensorReading);
             if (this.equals(sensorReading.getSensor())) { // Break other side
                 sensorReading.setSensor(null);
             }
        }
        // Note: Original UMPLE logic prevented removal if reading pointed here.
    }

     /**
     * Sets the Avatar for this Sensor, maintaining bidirectional consistency.
     * @param newAvatar The Avatar this sensor belongs to. Cannot be null.
     * @return boolean true if successful.
     */
    public boolean setAvatar(Avatar newAvatar) {
         if (newAvatar == null) {
             // Decide handling (is avatar mandatory?)
             // throw new IllegalArgumentException("Avatar cannot be null for Sensor");
             return false; // Following original UMPLE check pattern
         }

        // Avoid self-assignment loop / unnecessary work
        if (Objects.equals(this.avatar, newAvatar)) {
            return true;
        }

        // If currently associated with a different avatar, remove from its list
        if (this.avatar != null) {
            this.avatar.removeSensor(this); // Use helper on Avatar
        }

        // Set the new avatar reference on this sensor
        this.avatar = newAvatar;

        // Add this sensor to the new avatar's list
        this.avatar.addSensor(this); // Use helper on Avatar

        return true;
    }


    // UMPLE factory/index methods usually removed/refactored for JPA
    // public SensorReading addSensorReading(...) { ... }
    // public boolean addSensorReadingAt(...) { ... }
    // public boolean addOrMoveSensorReadingAt(...) { ... }


    // --- Delete Method ---
    // Handled by repository.delete(sensor)
    public void delete() {
         // Break link with Avatar *before* deletion
         if (this.avatar != null) {
             // Avatar placeholderAvatar = avatar; // Use correct reference
             Avatar placeholderAvatar = this.avatar;
             this.avatar = null; // Null internal ref first
             placeholderAvatar.removeSensor(this); // Tell avatar to remove this sensor
         }
        // CascadeType.ALL on sensorReadings handles deleting children
        // No need to loop through sensorReadings here if cascade is set
         // super.delete(); // If extending a class with delete()
    }


    // --- equals() and hashCode() ---
     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sensor)) return false;
        Sensor sensor = (Sensor) o;
        if (sensorID == 0 && sensor.sensorID == 0) return this == o;
        return sensorID == sensor.sensorID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sensorID);
    }

    // --- toString() ---
    @Override
    public String toString() {
        return "Sensor{" +
                "sensorID=" + sensorID +
                ", mountPosition=" + mountPosition +
                ", status=" + status +
                ", sensorType=" + sensorType +
                ", avatarId=" + (avatar != null ? avatar.getAvatarID() : "null") +
                '}';
    }
}