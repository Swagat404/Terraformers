package com.terraformers.model;

// Import JPA and other necessary classes
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

// line 177 "model.ump"
// line 267 "model.ump"

@Entity
@Table(name = "motor_readings") // Table for this specific reading type
public class MotorReading extends Reading { // Inherits fields and ID from Reading (@MappedSuperclass)

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    // --- MotorReading Attributes ---
    @Column(name = "current_speed")
    private float currentSpeed;

    @Column(length = 50) // Example length for direction string
    private String direction;

    @Column(name = "current_power")
    private float currentPower;

    // --- MotorReading Associations ---
    // Many MotorReadings belong to One Motor. This side owns the Foreign Key.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "motor_id", nullable = false) // Specifies the FK column in 'motor_readings' table
    private Motor motor;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    // JPA requires a no-arg constructor
    public MotorReading() {
        super(); // Call super if needed
    }

    // Original UMPLE constructor
    public MotorReading(int aReadingID, LocalDateTime aTimeStamp, float aCurrentSpeed, String aDirection, float aCurrentPower, Motor aMotor) {
        super(aReadingID, aTimeStamp); // Initialize inherited fields
        this.currentSpeed = aCurrentSpeed;
        this.direction = aDirection;
        this.currentPower = aCurrentPower;
        // Use JPA-aware setter
        this.setMotor(aMotor);
        // Constructor check (!didAdd...) usually not needed
    }

    //------------------------
    // INTERFACE (Getters/Setters)
    //------------------------
    // Keep standard getters and setters

    public boolean setCurrentSpeed(float aCurrentSpeed) { this.currentSpeed = aCurrentSpeed; return true; }
    public float getCurrentSpeed() { return currentSpeed; }

    public boolean setDirection(String aDirection) { this.direction = aDirection; return true; }
    public String getDirection() { return direction; }

    public boolean setCurrentPower(float aCurrentPower) { this.currentPower = aCurrentPower; return true; }
    public float getCurrentPower() { return currentPower; }

    /* Code from template association_GetOne */
    public Motor getMotor() {
        return motor;
    }

    /* Code from template association_SetOneToMany */
    /**
     * Sets the Motor for this reading, maintaining bidirectional consistency.
     * @param newMotor The Motor this reading belongs to. Cannot be null.
     * @return boolean true if successful.
     */
    public boolean setMotor(Motor newMotor) {
         if (newMotor == null) {
             // Decide handling based on requirements (is motor mandatory?)
             // throw new IllegalArgumentException("Motor cannot be null for MotorReading");
             return false; // Following original UMPLE check pattern
         }

        // Avoid self-assignment loop / unnecessary work
        if (Objects.equals(this.motor, newMotor)) {
            return true;
        }

        // If currently associated with a different motor, remove from its list
        if (this.motor != null) {
            this.motor.removeMotorReading(this); // Use helper on Motor
        }

        // Set the new motor reference on this reading
        this.motor = newMotor;

        // Add this reading to the new motor's list
        this.motor.addMotorReading(this); // Use helper on Motor

        return true;
    }

    // --- Delete Method ---
    @Override // Override if Reading has delete()
    public void delete() {
        // Break the link with the Motor *before* deletion
        if (this.motor != null) {
            // Motor placeholderMotor = motor; // Use correct reference
            Motor placeholderMotor = this.motor;
            this.motor = null; // Null internal reference first
            placeholderMotor.removeMotorReading(this); // Tell Motor to remove this reading
        }
        // super.delete(); // Call super if it does anything
        // Actual DB deletion handled by repository.delete()
    }

    // equals() and hashCode() are inherited from Reading (based on readingID and Class)

    // toString() can be inherited or overridden
    @Override
    public String toString() {
        return super.toString() + // Includes Reading fields
               " - MotorReading{" +
               "currentSpeed=" + currentSpeed +
               ", direction='" + direction + '\'' +
               ", currentPower=" + currentPower +
               ", motorId=" + (motor != null ? motor.getMotorID() : "null") +
               '}';
    }
}