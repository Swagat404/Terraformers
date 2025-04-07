package com.terraformers.model;

// Import JPA and other necessary classes
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

// line 104 "model.ump"
// line 225 "model.ump"

@Entity
@Table(name = "mission_logs") // Table for this specific log type
public class MissionLog extends Log { // Inherits fields and ID from Log (@MappedSuperclass)

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    // --- MissionLog Associations ---
    // Many MissionLogs belong to One Mission. This side owns the Foreign Key.
    @ManyToOne(fetch = FetchType.LAZY) // Lazy load the associated Mission
    @JoinColumn(name = "mission_id", nullable = false) // Specifies the FK column in 'mission_logs' table
    private Mission mission;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    // JPA requires a no-arg constructor
    public MissionLog() {
        super(); // Call super constructor if needed/available
    }

    // Original UMPLE constructor
    public MissionLog(int aLogID, Date aLogDate, Time aLogTime, LogType aLogType, String aLogMessage, Mission aMission) {
        super(aLogID, aLogDate, aLogTime, aLogType, aLogMessage); // Initialize inherited fields
        // Use JPA-aware setter to establish links
        this.setMission(aMission);
        // The check (!didAdd...) is usually not needed with JPA setters/service logic
    }

    //------------------------
    // INTERFACE
    //------------------------

    /* Code from template association_GetOne */
    public Mission getMission() {
        return mission;
    }

    /* Code from template association_SetOneToMany */
    // Renamed method for clarity (JPA standard) and corrected logic
    /**
     * Sets the Mission for this log, maintaining bidirectional consistency.
     * @param newMission The Mission this log belongs to. Cannot be null.
     * @return boolean true if successful.
     */
    public boolean setMission(Mission newMission) {
         if (newMission == null) {
             // Decide handling: throw exception? return false?
             // throw new IllegalArgumentException("Mission cannot be null for MissionLog");
             return false; // Following original UMPLE check pattern
         }

        // Avoid self-assignment loop / unnecessary work
        if (Objects.equals(this.mission, newMission)) {
            return true;
        }

        // If currently associated with a different mission, remove from its list
        if (this.mission != null) {
            this.mission.removeMissionLog(this); // Use helper on Mission
        }

        // Set the new mission reference on this log
        this.mission = newMission;

        // Add this log to the new mission's list
        this.mission.addMissionLog(this); // Use helper on Mission

        return true;
    }


    // --- Delete Method ---
    // UMPLE delete logic needs review for JPA.
    @Override // Override if Log has a delete method
    public void delete() {
        // Break the link with the Mission *before* deleting this log
        if (this.mission != null) {
            // Store placeholder ONLY if needed for further logic AFTER nulling internal ref
            // Mission placeholderMission = mission;
            Mission placeholderMission = this.mission; // Correct reference
            this.mission = null; // Null internal reference first
            placeholderMission.removeMissionLog(this); // Tell Mission to remove this log
        }
        // super.delete(); // Call super if it does anything meaningful
        // Actual DB deletion is handled by repository.delete()
    }

    // equals() and hashCode() are inherited from Log (based on logID and Class)

    // toString() can be inherited or overridden if needed
    @Override
    public String toString() {
        return super.toString() + // Includes Log fields
               " - MissionLog{" +
               "missionId=" + (mission != null ? mission.getMissionID() : "null") +
               '}';
    }
}