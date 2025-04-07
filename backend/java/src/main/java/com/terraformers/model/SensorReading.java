package com.terraformers.model;

// Import JPA and other necessary classes
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

// line 169 "model.ump"
// line 262 "model.ump"

@Entity
@Table(name = "sensor_readings") // Table for this specific reading type
public class SensorReading extends Reading { // Inherits fields and ID from Reading (@MappedSuperclass)

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    // --- SensorReading Attributes ---
    @Column(name = "value") // Map to column, name matches field name here
    private float value;

    // --- SensorReading Associations ---
    // Many SensorReadings belong to One Sensor. This side owns the Foreign Key.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id", nullable = false) // Specifies the FK column in 'sensor_readings' table
    private Sensor sensor;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    // JPA requires a no-arg constructor
    public SensorReading() {
        super(); // Call super if needed
    }

    // Original UMPLE constructor
    public SensorReading(int aReadingID, LocalDateTime aTimeStamp, float aValue, Sensor aSensor) {
        super(aReadingID, aTimeStamp); // Initialize inherited fields
        this.value = aValue;
        // Use JPA-aware setter
        this.setSensor(aSensor);
        // Constructor check (!didAdd...) usually not needed
    }

    //------------------------
    // INTERFACE (Getters/Setters)
    //------------------------
    // Keep standard getters and setters

    public boolean setValue(float aValue) { this.value = aValue; return true; }
    public float getValue() { return value; }

    /* Code from template association_GetOne */
    public Sensor getSensor() {
        return sensor;
    }

    /* Code from template association_SetOneToMany */
     /**
     * Sets the Sensor for this reading, maintaining bidirectional consistency.
     * @param newSensor The Sensor this reading belongs to. Cannot be null.
     * @return boolean true if successful.
     */
    public boolean setSensor(Sensor newSensor) {
         if (newSensor == null) {
             // Decide handling (is sensor mandatory?)
             // throw new IllegalArgumentException("Sensor cannot be null for SensorReading");
             return false; // Following original UMPLE check pattern
         }

        // Avoid self-assignment loop / unnecessary work
        if (Objects.equals(this.sensor, newSensor)) {
            return true;
        }

        // If currently associated with a different sensor, remove from its list
        if (this.sensor != null) {
            this.sensor.removeSensorReading(this); // Use helper on Sensor
        }

        // Set the new sensor reference on this reading
        this.sensor = newSensor;

        // Add this reading to the new sensor's list
        this.sensor.addSensorReading(this); // Use helper on Sensor

        return true;
    }

    // --- Delete Method ---
    @Override // Override if Reading has delete()
    public void delete() {
        // Break the link with the Sensor *before* deletion
        if (this.sensor != null) {
            // Sensor placeholderSensor = sensor; // Use correct reference
            Sensor placeholderSensor = this.sensor;
            this.sensor = null; // Null internal reference first
            placeholderSensor.removeSensorReading(this); // Tell Sensor to remove this reading
        }
        // super.delete(); // Call super if it does anything
        // Actual DB deletion handled by repository.delete()
    }

    // equals() and hashCode() are inherited from Reading (based on readingID and Class)

    // toString() can be inherited or overridden
    @Override
    public String toString() {
        return super.toString() + // Includes Reading fields
               " - SensorReading{" +
               "value=" + value +
               ", sensorId=" + (sensor != null ? sensor.getSensorID() : "null") +
               '}';
    }
}