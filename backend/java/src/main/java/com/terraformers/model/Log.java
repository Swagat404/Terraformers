package com.terraformers.model;

// Import JPA and other necessary classes
import java.sql.Date; // Core JPA annotations
import java.sql.Time;
import java.util.Objects;

import javax.persistence.Column; // For Objects.hash/equals
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

// line 186 "model.ump"
// line 272 "model.ump"

@MappedSuperclass // Declares this as a base class whose state is mapped to inheriting entities' tables
public abstract class Log { // Making it abstract is good practice for MappedSuperclass

    //------------------------
    // ENUMERATIONS
    //------------------------

    public enum LogType { Info, Warning, Error }

    //------------------------
    // MEMBER VARIABLES
    //------------------------
    // These fields will be included in the tables for AvatarLog and MissionLog

    @Id // Primary key for log entries (inherited by subclasses)
    // Assuming manual ID setting for now based on original constructor
    // @GeneratedValue(strategy = GenerationType.IDENTITY) // Use if DB generates IDs for log tables
    @Column(name = "log_id", nullable = false)
    private int logID;

    @Column(name = "log_date") // Maps to SQL DATE type
    private Date logDate;

    @Column(name = "log_time") // Maps to SQL TIME type
    private Time logTime;

    @Enumerated(EnumType.STRING) // Store enum name as string
    @Column(name = "log_type", length = 20)
    private LogType logType;

    @Column(name = "log_message", length = 1000) // Example length, adjust as needed
    // @Lob // Uncomment if messages can exceed standard VARCHAR limits
    private String logMessage;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    // Protected no-arg constructor REQUIRED for JPA and subclasses
    protected Log() {}

    // Constructor for subclasses to call via super()
    public Log(int aLogID, Date aLogDate, Time aLogTime, LogType aLogType, String aLogMessage) {
        this.logID = aLogID;
        this.logDate = aLogDate;
        this.logTime = aLogTime;
        this.logType = aLogType;
        this.logMessage = aLogMessage;
    }

    //------------------------
    // INTERFACE (Getters/Setters)
    //------------------------
    // Keep standard getters and setters

    public boolean setLogID(int aLogID) { this.logID = aLogID; return true; }
    public int getLogID() { return logID; }

    public boolean setLogDate(Date aLogDate) { this.logDate = aLogDate; return true; }
    public Date getLogDate() { return logDate; }

    public boolean setLogTime(Time aLogTime) { this.logTime = aLogTime; return true; }
    public Time getLogTime() { return logTime; }

    public boolean setLogType(LogType aLogType) { this.logType = aLogType; return true; }
    public LogType getLogType() { return logType; }

    public boolean setLogMessage(String aLogMessage) { this.logMessage = aLogMessage; return true; }
    public String getLogMessage() { return logMessage; }

    // delete() method in MappedSuperclass usually does nothing specific to persistence
    // Subclasses handle relationship cleanup before calling repository.delete()
    public void delete() {}

    // --- equals() and hashCode() ---
    // Base implementation needed for subclasses
    // Note: Includes getClass() check for correct comparison between different log types
     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Must be exact same class (e.g., AvatarLog==AvatarLog, not AvatarLog==MissionLog)
        // Use instanceof check if polymorphic queries were needed and an Inheritance strategy was used
        if (o == null || getClass() != o.getClass()) return false;
        Log log = (Log) o;
         // Handle transient instances (ID=0)
        if (logID == 0 && log.logID == 0) {
             return this == o; // Or super.equals(o)
        }
        return logID == log.logID; // Compare by ID once persisted
    }

    @Override
    public int hashCode() {
         // Use ID and class for hash code
         // Consistent with equals - different Log types should have different hash codes even with same ID
         return Objects.hash(logID, getClass());
        // Simpler alternative if only ID matters and class is checked in equals:
        // return Objects.hash(logID);
    }

    // --- toString() ---
    // Keep or modify UMPLE version, but avoid associations
     @Override
     public String toString() {
         return getClass().getSimpleName() + "{" + // Show actual subclass type
                 "logID=" + logID +
                 ", logDate=" + logDate +
                 ", logTime=" + logTime +
                 ", logType=" + logType +
                 ", logMessage='" + logMessage + '\'' +
                 '}';
     }
}