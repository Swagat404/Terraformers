package com.terraformers.model;

// Import JPA and other necessary classes
import java.util.ArrayList;
import java.util.List;
import java.util.Objects; // Keep if used by factory method

import javax.persistence.CascadeType; // Keep if used by factory method
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

// line 93 "model.ump"
// line 217 "model.ump"

@Entity
@Table(name = "missions")
public class Mission {

    //------------------------
    // ENUMERATIONS
    //------------------------

    public enum MissionStatus { Planned, Ongoing, Failed, Completed, Aborted }
    // Note: AvatarColor enum belongs in Avatar.java
    // public enum AvatarColor { Red, Blue, Green, Yellow }

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    // --- Mission Attributes ---
    @Id
    // Assuming manual ID for now
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id", nullable = false)
    private int missionID;

    @Column(name = "mission_name", length = 255)
    private String missionName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private MissionStatus status;

    @Column(length = 1000) // Example length
    // @Lob // Use if objective can be very long
    private String objective;

    // --- Mission Associations ---

    // One Mission has Many MissionLogs. MissionLog entity has the FK.
    @OneToMany(
            mappedBy = "mission",          // Refers to 'mission' field in MissionLog entity
            cascade = CascadeType.ALL,     // Operations on Mission cascade to its logs
            orphanRemoval = true,          // Removing log from list deletes it
            fetch = FetchType.LAZY
    )
    private List<MissionLog> missionLogs = new ArrayList<>();

    // One Mission has Many Avatars. Avatar entity has the FK.
    @OneToMany(
            mappedBy = "mission",          // Refers to 'mission' field in Avatar entity
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}, // Choose cascade carefully - maybe don't delete Avatars when Mission deleted?
            fetch = FetchType.LAZY
    )
    private List<Avatar> avatars = new ArrayList<>();

    //------------------------
    // CONSTRUCTOR
    //------------------------

    // JPA requires a no-arg constructor
    public Mission() {
    }

    // Original UMPLE constructor
    public Mission(int aMissionID, String aMissionName, MissionStatus aStatus, String aObjective) {
        this.missionID = aMissionID;
        this.missionName = aMissionName;
        this.status = aStatus;
        this.objective = aObjective;
        this.missionLogs = new ArrayList<>(); // Initialize lists
        this.avatars = new ArrayList<>();
    }

    //------------------------
    // INTERFACE (Getters/Setters)
    //------------------------
    // Standard getters and setters needed

    public boolean setMissionID(int aMissionID) { this.missionID = aMissionID; return true; }
    public int getMissionID() { return missionID; }

    public boolean setMissionName(String aMissionName) { this.missionName = aMissionName; return true; }
    public String getMissionName() { return missionName; }

    public boolean setStatus(MissionStatus aStatus) { this.status = aStatus; return true; }
    public MissionStatus getStatus() { return status; }

    public boolean setObjective(String aObjective) { this.objective = aObjective; return true; }
    public String getObjective() { return objective; }


    // --- Association Accessors/Mutators (Review/Replace UMPLE versions) ---

    // List getters - return direct list for JPA lazy loading
    public List<MissionLog> getMissionLogs() { return missionLogs; }
    public List<Avatar> getAvatars() { return avatars; }

    // List query methods (UMPL E) - Fine as they are
    public int numberOfMissionLogs() { return missionLogs.size(); }
    public boolean hasMissionLogs() { return !missionLogs.isEmpty(); }
    public int indexOfMissionLog(MissionLog aMissionLog) { return missionLogs.indexOf(aMissionLog); }
    public MissionLog getMissionLog(int index) { /* Add bounds check */ return missionLogs.get(index); }

    public int numberOfAvatars() { return avatars.size(); }
    public boolean hasAvatars() { return !avatars.isEmpty(); }
    public int indexOfAvatar(Avatar aAvatar) { return avatars.indexOf(aAvatar); }
    public Avatar getAvatar(int index) { /* Add bounds check */ return avatars.get(index); }

    // Static minimum methods - Keep if used for service layer validation
    public static int minimumNumberOfMissionLogs() { return 0; }
    public static int minimumNumberOfAvatars() { return 0; }


    // --- JPA-aware Add/Remove for Collections ---

    public void addMissionLog(MissionLog missionLog) {
        if (missionLog != null && !this.missionLogs.contains(missionLog)) {
            this.missionLogs.add(missionLog);
            if (!this.equals(missionLog.getMission())) { // Avoid loop
                missionLog.setMission(this);
            }
        }
    }

    public void removeMissionLog(MissionLog missionLog) {
        if (missionLog != null && this.missionLogs.contains(missionLog)) {
            this.missionLogs.remove(missionLog);
            if (this.equals(missionLog.getMission())) { // Break other side
                missionLog.setMission(null);
            }
        }
         // Note: Original UMPLE logic prevented removal if log pointed here.
         // JPA approach allows removal from list; link breaking is separate.
    }

    public void addAvatar(Avatar avatar) {
        if (avatar != null && !this.avatars.contains(avatar)) {
            this.avatars.add(avatar);
            if (!this.equals(avatar.getMission())) { // Avoid loop
                 avatar.setMission(this);
            }
        }
    }

    public void removeAvatar(Avatar avatar) {
        if (avatar != null && this.avatars.contains(avatar)) {
            this.avatars.remove(avatar);
             if (this.equals(avatar.getMission())) { // Break other side
                avatar.setMission(null);
             }
        }
         // Note: Original UMPLE logic prevented removal if avatar pointed here.
    }


    // UMPLE factory/index methods usually removed/refactored for JPA
    // public MissionLog addMissionLog(...) { ... }
    // public boolean addMissionLogAt(...) { ... }
    // public Avatar addAvatar(...) { ... }
    // public boolean addAvatarAt(...) { ... }


    // --- Delete Method ---
    // Handled by repository.delete(mission)
    // Cascade settings manage associated entities.
    // public void delete() { ... }


    // --- equals() and hashCode() ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mission)) return false;
        Mission mission = (Mission) o;
        if (missionID == 0 && mission.missionID == 0) return this == o;
        return missionID == mission.missionID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(missionID);
    }

    // --- toString() ---
    @Override
    public String toString() {
        return "Mission{" +
                "missionID=" + missionID +
                ", missionName='" + missionName + '\'' +
                ", status=" + status +
                ", objective='" + objective + '\'' +
                // Avoid printing collections
                '}';
    }
}