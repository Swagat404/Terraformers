package com.terraformers.dto;

import com.terraformers.model.Sensor; // For enums

public class SensorDTO {

    private Integer sensorID; // Use Integer if generated
    private Sensor.SensorMountPosition mountPosition;
    private Sensor.SensorStatus status;
    private Sensor.SensorType sensorType;

    // ID of the associated avatar
    private Integer avatarId;

    // Exclude readings collection

    // --- Constructors ---
    public SensorDTO() {}

    // --- Getters and Setters ---
    public Integer getSensorID() { return sensorID; }
    public void setSensorID(Integer sensorID) { this.sensorID = sensorID; }
    public Sensor.SensorMountPosition getMountPosition() { return mountPosition; }
    public void setMountPosition(Sensor.SensorMountPosition mountPosition) { this.mountPosition = mountPosition; }
    public Sensor.SensorStatus getStatus() { return status; }
    public void setStatus(Sensor.SensorStatus status) { this.status = status; }
    public Sensor.SensorType getSensorType() { return sensorType; }
    public void setSensorType(Sensor.SensorType sensorType) { this.sensorType = sensorType; }
    public Integer getAvatarId() { return avatarId; }
    public void setAvatarId(Integer avatarId) { this.avatarId = avatarId; }

    // --- Optional: equals, hashCode, toString ---
     @Override
    public boolean equals(Object o) { /* ... implement ... */ return false; }
    @Override
    public int hashCode() { /* ... implement ... */ return 0; }
    @Override
    public String toString() { /* ... implement ... */ return ""; }
}