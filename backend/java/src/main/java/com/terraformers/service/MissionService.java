package com.terraformers.service;

import java.util.ArrayList; // Needed for association management example
import java.util.List;
import java.util.Optional; // Needed for association management example

import org.springframework.beans.factory.annotation.Autowired; // Needed for association management example
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Needed for association management example

import com.terraformers.model.Avatar;
import com.terraformers.model.Mission;
import com.terraformers.model.MissionLog;
import com.terraformers.repository.AvatarRepository;
import com.terraformers.repository.MissionLogRepository;
import com.terraformers.repository.MissionRepository;

// Import custom exception (create this class if desired)
// import com.terraformers.exception.ResourceNotFoundException;

@Service
public class MissionService {

    @Autowired
    private MissionRepository missionRepository;

    // Inject other repositories if needed for association management or validation
    @Autowired(required = false) // Make optional if Avatar feature not always present
    private AvatarRepository avatarRepository;

    @Autowired(required = false) // Make optional if MissionLog feature not always present
    private MissionLogRepository missionLogRepository;


    @Transactional(readOnly = true)
    public List<Mission> getAllMissions() {
        return missionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Mission> getMissionById(int id) {
        return missionRepository.findById(id);
    }

    @Transactional
    public Mission createMission(Mission mission) {
        if (mission == null) {
            throw new IllegalArgumentException("Mission object cannot be null");
        }
        // Ensure ID is 0/null if DB generates it
        // mission.setMissionID(0);

        // Set default status if not provided
        if (mission.getStatus() == null) {
            mission.setStatus(Mission.MissionStatus.Planned);
        }
        // Ensure lists are initialized (though entity does this already)
        // if (mission.getAvatars() == null) mission.setAvatars(new ArrayList<>());
        // if (mission.getMissionLogs() == null) mission.setMissionLogs(new ArrayList<>());

        return missionRepository.save(mission);
    }

    @Transactional
    public Mission updateMission(int id, Mission missionDetails) {
         Mission existingMission = missionRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Mission not found with id: " + id));
                 // .orElseThrow(() -> new ResourceNotFoundException("Mission", "id", id));

         if (missionDetails == null) {
             throw new IllegalArgumentException("Mission details cannot be null for update");
         }

         // Update attributes
         existingMission.setMissionName(missionDetails.getMissionName());
         existingMission.setStatus(missionDetails.getStatus()); // Allow status update via PUT
         existingMission.setObjective(missionDetails.getObjective());

         // NOTE: Updating collections (avatars, missionLogs) via PUT is complex.
         // Avoid replacing collections directly. Use specific add/remove methods.

         return missionRepository.save(existingMission);
    }

    @Transactional
    public void deleteMission(int id) {
        Mission mission = missionRepository.findById(id)
             .orElseThrow(() -> new RuntimeException("Mission not found with id: " + id)); // Or use EmptyResultDataAccessException

        // Business Logic Example: Cannot delete 'Ongoing' mission
        if (mission.getStatus() == Mission.MissionStatus.Ongoing) {
            throw new IllegalStateException("Cannot delete an ongoing mission (ID: " + id + ")");
        }

        // Handle Avatars associated with this Mission before deleting mission.
        // Option 1: Null out the mission link on avatars (if Avatar.mission is nullable)
        // Option 2: Reassign avatars to a default 'unassigned' mission (if exists)
        // Option 3: Prevent deletion if avatars are assigned (like the check above)
        // Option 4: Rely on DB constraint (ON DELETE SET NULL/RESTRICT) - less flexible
        // Let's null out the link if avatarRepository exists:
        if (avatarRepository != null) {
             // Create copy as removing modifies the list being iterated over by helper method
             List<Avatar> avatarsToRemove = new ArrayList<>(mission.getAvatars());
             for (Avatar avatar : avatarsToRemove) {
                 // Use the helper method which should handle both sides
                 mission.removeAvatar(avatar);
                 // Or directly: avatar.setMission(null); avatarRepository.save(avatar);
             }
        }


        // Delete the mission itself. CascadeType.ALL on missionLogs ensures logs are deleted.
        missionRepository.delete(mission); // Use delete(entity) to ensure cascades work correctly if needed by specific JPA provider nuances
        // Or: missionRepository.deleteById(id);
    }

    // --- Example methods for managing associations ---

    @Transactional
    public Mission addAvatarToMission(int missionId, int avatarId) {
        if (avatarRepository == null) throw new UnsupportedOperationException("Avatar management not configured");

        Mission mission = missionRepository.findById(missionId)
                 .orElseThrow(() -> new RuntimeException("Mission not found with id: " + missionId));
        Avatar avatar = avatarRepository.findById(avatarId)
                 .orElseThrow(() -> new RuntimeException("Avatar not found with id: " + avatarId));

        // Business logic? Check if avatar is already on another mission?
        // if (avatar.getMission() != null && avatar.getMission().getMissionID() != missionId) {
        //     throw new IllegalStateException("Avatar " + avatarId + " is already assigned to mission " + avatar.getMission().getMissionID());
        // }

        mission.addAvatar(avatar); // Uses helper method in Mission
        // Explicit save often not needed due to transaction commit, but can be added for clarity
        // return missionRepository.save(mission);
        return mission; // Return the potentially modified mission
    }

    @Transactional
    public Mission removeAvatarFromMission(int missionId, int avatarId) {
         if (avatarRepository == null) throw new UnsupportedOperationException("Avatar management not configured");

         Mission mission = missionRepository.findById(missionId)
                 .orElseThrow(() -> new RuntimeException("Mission not found with id: " + missionId));
         Avatar avatar = avatarRepository.findById(avatarId)
                 .orElseThrow(() -> new RuntimeException("Avatar not found with id: " + avatarId));

         mission.removeAvatar(avatar); // Uses helper method in Mission
         // return missionRepository.save(mission);
         return mission;
    }

    @Transactional
    public Mission addLogToMission(int missionId, MissionLog log) {
        // Assumes log object is fully formed but possibly without mission set yet
         if (missionLogRepository == null) throw new UnsupportedOperationException("MissionLog management not configured");

         Mission mission = missionRepository.findById(missionId)
                 .orElseThrow(() -> new RuntimeException("Mission not found with id: " + missionId));

         if (log == null) throw new IllegalArgumentException("Log cannot be null");

         // Ensure ID is 0/null if using DB generation for logs
         // log.setLogID(0);

         mission.addMissionLog(log); // Uses helper method, which also calls log.setMission(this)

         // Save the log explicitly if it's a new transient instance
         // missionLogRepository.save(log); // Cascade might handle this if configured on Mission->Logs

         return mission; // Or return the saved log?
    }

    // --- Example method for changing status ---
    @Transactional
    public Mission updateMissionStatus(int id, Mission.MissionStatus newStatus) {
        Mission mission = missionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mission not found with id: " + id));
        // Add validation for valid status transitions if needed
        // e.g., cannot go from Completed back to Planned?
        mission.setStatus(newStatus);
        return missionRepository.save(mission); // Save the change
    }
}