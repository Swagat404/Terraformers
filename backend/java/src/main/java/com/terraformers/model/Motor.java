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

// line 145 "model.ump"
// line 250 "model.ump"

@Entity
@Table(name = "motors")
public class Motor {

    //------------------------
    // ENUMERATIONS
    //------------------------

    public enum MotorPosition { Front, Back, Left, Right }
    public enum MotorStatus { Active, Inactive, Faulty }
    public enum MotorType { TypeX, TypeY }

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    // --- Motor Attributes ---
    @Id
    // Assuming manual ID for now
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "motor_id", nullable = false)
    private int motorID;

    @Column(name = "max_speed")
    private float maxSpeed;

    @Column(name = "power_consumption")
    private float powerConsumption;

    @Enumerated(EnumType.STRING)
    @Column(length = 10) // e.g., Front, Back, Left, Right
    private MotorPosition position;

    @Enumerated(EnumType.STRING)
    @Column(length = 20) // e.g., Active, Inactive, Faulty
    private MotorStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "motor_type", length = 10) // e.g., TypeX, TypeY
    private MotorType motorType;

    // --- Motor Associations ---

    // One Motor has Many MotorReadings. MotorReading entity has the FK.
    @OneToMany(
            mappedBy = "motor",            // Refers to 'motor' field in MotorReading entity
            cascade = CascadeType.ALL,     // Operations on Motor cascade to its readings
            orphanRemoval = true,          // Removing reading from list deletes it
            fetch = FetchType.LAZY
    )
    private List<MotorReading> motorReadings = new ArrayList<>();

    // Many Motors belong to One AvatarBrain. This side owns the Foreign Key.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_brain_id", nullable = false) // FK column in 'motors' table
    private AvatarBrain avatarBrain;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    // JPA requires a no-arg constructor
    public Motor() {
    }

    // Original UMPLE constructor
    public Motor(int aMotorID, float aMaxSpeed, float aPowerConsumption, MotorPosition aPosition, MotorStatus aStatus, MotorType aMotorType, AvatarBrain aAvatarBrain) {
        this.motorID = aMotorID;
        this.maxSpeed = aMaxSpeed;
        this.powerConsumption = aPowerConsumption;
        this.position = aPosition;
        this.status = aStatus;
        this.motorType = aMotorType;
        this.motorReadings = new ArrayList<>(); // Initialize list
        // Use JPA-aware setter
        this.setAvatarBrain(aAvatarBrain);
        // Constructor check (!didAdd...) usually not needed
    }

    //------------------------
    // INTERFACE (Getters/Setters)
    //------------------------
    // Keep standard getters and setters

    public boolean setMotorID(int aMotorID) { this.motorID = aMotorID; return true; }
    public int getMotorID() { return motorID; }

    public boolean setMaxSpeed(float aMaxSpeed) { this.maxSpeed = aMaxSpeed; return true; }
    public float getMaxSpeed() { return maxSpeed; }

    public boolean setPowerConsumption(float aPowerConsumption) { this.powerConsumption = aPowerConsumption; return true; }
    public float getPowerConsumption() { return powerConsumption; }

    public boolean setPosition(MotorPosition aPosition) { this.position = aPosition; return true; }
    public MotorPosition getPosition() { return position; }

    public boolean setStatus(MotorStatus aStatus) { this.status = aStatus; return true; }
    public MotorStatus getStatus() { return status; }

    public boolean setMotorType(MotorType aMotorType) { this.motorType = aMotorType; return true; }
    public MotorType getMotorType() { return motorType; }


    // --- Association Accessors/Mutators (Review/Replace UMPLE versions) ---

    public List<MotorReading> getMotorReadings() {
        return motorReadings; // Direct list for JPA lazy loading
    }
    public int numberOfMotorReadings() { return this.motorReadings.size(); }
    public boolean hasMotorReadings() { return !this.motorReadings.isEmpty(); }
    public int indexOfMotorReading(MotorReading aMotorReading) { return this.motorReadings.indexOf(aMotorReading); }
    public MotorReading getMotorReading(int index) { /* Add bounds check */ return motorReadings.get(index); }
    public static int minimumNumberOfMotorReadings() { return 0; } // Keep if needed for service logic


    public AvatarBrain getAvatarBrain() { return avatarBrain; }

    // --- JPA-aware Add/Remove for MotorReadings collection ---

    public void addMotorReading(MotorReading motorReading) {
        if (motorReading != null && !this.motorReadings.contains(motorReading)) {
            this.motorReadings.add(motorReading);
            if (!this.equals(motorReading.getMotor())) { // Avoid loop
                motorReading.setMotor(this);
            }
        }
    }

    public void removeMotorReading(MotorReading motorReading) {
        if (motorReading != null && this.motorReadings.contains(motorReading)) {
            this.motorReadings.remove(motorReading);
             if (this.equals(motorReading.getMotor())) { // Break other side
                 motorReading.setMotor(null);
             }
        }
        // Note: Original UMPLE logic prevented removal if reading pointed here.
    }

     /**
     * Sets the AvatarBrain for this Motor, maintaining bidirectional consistency.
     * @param newAvatarBrain The AvatarBrain this motor belongs to. Cannot be null.
     * @return boolean true if successful.
     */
    public boolean setAvatarBrain(AvatarBrain newAvatarBrain) {
         if (newAvatarBrain == null) {
             // Decide handling based on requirements (is brain mandatory?)
             // throw new IllegalArgumentException("AvatarBrain cannot be null for Motor");
             return false; // Following original UMPLE check pattern
         }

        // Avoid self-assignment loop / unnecessary work
        if (Objects.equals(this.avatarBrain, newAvatarBrain)) {
            return true;
        }

        // If currently associated with a different brain, remove from its list
        if (this.avatarBrain != null) {
            this.avatarBrain.removeMotor(this); // Use helper on AvatarBrain
        }

        // Set the new brain reference on this motor
        this.avatarBrain = newAvatarBrain;

        // Add this motor to the new brain's list
        this.avatarBrain.addMotor(this); // Use helper on AvatarBrain

        return true;
    }


    // UMPLE factory/index methods usually removed/refactored for JPA
    // public MotorReading addMotorReading(...) { ... }
    // public boolean addMotorReadingAt(...) { ... }
    // public boolean addOrMoveMotorReadingAt(...) { ... }


    // --- Delete Method ---
    // Handled by repository.delete(motor)
    // Cascade settings manage associated entities.
    public void delete() {
         // Break link with AvatarBrain *before* deletion
         if (this.avatarBrain != null) {
             // AvatarBrain placeholderAvatarBrain = avatarBrain; // Use correct reference
             AvatarBrain placeholderAvatarBrain = this.avatarBrain;
             this.avatarBrain = null; // Null internal ref first
             placeholderAvatarBrain.removeMotor(this); // Tell brain to remove this motor
         }
        // CascadeType.ALL on motorReadings handles deleting children
        // No need to loop through motorReadings here if cascade is set
         // super.delete(); // If extending a class with delete()
    }


    // --- equals() and hashCode() ---
     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Motor)) return false;
        Motor motor = (Motor) o;
        if (motorID == 0 && motor.motorID == 0) return this == o;
        return motorID == motor.motorID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(motorID);
    }

    // --- toString() ---
    @Override
    public String toString() {
        return "Motor{" +
                "motorID=" + motorID +
                ", maxSpeed=" + maxSpeed +
                ", powerConsumption=" + powerConsumption +
                ", position=" + position +
                ", status=" + status +
                ", motorType=" + motorType +
                ", avatarBrainId=" + (avatarBrain != null ? avatarBrain.getAvatarBrainID() : "null") +
                '}';
    }
}