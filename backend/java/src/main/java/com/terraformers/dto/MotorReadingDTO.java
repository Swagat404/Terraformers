package com.terraformers.dto;

import java.time.LocalDateTime;

public class MotorReadingDTO {
    private Integer readingID; // Use Integer if generated
    private LocalDateTime timeStamp;
    private float currentSpeed;
    private String direction;
    private float currentPower;
    private Integer motorId; // ID of the associated motor

    // Constructors, Getters, Setters, equals, hashCode, toString...
    public MotorReadingDTO() {}

    // Getters and Setters...
    public Integer getReadingID() { return readingID; }
    public void setReadingID(Integer readingID) { this.readingID = readingID; }
    public LocalDateTime getTimeStamp() { return timeStamp; }
    public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }
    public float getCurrentSpeed() { return currentSpeed; }
    public void setCurrentSpeed(float currentSpeed) { this.currentSpeed = currentSpeed; }
    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }
    public float getCurrentPower() { return currentPower; }
    public void setCurrentPower(float currentPower) { this.currentPower = currentPower; }
    public Integer getMotorId() { return motorId; }
    public void setMotorId(Integer motorId) { this.motorId = motorId; }

     @Override
    public boolean equals(Object o) { /* ... implement ... */ return false;}
    @Override
    public int hashCode() { /* ... implement ... */ return 0; }
    @Override
    public String toString() { /* ... implement ... */ return ""; }
}