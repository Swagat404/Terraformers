package com.terraformers.dto;

import com.terraformers.model.Motor; // For enums

public class MotorDTO {

    private Integer motorID; // Use Integer if generated
    private float maxSpeed;
    private float powerConsumption;
    private Motor.MotorPosition position;
    private Motor.MotorStatus status;
    private Motor.MotorType motorType;

    // ID of the associated brain
    private Integer avatarBrainId;

    // Exclude readings collection

    // --- Constructors ---
    public MotorDTO() {}

    // --- Getters and Setters ---
    public Integer getMotorID() { return motorID; }
    public void setMotorID(Integer motorID) { this.motorID = motorID; }
    public float getMaxSpeed() { return maxSpeed; }
    public void setMaxSpeed(float maxSpeed) { this.maxSpeed = maxSpeed; }
    public float getPowerConsumption() { return powerConsumption; }
    public void setPowerConsumption(float powerConsumption) { this.powerConsumption = powerConsumption; }
    public Motor.MotorPosition getPosition() { return position; }
    public void setPosition(Motor.MotorPosition position) { this.position = position; }
    public Motor.MotorStatus getStatus() { return status; }
    public void setStatus(Motor.MotorStatus status) { this.status = status; }
    public Motor.MotorType getMotorType() { return motorType; }
    public void setMotorType(Motor.MotorType motorType) { this.motorType = motorType; }
    public Integer getAvatarBrainId() { return avatarBrainId; }
    public void setAvatarBrainId(Integer avatarBrainId) { this.avatarBrainId = avatarBrainId; }

    // --- Optional: equals, hashCode, toString ---
    @Override
    public boolean equals(Object o) { /* ... implement ... */ return false; }
    @Override
    public int hashCode() { /* ... implement ... */ return 0; }
    @Override
    public String toString() { /* ... implement ... */ return ""; }
}