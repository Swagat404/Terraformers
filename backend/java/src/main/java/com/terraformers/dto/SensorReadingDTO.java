package com.terraformers.dto;

import java.time.LocalDateTime;

// DTO for SensorReading API data transfer
public class SensorReadingDTO {

    private Integer readingID; // Use Integer if generated
    private LocalDateTime timeStamp;
    private float value;

    // ID of the associated sensor
    private Integer sensorId;

    // --- Constructors ---
    public SensorReadingDTO() {}

    // --- Getters and Setters ---
    public Integer getReadingID() { return readingID; }
    public void setReadingID(Integer readingID) { this.readingID = readingID; }
    public LocalDateTime getTimeStamp() { return timeStamp; }
    public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }
    public float getValue() { return value; }
    public void setValue(float value) { this.value = value; }
    public Integer getSensorId() { return sensorId; }
    public void setSensorId(Integer sensorId) { this.sensorId = sensorId; }

    // --- Optional: equals, hashCode, toString ---
     @Override
    public boolean equals(Object o) { /* ... implement ... */ return false; }
    @Override
    public int hashCode() { /* ... implement ... */ return 0; }
    @Override
    public String toString() { /* ... implement ... */ return ""; }
}