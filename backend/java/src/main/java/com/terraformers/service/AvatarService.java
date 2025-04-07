package com.terraformers.service;

import java.util.List; // Import all models
import java.util.Optional; // Import all repositories

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terraformers.model.Avatar;
import com.terraformers.model.AvatarBrain;
import com.terraformers.model.Location;
import com.terraformers.model.Mission;
import com.terraformers.repository.AvatarBrainRepository;
import com.terraformers.repository.AvatarRepository;
import com.terraformers.repository.LocationRepository;
import com.terraformers.repository.MissionRepository;

// Import custom exception (create this class if desired)
// import com.terraformers.exception.ResourceNotFoundException;

@Service
public class AvatarService {

    @Autowired private AvatarRepository avatarRepository;
    @Autowired private LocationRepository locationRepository;
    @Autowired private AvatarBrainRepository avatarBrainRepository;
    @Autowired private MissionRepository missionRepository;
    // Inject log/sensor repos if creating logs/sensors within avatar service methods
    // @Autowired private AvatarLogRepository avatarLogRepository;
    // @Autowired private SensorRepository sensorRepository;


    @Transactional(readOnly = true)
    public List<Avatar> getAllAvatars() {
        return avatarRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Avatar> getAvatarById(int id) {
        return avatarRepository.findById(id);
    }

    /**
     * Creates a new Avatar, linking it to existing Location, Brain, and Mission.
     * Assumes IDs for related entities are provided (e.g., via a DTO).
     *
     * @param avatarDetails Basic Avatar attributes (name, color). ID ignored if generated.
     * @param locationId ID of the Location (Mandatory).
     * @param brainId ID of the AvatarBrain (Mandatory).
     * @param missionId ID of the Mission (Potentially optional?).
     * @return The saved Avatar entity.
     * @throws RuntimeException if any referenced entity (Location, Brain, Mission) is not found.
     * @throws IllegalArgumentException if required data is missing.
     */
    @Transactional
    public Avatar createAvatar(Avatar avatarDetails, Integer locationId, Integer brainId, Integer missionId) {
        if (avatarDetails == null) throw new IllegalArgumentException("Avatar details cannot be null");
        if (locationId == null) throw new IllegalArgumentException("Location ID is required for new Avatar");
        if (brainId == null) throw new IllegalArgumentException("AvatarBrain ID is required for new Avatar");
        // Mission might be optional depending on rules

        // Fetch associated entities or throw if not found
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + locationId));
        AvatarBrain brain = avatarBrainRepository.findById(brainId)
                .orElseThrow(() -> new RuntimeException("AvatarBrain not found with id: " + brainId));
        Mission mission = null;
        if (missionId != null) {
            mission = missionRepository.findById(missionId)
                .orElseThrow(() -> new RuntimeException("Mission not found with id: " + missionId));
        }

        // --- Business Rule Checks (Examples) ---
        // Check if location is already occupied
        if (location.getAvatar() != null) {
             throw new IllegalStateException("Location " + locationId + " is already occupied by Avatar " + location.getAvatar().getAvatarID());
        }
        // Check if brain is already assigned
        if (brain.getAvatar() != null) {
            throw new IllegalStateException("AvatarBrain " + brainId + " is already assigned to Avatar " + brain.getAvatar().getAvatarID());
        }
        // --- End Checks ---

        // Create new Avatar
        Avatar newAvatar = new Avatar();
        // newAvatar.setAvatarID(0); // If ID is generated

        // Set attributes
        newAvatar.setAvatarID(avatarDetails.getAvatarID()); // If using manual IDs
        newAvatar.setAvatarName(avatarDetails.getAvatarName());
        newAvatar.setAvatarColor(avatarDetails.getAvatarColor());
        // Initialize collections (done in entity already)
        // newAvatar.setAvatarLogs(new ArrayList<>());
        // newAvatar.setSensors(new ArrayList<>());


        // Set associations using JPA-aware setters
        newAvatar.setLocation(location);     // Handles both sides
        newAvatar.setAvatarBrain(brain); // Handles both sides
        if (mission != null) {
            newAvatar.setMission(mission);   // Handles both sides
        }

