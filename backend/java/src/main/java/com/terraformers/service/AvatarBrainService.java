package com.terraformers.service;

import java.util.List; // Needed for association management
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired; // Needed for association management
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Needed for setting Avatar link

import com.terraformers.model.AvatarBrain; // Needed for managing Motors
import com.terraformers.model.Motor;
import com.terraformers.repository.AvatarBrainRepository;
import com.terraformers.repository.AvatarRepository;
import com.terraformers.repository.MotorRepository;

// Import custom exception (create this class if desired)
// import com.terraformers.exception.ResourceNotFoundException;

@Service
public class AvatarBrainService {

    @Autowired
    private AvatarBrainRepository avatarBrainRepository;

    // Optional injection based on required logic
    @Autowired(required = false)
    private MotorRepository motorRepository;

    @Autowired(required = false)
    private AvatarRepository avatarRepository;


    @Transactional(readOnly = true)
    public List<AvatarBrain> getAllAvatarBrains() {
        return avatarBrainRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<AvatarBrain> getAvatarBrainById(int id) {
        return avatarBrainRepository.findById(id);
    }

    @Transactional
    public AvatarBrain createAvatarBrain(AvatarBrain avatarBrain) {
        if (avatarBrain == null) {
            throw new IllegalArgumentException("AvatarBrain object cannot be null");
        }
        // Ensure ID is 0/null if DB generates it
        // avatarBrain.setAvatarBrainID(0);
        // Ensure lists are initialized (though entity does this)
        // if (avatarBrain.getMotors() == null) avatarBrain.setMotors(new ArrayList<>());
        // Avatar link is typically set from the Avatar side

        return avatarBrainRepository.save(avatarBrain);
    }

    @Transactional
    public AvatarBrain updateAvatarBrain(int id, AvatarBrain brainDetails) {
         AvatarBrain existingBrain = avatarBrainRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("AvatarBrain not found with id: " + id));
                 // .orElseThrow(() -> new ResourceNotFoundException("AvatarBrain", "id", id));

         if (brainDetails == null) {
             throw new IllegalArgumentException("AvatarBrain details cannot be null for update");
         }

         // Update attributes
         existingBrain.setAvatarBrainType(brainDetails.getAvatarBrainType());
         existingBrain.setAvatarSpeed(brainDetails.getAvatarSpeed());
         existingBrain.setAvatarMaxJumpHeight(brainDetails.getAvatarMaxJumpHeight());
         existingBrain.setAvatarLength(brainDetails.getAvatarLength());
         existingBrain.setAvatarWidth(brainDetails.getAvatarWidth());
         existingBrain.setAvatarHeight(brainDetails.getAvatarHeight());

         // NOTE: Updating 'motors' collection or 'avatar' link requires specific logic,
         // usually via dedicated service methods or careful processing of DTO inputs.
         // Avoid existingBrain.setMotors(...) or existingBrain.setAvatar(...) directly here
         // unless you fully manage the bidirectional state.

         return avatarBrainRepository.save(existingBrain);
    }

    @Transactional
    public void deleteAvatarBrain(int id) {
         AvatarBrain brain = avatarBrainRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("AvatarBrain not found with id: " + id));

        // Business logic: Cannot delete a brain if it's assigned to an Avatar?
        if (brain.getAvatar() != null) {
            throw new IllegalStateException("Cannot delete AvatarBrain " + id + " as it is assigned to Avatar " + brain.getAvatar().getAvatarID());
            // Alternative: Null out the link on the Avatar side first
            // if(avatarRepository != null) {
            //     brain.getAvatar().setAvatarBrain(null);
            //     avatarRepository.save(brain.getAvatar());
            // } else { // Throw if cannot resolve link programmatically }
        }

        // Deletion will cascade to Motors if CascadeType.ALL/REMOVE is set on AvatarBrain.motors
        avatarBrainRepository.delete(brain);
        // Or avatarBrainRepository.deleteById(id);
    }

    // --- Example methods for managing associations ---

    @Transactional
    public AvatarBrain addMotorToBrain(int brainId, Motor motor) {
         if (motorRepository == null) throw new UnsupportedOperationException("Motor management not configured");

         AvatarBrain brain = avatarBrainRepository.findById(brainId)
                 .orElseThrow(() -> new RuntimeException("AvatarBrain not found with id: " + brainId));
        if (motor == null) throw new IllegalArgumentException("Motor cannot be null");

        // Ensure motor ID is 0/null if DB generates it
        // motor.setMotorID(0);

        brain.addMotor(motor); // Use helper method - this also calls motor.setAvatarBrain(brain)

        // Depending on cascade settings, saving the brain might save the new motor.
        // If motor is transient, explicitly saving it first might be needed,
        // or ensure CascadeType.PERSIST/MERGE is on the motors collection.
        // motorRepository.save(motor); // Possibly needed if motor is new and cascade isn't sufficient
        // return avatarBrainRepository.save(brain); // Re-saving parent often unnecessary if child link set

        return brain; // Return potentially modified brain
    }

     @Transactional
    public AvatarBrain removeMotorFromBrain(int brainId, int motorId) {
         if (motorRepository == null) throw new UnsupportedOperationException("Motor management not configured");

         AvatarBrain brain = avatarBrainRepository.findById(brainId)
                 .orElseThrow(() -> new RuntimeException("AvatarBrain not found with id: " + brainId));
         Motor motor = motorRepository.findById(motorId)
                 .orElseThrow(() -> new RuntimeException("Motor not found with id: " + motorId));

         brain.removeMotor(motor); // Use helper method - this also calls motor.setAvatarBrain(null)

         // orphanRemoval=true on brain.motors handles deleting the motor if desired.
         // If orphanRemoval=false, the motor exists but is unlinked.

         // return avatarBrainRepository.save(brain); // Re-saving parent usually unnecessary
         return brain;
    }

    // Assigning the brain TO an avatar is usually done in AvatarService
    // by calling avatar.setAvatarBrain(brain)

}