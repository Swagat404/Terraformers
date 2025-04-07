package com.terraformers.model;

// Import JPA and other necessary classes
import java.util.ArrayList;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;

// line 132 "model.ump"
// line 244 "model.ump"

@Entity
@Table(name = "avatar_brains") // Use plural and underscore convention
public class AvatarBrain {

    //------------------------
    // ENUMERATIONS
    //------------------------
    // Keep enums as defined

    public enum AvatarBrainType { TypeA, TypeB }
    // Note: Motor enums belong in Motor.java
    // public enum MotorPosition { Front, Back, Left, Right }
    // public enum MotorStatus { Active, Inactive, Faulty }
    // public enum MotorType { TypeX, TypeY }

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    // --- AvatarBrain Attributes ---
    @Id
    // Assuming manual ID for now
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avatar_brain_id", nullable = false)
    private int avatarBrainID;

    @Enumerated(EnumType.STRING)
    @Column(name = "avatar_brain_type", length = 20)
    private AvatarBrainType avatarBrainType;

    @Column(name = "avatar_speed")
    private int avatarSpeed;

    @Column(name = "avatar_max_jump_height")
    private int avatarMaxJumpHeight;

    @Column(name = "avatar_length")
    private float avatarLength;

    @Column(name = "avatar_width")
    private float avatarWidth;

    @Column(name = "avatar_height")
    private float avatarHeight;

    // --- AvatarBrain Associations ---

    // One AvatarBrain has Many Motors. Motor entity owns the relationship.
    @OneToMany(
            mappedBy = "avatarBrain",       // Refers to 'avatarBrain' field in Motor entity
            cascade = CascadeType.ALL,     // Operations on brain cascade to motors
            orphanRemoval = true,          // Removing motor from list deletes it
            fetch = FetchType.LAZY           // Lazy load motors
    )
    private List<Motor> motors = new ArrayList<>();

    // One-to-One with Avatar. Mapped by the 'avatarBrain' field in the Avatar entity.
    // Avatar entity owns the foreign key column.
    @OneToOne(mappedBy = "avatarBrain",   // Refers to 'avatarBrain' field in Avatar entity
              fetch = FetchType.LAZY,
              cascade = {CascadeType.PERSIST, CascadeType.MERGE}) // Decide appropriate cascade from Brain -> Avatar
    private Avatar avatar;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    // JPA requires a no-arg constructor
    public AvatarBrain() {
    }

    // Original UMPLE constructor
    public AvatarBrain(int aAvatarBrainID, AvatarBrainType aAvatarBrainType, int aAvatarSpeed, int aAvatarMaxJumpHeight, float aAvatarLength, float aAvatarWidth, float aAvatarHeight) {
        this.avatarBrainID = aAvatarBrainID;
        this.avatarBrainType = aAvatarBrainType;
        this.avatarSpeed = aAvatarSpeed;
        this.avatarMaxJumpHeight = aAvatarMaxJumpHeight;
        this.avatarLength = aAvatarLength;
        this.avatarWidth = aAvatarWidth;
        this.avatarHeight = aAvatarHeight;
        this.motors = new ArrayList<>(); // Initialize list
        // Avatar association starts null
    }

    //------------------------
    // INTERFACE (Getters/Setters)
    //------------------------
    // Keep standard getters and setters

    public boolean setAvatarBrainID(int aAvatarBrainID) { this.avatarBrainID = aAvatarBrainID; return true; }
    public int getAvatarBrainID() { return avatarBrainID; }

    public boolean setAvatarBrainType(AvatarBrainType aAvatarBrainType) { this.avatarBrainType = aAvatarBrainType; return true; }
    public AvatarBrainType getAvatarBrainType() { return avatarBrainType; }

    public boolean setAvatarSpeed(int aAvatarSpeed) { this.avatarSpeed = aAvatarSpeed; return true; }
    public int getAvatarSpeed() { return avatarSpeed; }

    public boolean setAvatarMaxJumpHeight(int aAvatarMaxJumpHeight) { this.avatarMaxJumpHeight = aAvatarMaxJumpHeight; return true; }
    public int getAvatarMaxJumpHeight() { return avatarMaxJumpHeight; }

