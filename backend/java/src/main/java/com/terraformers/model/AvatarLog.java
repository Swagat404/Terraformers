package com.terraformers.model;

// Import JPA and other necessary classes
import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

// line 127 "model.ump"
// line 239 "model.ump"

@Entity
@Table(name = "avatar_logs") // Table for this specific log type
// If using Inheritance strategy (e.g., JOINED or SINGLE_TABLE on Log), annotations here might differ slightly.
// With @MappedSuperclass on Log, this defines its own table including Log's fields.
public class AvatarLog extends Log { // Inherits fields and ID from Log

    //------------------------
    // MEMBER VARIABLES
    //------------------------

    // --- AvatarLog Associations ---
    // Many AvatarLogs belong to One Avatar. This side owns the Foreign Key.
    @ManyToOne(fetch = FetchType.LAZY) // Lazy load the associated Avatar
    @JoinColumn(name = "avatar_id", nullable = false) // Specifies the FK column in 'avatar_logs' table
    private Avatar avatar;

    //------------------------
    // CONSTRUCTOR
    //------------------------

    // JPA requires a no-arg constructor
    public AvatarLog() {
        super(); // Call super constructor if needed/available
    }

    // Original UMPLE constructor
    public AvatarLog(int aLogID, Date aLogDate, Time aLogTime, LogType aLogType, String aLogMessage, Avatar aAvatar) {
        super(aLogID, aLogDate, aLogTime, aLogType, aLogMessage); // Initialize inherited fields
        // Use JPA-aware setter to establish links
        this.setAvatar(aAvatar);
        // The check (!didAdd...) is usually not needed with JPA setters/service logic
    }

    //------------------------
    // INTERFACE
    //------------------------

    /* Code from template association_GetOne */
    public Avatar getAvatar() {
        return avatar;
    }

    /* Code from template association_SetOneToMany */
    // Renamed method for clarity (JPA standard) and corrected logic
    /**
     * Sets the Avatar for this log, maintaining bidirectional consistency.
     * @param newAvatar The Avatar this log belongs to. Cannot be null.
     * @return boolean true if successful.
     */
    public boolean setAvatar(Avatar newAvatar) {
        if (newAvatar == null) {
            // Decide handling: throw exception? return false? Based on requirements.
            // throw new IllegalArgumentException("Avatar cannot be null for AvatarLog");
            return false; // Following original UMPLE check pattern
        }

        // Avoid self-assignment loop / unnecessary work
        if (Objects.equals(this.avatar, newAvatar)) {
            return true;
        }

        // If currently associated with a different avatar, remove from its list
        if (this.avatar != null) {
            this.avatar.removeAvatarLog(this); // Use helper on Avatar
        }

        // Set the new avatar reference on this log
        this.avatar = newAvatar;

        // Add this log to the new avatar's list
        this.avatar.addAvatarLog(this); // Use helper on Avatar

        return true;
    }

    // --- Delete Method ---
    // UMPLE delete logic needs review for JPA.
    // Usually handled by repository.delete(avatarLog).
    // Need to ensure link from Avatar is broken before deletion if not using CascadeType.REMOVE on Avatar.
    @Override // Override if Log has a delete method
    public void delete() {
        // Break the link with the Avatar *before* deleting this log
        if (this.avatar != null) {
            // Store placeholder ONLY if needed for further logic AFTER nulling internal ref
            // Avatar placeholderAvatar = avatar;
            Avatar placeholderAvatar = this.avatar; // Correct reference
            this.avatar = null; // Null internal reference first
            placeholderAvatar.removeAvatarLog(this); // Tell Avatar to remove this log
        }
        // super.delete(); // Call super if it does anything meaningful
        // Actual DB deletion is handled by repository.delete()
    }

    // equals() and hashCode() are inherited from Log (based on logID and Class)

    // toString() can be inherited or overridden if needed
    @Override
    public String toString() {
        // Include Avatar ID for context, avoid printing full Avatar object
        return super.toString() + // Includes Log fields
               " - AvatarLog{" +
               "avatarId=" + (avatar != null ? avatar.getAvatarID() : "null") +
               '}';
    }
}