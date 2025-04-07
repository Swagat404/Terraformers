package com.terraformers.dto;
import com.terraformers.model.Avatar;

public class AvatarDTO {
    private Integer avatarID;
    private String avatarName;
    private Avatar.AvatarColor avatarColor;
    private Integer locationId;
    private Integer avatarBrainId;
    private Integer missionId;
    // Exclude collections

    public AvatarDTO() {}
    // Getters and Setters...
    public Integer getAvatarID() { return avatarID; }
    public void setAvatarID(Integer avatarID) { this.avatarID = avatarID; }
    public String getAvatarName() { return avatarName; }
    public void setAvatarName(String avatarName) { this.avatarName = avatarName; }
    public Avatar.AvatarColor getAvatarColor() { return avatarColor; }
    public void setAvatarColor(Avatar.AvatarColor avatarColor) { this.avatarColor = avatarColor; }
    public Integer getLocationId() { return locationId; }
    public void setLocationId(Integer locationId) { this.locationId = locationId; }
    public Integer getAvatarBrainId() { return avatarBrainId; }
    public void setAvatarBrainId(Integer avatarBrainId) { this.avatarBrainId = avatarBrainId; }
    public Integer getMissionId() { return missionId; }
    public void setMissionId(Integer missionId) { this.missionId = missionId; }

    @Override
    public boolean equals(Object o) { /* ... implement ... */ return false; }
    @Override
    public int hashCode() { /* ... implement ... */ return 0; }
    @Override
    public String toString() { /* ... implement ... */ return ""; }
}