    public boolean setAvatarLength(float aAvatarLength) { this.avatarLength = aAvatarLength; return true; }
    public float getAvatarLength() { return avatarLength; }

    public boolean setAvatarWidth(float aAvatarWidth) { this.avatarWidth = aAvatarWidth; return true; }
    public float getAvatarWidth() { return avatarWidth; }

    public boolean setAvatarHeight(float aAvatarHeight) { this.avatarHeight = aAvatarHeight; return true; }
    public float getAvatarHeight() { return avatarHeight; }


    // --- Association Accessors/Mutators (Review/Replace UMPLE versions) ---

    public List<Motor> getMotors() {
        // return Collections.unmodifiableList(motors); // Use if strict immutability needed outside
        return motors; // Direct access for JPA lazy loading
    }
    public int numberOfMotors() { return this.motors.size(); }
    public boolean hasMotors() { return !this.motors.isEmpty(); }
    public int indexOfMotor(Motor aMotor) { return this.motors.indexOf(aMotor); }
    public Motor getMotor(int index) { /* Add bounds check */ return motors.get(index); }
    public static int minimumNumberOfMotors() { return 0; } // Keep if needed for service logic

    public Avatar getAvatar() { return avatar; }
    public boolean hasAvatar() { return avatar != null; }

    // --- JPA-aware Add/Remove for Motors collection ---

    public void addMotor(Motor motor) {
        if (motor != null && !this.motors.contains(motor)) {
            this.motors.add(motor);
            // Set the other side of the relationship
            if (!this.equals(motor.getAvatarBrain())) { // Avoid loop
                 motor.setAvatarBrain(this);
            }
        }
    }

    public void removeMotor(Motor motor) {
        if (motor != null && this.motors.contains(motor)) {
            this.motors.remove(motor);
             // Break the other side of the relationship if necessary
             if (this.equals(motor.getAvatarBrain())) {
                motor.setAvatarBrain(null);
             }
        }
        // Note: The original UMPLE removeMotor logic was different, preventing removal
        // if the motor still pointed here. This JPA version allows removal from the list,
        // and separately breaks the link on the Motor side. Business rules about whether
        // a motor *must* have a brain would be enforced elsewhere (e.g., DB constraints, Service).
    }


    /**
     * Sets the Avatar for this AvatarBrain, managing the bidirectional OneToOne link.
     * Uses the corrected logic without the flawed orphan check.
     */
     // Keep the corrected setAvatar method you pasted previously
     public boolean setAvatar(Avatar aNewAvatar) {
        boolean wasSet = false;
        Avatar existingAvatar = avatar;
        avatar = aNewAvatar;
        AvatarBrain anOldAvatarBrain = aNewAvatar != null ? aNewAvatar.getAvatarBrain() : null;
        if (!this.equals(anOldAvatarBrain)) {
            if (anOldAvatarBrain != null) {
                anOldAvatarBrain.avatar = null;
            }
            if (avatar != null) {
                avatar.setAvatarBrain(this);
            }
        }
        wasSet = true;
        return wasSet;
    }


    // UMPLE factory/index methods for motors usually removed for JPA
    // public Motor addMotor(...) { ... }
    // public boolean addMotorAt(...) { ... }
    // public boolean addOrMoveMotorAt(...) { ... }

    // --- Delete Method ---
    // Handled by repository.delete(avatarBrain)
    // CascadeType.ALL on motors/avatar handles children.
    // public void delete() { ... }

    // --- equals() and hashCode() ---
     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AvatarBrain)) return false;
        AvatarBrain that = (AvatarBrain) o;
        if (avatarBrainID == 0 && that.avatarBrainID == 0) return this == o;
        return avatarBrainID == that.avatarBrainID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(avatarBrainID);
    }


    // --- toString() ---
    @Override
    public String toString() {
        return "AvatarBrain{" +
                "avatarBrainID=" + avatarBrainID +
                ", avatarBrainType=" + avatarBrainType +
                ", avatarSpeed=" + avatarSpeed +
                ", avatarMaxJumpHeight=" + avatarMaxJumpHeight +
                // Avoid printing collections or associated objects to prevent lazy loading issues
                ", avatarId=" + (avatar != null ? avatar.getAvatarID() : "null") +
                '}';
    }
}