        return avatarRepository.save(newAvatar);
    }

    /**
     * Updates an existing Avatar entity.
     * Allows changing attributes and potentially reassigning Location, Brain, Mission.
     *
     * @param id The ID of the Avatar to update.
     * @param avatarDetails Avatar object containing new details.
     * @param newLocationId Optional new Location ID.
     * @param newBrainId Optional new AvatarBrain ID.
     * @param newMissionId Optional new Mission ID (use null or a special value like -1 to unset).
     * @return The updated Avatar entity.
     * @throws RuntimeException if Avatar or any referenced new entity ID is not found.
     * @throws IllegalArgumentException if required data is missing.
     * @throws IllegalStateException for business rule violations (e.g., trying to move to occupied location).
     */
    @Transactional
    public Avatar updateAvatar(int id, Avatar avatarDetails, Integer newLocationId, Integer newBrainId, Integer newMissionId) {
        Avatar existingAvatar = avatarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Avatar not found with id: " + id));

        if (avatarDetails == null) throw new IllegalArgumentException("Avatar details cannot be null");

        // Update basic attributes
        existingAvatar.setAvatarName(avatarDetails.getAvatarName());
        existingAvatar.setAvatarColor(avatarDetails.getAvatarColor());

        // Update Location association if requested and different
        if (newLocationId != null && (existingAvatar.getLocation() == null || existingAvatar.getLocation().getLocationID() != newLocationId)) {
            Location newLocation = locationRepository.findById(newLocationId)
                    .orElseThrow(() -> new RuntimeException("New Location not found with id: " + newLocationId));
            // Check if new location is occupied by a *different* avatar
             if (newLocation.getAvatar() != null && newLocation.getAvatar().getAvatarID() != existingAvatar.getAvatarID()) {
                 throw new IllegalStateException("New Location " + newLocationId + " is already occupied by Avatar " + newLocation.getAvatar().getAvatarID());
             }
            existingAvatar.setLocation(newLocation); // Use JPA-aware setter
        }

        // Update AvatarBrain association if requested and different
        if (newBrainId != null && (existingAvatar.getAvatarBrain() == null || existingAvatar.getAvatarBrain().getAvatarBrainID() != newBrainId)) {
            AvatarBrain newBrain = avatarBrainRepository.findById(newBrainId)
                    .orElseThrow(() -> new RuntimeException("New AvatarBrain not found with id: " + newBrainId));
             // Check if new brain is occupied by a *different* avatar
             if (newBrain.getAvatar() != null && newBrain.getAvatar().getAvatarID() != existingAvatar.getAvatarID()) {
                 throw new IllegalStateException("New AvatarBrain " + newBrainId + " is already assigned to Avatar " + newBrain.getAvatar().getAvatarID());
             }
            existingAvatar.setAvatarBrain(newBrain); // Use JPA-aware setter
        }

        // Update Mission association if requested and different
        // Handling null: Assume passing null means unassigning from current mission
        boolean missionChanged = false;
        if (newMissionId != null) { // If a specific mission ID is provided
             if (existingAvatar.getMission() == null || existingAvatar.getMission().getMissionID() != newMissionId) {
                  Mission newMission = missionRepository.findById(newMissionId)
                          .orElseThrow(() -> new RuntimeException("New Mission not found with id: " + newMissionId));
                  existingAvatar.setMission(newMission); // Use JPA-aware setter
                  missionChanged = true;
             }
        } else { // If no mission ID provided in update, maybe unset the current one?
             if (existingAvatar.getMission() != null) {
                  existingAvatar.setMission(null); // Unset mission
                  missionChanged = true;
             }
        }

        // NOTE: Updating avatarLogs or sensors list usually happens via separate service calls

        return avatarRepository.save(existingAvatar);
    }


    /**
     * Deletes an Avatar entity by its ID.
     * Cascade settings handle associated logs/sensors. Links from Location/Brain need care.
     *
     * @param id The ID of the Avatar to delete.
     * @throws RuntimeException if avatar is not found.
     * @throws IllegalStateException if deletion violates business rules (e.g., on active mission).
     */
    @Transactional
    public void deleteAvatar(int id) {
        Avatar avatar = avatarRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Avatar not found with id: " + id));

        // --- Business Logic Checks (Example) ---
        // if (avatar.getMission() != null && avatar.getMission().getStatus() == Mission.MissionStatus.Ongoing) {
        //     throw new IllegalStateException("Cannot delete Avatar " + id + " while on an ongoing mission.");
        // }
        // --- End Checks ---

        // Explicitly break OneToOne links from the *other* side before deleting Avatar,
        // especially if cascade isn't set to REMOVE from the other side.
        if (avatar.getLocation() != null) {
            avatar.getLocation().setAvatar(null);
            // locationRepository.save(avatar.getLocation()); // Often not needed within same transaction
        }
        if (avatar.getAvatarBrain() != null) {
            avatar.getAvatarBrain().setAvatar(null);
            // avatarBrainRepository.save(avatar.getAvatarBrain()); // Often not needed
        }
         // Break link from Mission side
         if (avatar.getMission() != null) {
             avatar.getMission().removeAvatar(avatar);
             // missionRepository.save(avatar.getMission()); // Often not needed
         }


        // Now delete the avatar. CascadeType.ALL on avatarLogs and sensors
        // should handle deletion of those children.
        avatarRepository.delete(avatar);
    }

     // Add methods for managing logs/sensors for an avatar if needed
     // e.g., addLogToAvatar(int avatarId, AvatarLog log)
     // e.g., addSensorToAvatar(int avatarId, Sensor sensor)
     // e.g., getAvatarLogs(int avatarId) -> returns List<AvatarLog>
     // e.g., getAvatarSensors(int avatarId) -> returns List<Sensor>

}