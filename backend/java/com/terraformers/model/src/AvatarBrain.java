package com.terraformers.model.src;

import org.json.JSONObject;

/**
 * Represents the brain/AI controller for Mars avatars
 */
public class AvatarBrain {
    private int brainId;
    private String brainType;
    private int maxSpeed;
    private int maxJumpHeight;
    private String dimensions; // Stored as JSON string

    // Default constructor
    public AvatarBrain() {}

    // Constructor for new records
    public AvatarBrain(String brainType, int maxSpeed, int maxJumpHeight, String dimensions) {
        this.brainType = brainType;
        this.maxSpeed = maxSpeed;
        this.maxJumpHeight = maxJumpHeight;
        this.dimensions = dimensions;
    }

    // Getters and setters
    public int getBrainId() { return brainId; }
    public void setBrainId(int brainId) { this.brainId = brainId; }
    
    public String getBrainType() { return brainType; }
    public void setBrainType(String brainType) { this.brainType = brainType; }
    
    public int getMaxSpeed() { return maxSpeed; }
    public void setMaxSpeed(int maxSpeed) { this.maxSpeed = maxSpeed; }
    
    public int getMaxJumpHeight() { return maxJumpHeight; }
    public void setMaxJumpHeight(int maxJumpHeight) { this.maxJumpHeight = maxJumpHeight; }
    
    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }

    @Override
    public String toString() {
        return "AvatarBrain{" +
                "brainId=" + brainId +
                ", brainType='" + brainType + '\'' +
                ", maxSpeed=" + maxSpeed +
                ", maxJumpHeight=" + maxJumpHeight +
                ", dimensions='" + dimensions + '\'' +
                '}';
    }
}
