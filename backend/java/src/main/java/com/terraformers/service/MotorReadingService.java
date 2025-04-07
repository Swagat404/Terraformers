package com.terraformers.service;

import java.util.List; // Needed for linking
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired; // Needed to link reading
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terraformers.model.Motor;
import com.terraformers.model.MotorReading;
import com.terraformers.repository.MotorReadingRepository;
import com.terraformers.repository.MotorRepository;

@Service
public class MotorReadingService {

    @Autowired
    private MotorReadingRepository motorReadingRepository;

    @Autowired
    private MotorRepository motorRepository;

    @Transactional(readOnly = true)
    public List<MotorReading> getAllMotorReadings() {
        // Use with caution, potentially very large
        return motorReadingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MotorReading> getMotorReadingById(int id) {
        return motorReadingRepository.findById(id);
    }

    /**
     * Finds all readings associated with a specific Motor ID.
     */
    @Transactional(readOnly = true)
    public List<MotorReading> findReadingsByMotorId(int motorId) {
        // Check if motor exists first
        if (!motorRepository.existsById(motorId)) {
             throw new RuntimeException("Motor not found with id: " + motorId);
        }
        // Efficient way using derived query (add to MotorReadingRepository):
        // return motorReadingRepository.findByMotor_MotorIDOrderByTimeStampDesc(motorId);

        // Manual filtering alternative:
        return motorReadingRepository.findAll().stream()
                .filter(reading -> reading.getMotor() != null && reading.getMotor().getMotorID() == motorId)
                // .sorted(Comparator.comparing(MotorReading::getTimeStamp, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    /**
     * Creates a new MotorReading associated with a specific Motor.
     */
    @Transactional
    public MotorReading createReadingForMotor(MotorReading readingDetails, int motorId) {
         if (readingDetails == null) throw new IllegalArgumentException("Reading details cannot be null");

         Motor motor = motorRepository.findById(motorId)
                 .orElseThrow(() -> new RuntimeException("Motor not found with id: " + motorId + " while creating motor reading"));

         MotorReading newReading = new MotorReading();
         // newReading.setReadingID(0); // If ID generated

         // Copy attributes (inherited and specific)
         newReading.setReadingID(readingDetails.getReadingID()); // If manual IDs
         newReading.setTimeStamp(readingDetails.getTimeStamp());
         newReading.setCurrentSpeed(readingDetails.getCurrentSpeed());
         newReading.setDirection(readingDetails.getDirection());
         newReading.setCurrentPower(readingDetails.getCurrentPower());


         // Set association using JPA-aware setter
         newReading.setMotor(motor); // Also adds reading to motor.getMotorReadings()

         return motorReadingRepository.save(newReading);
    }

    /**
     * Deletes a MotorReading by its ID.
     * Ensures link from Motor is broken first.
     */
    @Transactional
    public void deleteReading(int id) {
        MotorReading reading = motorReadingRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("MotorReading not found with id: " + id));

        // Break link from parent Motor
        if (reading.getMotor() != null) {
             reading.getMotor().removeMotorReading(reading);
        }

        motorReadingRepository.delete(reading); // Or deleteById(id)
    }

     /**
      * Deletes a specific reading only if it belongs to the specified motor.
      */
     @Transactional
     public void deleteReading(int readingId, int motorId) {
         MotorReading reading = motorReadingRepository.findById(readingId)
                 .orElseThrow(() -> new RuntimeException("MotorReading not found with id: " + readingId));

         // Verify association
         if (reading.getMotor() == null || reading.getMotor().getMotorID() != motorId) {
             throw new IllegalStateException("MotorReading " + readingId + " is not associated with Motor " + motorId);
         }

         // Proceed with deletion
         deleteReading(readingId); // Call the main delete method
     }

    // Update method for readings might be less common, but could be added if needed.
}