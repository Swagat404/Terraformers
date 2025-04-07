package com.terraformers.dto;

import com.terraformers.model.Mission; // For enum

public class MissionDTO {

    private int missionID;
    private String missionName;
    private Mission.MissionStatus status;
    private String objective;
    // Exclude collections

    public MissionDTO() { }

    // Getters/Setters...
    public int getMissionID() { return missionID; }
    public void setMissionID(int missionID) { this.missionID = missionID; }
    public String getMissionName() { return missionName; }
    public void setMissionName(String missionName) { this.missionName = missionName; }
    public Mission.MissionStatus getStatus() { return status; }
    public void setStatus(Mission.MissionStatus status) { this.status = status; }
    public String getObjective() { return objective; }
    public void setObjective(String objective) { this.objective = objective; }

    // equals, hashCode, toString...
    @Override
    public boolean equals(Object o) { /* ... implement ... */ return false; }
    @Override
    public int hashCode() { /* ... implement ... */ return 0; }
    @Override
    public String toString() { /* ... implement ... */ return ""; }
}