package com.terraformers.model;

// Import JPA and other necessary classes
import java.time.LocalDateTime; // Core JPA annotations
import java.util.Objects;

import javax.persistence.Column; // For Objects.hash/equals
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

// line 195 "model.ump"
// line 277 "model.ump"

@MappedSuperclass // Base class, fields mapped to inheriting entities' tables
public abstract class Reading { // Make abstract

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    // Reading Attributes - will be part of MotorReading/SensorReading tables

    @Id // Primary key for readings (inherited by subclasses)
    // Assuming manual ID setting for now
    // @GeneratedValue(strategy = GenerationType.IDENTITY) // Use if DB generates IDs
    @Column(name = "reading_id", nullable = false)
    private int readingID;

    // JPA automatically maps LocalDateTime to appropriate SQL TIMESTAMP type
    @Column(name = "time_stamp")
    private LocalDateTime timeStamp;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    // Protected no-arg constructor REQUIRED for JPA and subclasses
    protected Reading() {}

    public Reading(int aReadingID, LocalDateTime aTimeStamp) {
        this.readingID = aReadingID;
        this.timeStamp = aTimeStamp;
    }

    //------------------------
    // INTERFACE (Getters/Setters)
    //------------------------
    // Keep standard getters and setters

    public boolean setReadingID(int aReadingID) { this.readingID = aReadingID; return true; }
    public int getReadingID() { return readingID; }

    public boolean setTimeStamp(LocalDateTime aTimeStamp) { this.timeStamp = aTimeStamp; return true; }
    public LocalDateTime getTimeStamp() { return timeStamp; }

    // delete() method in MappedSuperclass usually does nothing specific to persistence
    public void delete() {}


    // --- equals() and hashCode() ---
    // Base implementation needed for subclasses
    // Includes getClass() check for correct comparison between different reading types
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Must be exact same class (e.g., MotorReading==MotorReading)
        if (o == null || getClass() != o.getClass()) return false;
        Reading reading = (Reading) o;
        // Handle transient instances (ID=0)
        if (readingID == 0 && reading.readingID == 0) {
             return this == o; // Or super.equals(o)
        }
        return readingID == reading.readingID; // Compare by ID
    }

    @Override
    public int hashCode() {
        // Consistent with equals - includes class to differentiate Reading types
        return Objects.hash(readingID, getClass());
        // Simpler alternative if only ID matters and class checked in equals:
        // return Objects.hash(readingID);
    }

    // --- toString() ---
    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + // Show actual subclass type
                "readingID=" + readingID +
                ", timeStamp=" + timeStamp +
                '}';
    }
}