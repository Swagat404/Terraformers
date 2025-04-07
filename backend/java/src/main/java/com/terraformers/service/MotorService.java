package com.terraformers.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Needed for delete logic

import org.springframework.beans.factory.annotation.Autowired; // Needed to link motor
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terraformers.model.AvatarBrain;
import com.terraformers.model.Motor;
import com.terraformers.repository.AvatarBrainRepository;
import com.terraformers.repository.MotorRepository;


@Service
public class MotorService {

    @Autowired
    private MotorRepository motorRepository;

    @Autowired // Needed for create/update links
    private AvatarBrainRepository avatarBrainRepository;

    @Transactional(readOnly = true)
    public List<Motor> getAllMotors() {
        return motorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Motor> getMotorById(int id) {
        return motorRepository.findById(id);
    }

    /**
     * Creates a new Motor associated with a specific AvatarBrain.
     */
    @Transactional
    public Motor createMotorForBrain(Motor motorDetails, int brainId) {
        if (motorDetails == null) throw new IllegalArgumentException("Motor details cannot be null");

        AvatarBrain brain = avatarBrainRepository.findById(brainId)
                .orElseThrow(() -> new RuntimeException("AvatarBrain not found with id: " + brainId + " while creating motor"));

        Motor newMotor = new Motor();
        // newMotor.setMotorID(0); // If ID is generated

        // Copy attributes
        newMotor.setMotorID(motorDetails.getMotorID()); // If using manual IDs
        newMotor.setMaxSpeed(motorDetails.getMaxSpeed());
        newMotor.setPowerConsumption(motorDetails.getPowerConsumption());
        newMotor.setPosition(motorDetails.getPosition());
        newMotor.setStatus(motorDetails.getStatus());
        newMotor.setMotorType(motorDetails.getMotorType());
        // newMotor.setMotorReadings(new ArrayList<>()); // List initialized in entity

        // Set the mandatory association using JPA-aware setter
        newMotor.setAvatarBrain(brain); // This should also add motor to brain.getMotors()

        return motorRepository.save(newMotor);
    }

    /**
     * Updates an existing Motor. Can optionally change the associated AvatarBrain.
     */
    @Transactional
    public Motor updateMotor(int id, Motor motorDetails, Integer newBrainId) {
        Motor existingMotor = motorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Motor not found with id: " + id));

        if (motorDetails == null) throw new IllegalArgumentException("Motor details cannot be null");

        // Update attributes
        existingMotor.setMaxSpeed(motorDetails.getMaxSpeed());
        existingMotor.setPowerConsumption(motorDetails.getPowerConsumption());
        existingMotor.setPosition(motorDetails.getPosition());
        existingMotor.setStatus(motorDetails.getStatus());
        existingMotor.setMotorType(motorDetails.getMotorType());

        // Optionally update Brain association
        if (newBrainId != null && (existingMotor.getAvatarBrain() == null || existingMotor.getAvatarBrain().getAvatarBrainID() != newBrainId)) {
             AvatarBrain newBrain = avatarBrainRepository.findById(newBrainId)
                     .orElseThrow(() -> new RuntimeException("New AvatarBrain not found with id: " + newBrainId + " while updating motor"));
             existingMotor.setAvatarBrain(newBrain); // Use JPA-aware setter
        }

        // Updating readings list handled separately

        return motorRepository.save(existingMotor);
    }

    /**
     * Deletes a Motor after verifying its association (optional).
     * Cascade settings on Motor entity handle deleting MotorReadings.
     */
    @Transactional
    public void deleteMotor(int id) {
        // Optional: Add business logic check (e.g., cannot delete 'Active' motor?)
         Motor motor = motorRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Motor not found with id: " + id));
        // if(motor.getStatus() == Motor.MotorStatus.Active) { ... throw exception ... }

        // The @ManyToOne link from Motor to AvatarBrain means deleting the motor
        // just removes the motor. The AvatarBrain's list should update automatically
        // if managed correctly by JPA/helpers, or on next fetch.
        // Explicitly calling brain.removeMotor might be needed if not using JPA helpers consistently.
        if (motor.getAvatarBrain() != null) {
             motor.getAvatarBrain().removeMotor(motor); // Ensure link broken in parent object state
        }

        motorRepository.deleteById(id);
    }

     /**
      * Deletes a specific motor only if it belongs to the specified brain.
      */
     @Transactional
     public void deleteMotorFromBrain(int brainId, int motorId) {
          Motor motor = motorRepository.findById(motorId)
                 .orElseThrow(() -> new RuntimeException("Motor not found with id: " + motorId));

         // Verify association
         if (motor.getAvatarBrain() == null || motor.getAvatarBrain().getAvatarBrainID() != brainId) {
             throw new IllegalStateException("Motor " + motorId + " is not associated with AvatarBrain " + brainId);
         }

          // Proceed with deletion (Cascade handles readings)
         // Link breaking happens implicitly here, or call removeMotor helper first
         if (motor.getAvatarBrain() != null) { // Should be true based on check above
             motor.getAvatarBrain().removeMotor(motor); // Ensure parent state updated
         }
         motorRepository.delete(motor); // Or deleteById(motorId)
     }

    /**
     * Finds all motors associated with a specific AvatarBrain ID.
     */
    @Transactional(readOnly = true)
    public List<Motor> findMotorsByBrainId(int brainId) {
        // Check if brain exists first
        if (!avatarBrainRepository.existsById(brainId)) {
             throw new RuntimeException("AvatarBrain not found with id: " + brainId);
        }
        // This is where a custom query in MotorRepository is most efficient:
        // return motorRepository.findByAvatarBrain_AvatarBrainID(brainId);

        // Manual filtering alternative (less efficient):
        return motorRepository.findAll().stream()
                .filter(motor -> motor.getAvatarBrain() != null && motor.getAvatarBrain().getAvatarBrainID() == brainId)
                .collect(Collectors.toList());
    }

     // Add methods for adding/removing/getting MotorReadings for a Motor if needed
     /*
     @Transactional
     public Motor addReadingToMotor(int motorId, MotorReading reading) { ... }
     */

}