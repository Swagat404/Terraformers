package com.terraformers.dto;

import java.sql.Date; // For enum
import java.sql.Time;

import com.terraformers.model.Log;

public class MissionLogDTO {
    private Integer logID; // Use Integer if generated
    private Date logDate;
    private Time logTime;
    private Log.LogType logType;
    private String logMessage;
    private Integer missionId; // ID of the associated mission

    // Constructors, Getters, Setters, equals, hashCode, toString...
    public MissionLogDTO() {}

    // Getters and Setters...
    public Integer getLogID() { return logID; }
    public void setLogID(Integer logID) { this.logID = logID; }
    public Date getLogDate() { return logDate; }
    public void setLogDate(Date logDate) { this.logDate = logDate; }
    public Time getLogTime() { return logTime; }
    public void setLogTime(Time logTime) { this.logTime = logTime; }
    public Log.LogType getLogType() { return logType; }
    public void setLogType(Log.LogType logType) { this.logType = logType; }
    public String getLogMessage() { return logMessage; }
    public void setLogMessage(String logMessage) { this.logMessage = logMessage; }
    public Integer getMissionId() { return missionId; }
    public void setMissionId(Integer missionId) { this.missionId = missionId; }

     @Override
    public boolean equals(Object o) { /* ... implement ... */ return false;}
    @Override
    public int hashCode() { /* ... implement ... */ return 0; }
    @Override
    public String toString() { /* ... implement ... */ return ""; }
}