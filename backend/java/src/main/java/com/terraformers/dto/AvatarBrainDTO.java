package com.terraformers.dto;

import com.terraformers.model.AvatarBrain; // For enum

public class AvatarBrainDTO {

    private Integer avatarBrainID; // Use Integer if generated
    private AvatarBrain.AvatarBrainType avatarBrainType;
    private int avatarSpeed;
    private int avatarMaxJumpHeight;
    private float avatarLength;
    private float avatarWidth;
    private float avatarHeight;

    // ID of the associated avatar (if any)
    private Integer avatarId;

    // Exclude motors collection

    // --- Constructors ---
    public AvatarBrainDTO() {}

    // --- Getters and Setters ---
    public Integer getAvatarBrainID() { return avatarBrainID; }
    public void setAvatarBrainID(Integer avatarBrainID) { this.avatarBrainID = avatarBrainID; }
    public AvatarBrain.AvatarBrainType getAvatarBrainType() { return avatarBrainType; }
    public void setAvatarBrainType(AvatarBrain.AvatarBrainType avatarBrainType) { this.avatarBrainType = avatarBrainType; }
    public int getAvatarSpeed() { return avatarSpeed; }
    public void setAvatarSpeed(int avatarSpeed) { this.avatarSpeed = avatarSpeed; }
    public int getAvatarMaxJumpHeight() { return avatarMaxJumpHeight; }
    public void setAvatarMaxJumpHeight(int avatarMaxJumpHeight) { this.avatarMaxJumpHeight = avatarMaxJumpHeight; }
    public float getAvatarLength() { return avatarLength; }
    public void setAvatarLength(float avatarLength) { this.avatarLength = avatarLength; }
    public float getAvatarWidth() { return avatarWidth; }
    public void setAvatarWidth(float avatarWidth) { this.avatarWidth = avatarWidth; }
    public float getAvatarHeight() { return avatarHeight; }
    public void setAvatarHeight(float avatarHeight) { this.avatarHeight = avatarHeight; }
    public Integer getAvatarId() { return avatarId; }
    public void setAvatarId(Integer avatarId) { this.avatarId = avatarId; }

    // --- Optional: equals, hashCode, toString ---
    @Override
    public boolean equals(Object o) { /* ... implement based on fields ... */ return false;}
    @Override
    public int hashCode() { /* ... implement based on fields ... */ return 0; }
    @Override
    public String toString() { /* ... implement ... */ return ""; }
}