package com.terraformers.controller;

import com.terraformers.dto.MotorDTO;
import com.terraformers.dto.MotorReadingDTO; // Import Reading DTO
import com.terraformers.model.Motor;
import com.terraformers.model.MotorReading; // Import Reading Model
import com.terraformers.service.MotorService;
import com.terraformers.service.MotorReadingService; // Inject Reading Service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/motors") // Base path for motor resources
public class MotorRestController {

    @Autowired
    private MotorService motorService;

    @Autowired // Inject service for nested readings
    private MotorReadingService motorReadingService;

    // --- DTO Conversion Helpers ---
     // Assume convertToDto(Motor) and convertToEntity(MotorDTO) exist
     private MotorDTO convertToDto(Motor motor) {
         if (motor == null) return null;
         MotorDTO dto = new MotorDTO();
         dto.setMotorID(motor.getMotorID());
         dto.setMaxSpeed(motor.getMaxSpeed());
         dto.setPowerConsumption(motor.getPowerConsumption());
         dto.setPosition(motor.getPosition());
         dto.setStatus(motor.getStatus());
         dto.setMotorType(motor.getMotorType());
         if(motor.getAvatarBrain() != null) {
             dto.setAvatarBrainId(motor.getAvatarBrain().getAvatarBrainID());
         }
         return dto;
     }
      private Motor convertToEntity(MotorDTO dto) {
         if (dto == null) return null;
         Motor motor = new Motor();
         motor.setMotorID(dto.getMotorID());
         motor.setMaxSpeed(dto.getMaxSpeed());
         motor.setPowerConsumption(dto.getPowerConsumption());
         motor.setPosition(dto.getPosition());
         motor.setStatus(dto.getStatus());
         motor.setMotorType(dto.getMotorType());
         // Service layer handles linking based on avatarBrainId
         return motor;
     }
     // Add converters for MotorReading <-> MotorReadingDTO
      private MotorReadingDTO convertToDto(MotorReading reading) {
        if (reading == null) return null;
        MotorReadingDTO dto = new MotorReadingDTO();
        dto.setReadingID(reading.getReadingID());
        dto.setTimeStamp(reading.getTimeStamp());
        dto.setCurrentSpeed(reading.getCurrentSpeed());
        dto.setDirection(reading.getDirection());
        dto.setCurrentPower(reading.getCurrentPower());
         if (reading.getMotor() != null) {
            dto.setMotorId(reading.getMotor().getMotorID());
        }
        return dto;
    }
     private MotorReading convertToEntity(MotorReadingDTO dto) {
         if (dto == null) return null;
         MotorReading reading = new MotorReading();
         reading.setReadingID(dto.getReadingID());
         reading.setTimeStamp(dto.getTimeStamp());
         reading.setCurrentSpeed(dto.getCurrentSpeed());
         reading.setDirection(dto.getDirection());
         reading.setCurrentPower(dto.getCurrentPower());
         // Service links to Motor based on motorId
         return reading;
     }

    // --- Standard CRUD for Motors ---
    // Note: Creating a motor often requires associating it with a brain immediately.
    // So, a standalone POST /api/motors might not make sense unless a brainId is in the body.
    // Using the POST /api/avatar-brains/{brainId}/motors is often preferred.
    // We'll keep GET and DELETE, but POST/PUT might be less common here.

    @GetMapping
    public List<MotorDTO> getAllMotors() {
        return motorService.getAllMotors().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotorDTO> getMotorById(@PathVariable int id) {
        return motorService.getMotorById(id)
                .map(motor -> ResponseEntity.ok(convertToDto(motor)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Standalone PUT /api/motors/{id} - If allowed (maybe needs brainId in DTO?)
    @PutMapping("/{id}")
    public ResponseEntity<MotorDTO> updateMotorStandalone(@PathVariable int id, @RequestBody MotorDTO motorDto) {
         try {
             Motor motorDetails = convertToEntity(motorDto);
             // Pass null for brainId change, assuming it's not updated here or included in DTO
             Motor updatedMotor = motorService.updateMotor(id, motorDetails, motorDto.getAvatarBrainId());
             return ResponseEntity.ok(convertToDto(updatedMotor));
        } catch (RuntimeException e) {
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
             }
              if (e.getMessage().contains("AvatarBrain not found")) { // If update tries to link non-existent brain
                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating motor", e);
        }
    }


    // Standalone DELETE /api/motors/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMotorStandalone(@PathVariable int id) {
        try {
            motorService.deleteMotor(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Log error
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Motor not found with id: " + id, e);
        }
    }

    // --- Endpoints for Readings associated with a Motor ---

    /**
     * GET /api/motors/{motorId}/readings : Get all readings for a specific motor.
     */
    @GetMapping("/{motorId}/readings")
    public ResponseEntity<List<MotorReadingDTO>> getMotorReadings(@PathVariable int motorId) {
        try {
            // Assumes MotorReadingService has findReadingsByMotorId(motorId)
            List<MotorReading> readings = motorReadingService.findReadingsByMotorId(motorId);
            List<MotorReadingDTO> readingDTOs = readings.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(readingDTOs);
        } catch (RuntimeException e) { // Catch if motorId not found
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching readings for motor", e);
        }
    }

    /**
     * POST /api/motors/{motorId}/readings : Create a new reading associated with a specific motor.
     */
    @PostMapping("/{motorId}/readings")
    @ResponseStatus(HttpStatus.CREATED)
    public MotorReadingDTO addReadingToMotor(@PathVariable int motorId, @RequestBody MotorReadingDTO readingDto) {
         try {
             MotorReading readingToAdd = convertToEntity(readingDto);
             // Assumes MotorReadingService has createReadingForMotor(reading, motorId)
             MotorReading savedReading = motorReadingService.createReadingForMotor(readingToAdd, motorId);
             return convertToDto(savedReading);
        } catch (IllegalArgumentException e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RuntimeException e) { // Catch if motorId not found
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Motor not found with id: " + motorId, e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding reading to motor", e);
        }
    }

     // DELETE /api/motors/{motorId}/readings/{readingId} - Optional: Delete specific reading
     @DeleteMapping("/{motorId}/readings/{readingId}")
     public ResponseEntity<Void> deleteMotorReading(@PathVariable int motorId, @PathVariable int readingId) {
          try {
             // Assumes MotorReadingService has deleteReading(readingId, motorId) to verify association before deleting
             motorReadingService.deleteReading(readingId, motorId);
             return ResponseEntity.noContent().build();
         } catch (RuntimeException e) { // Catch if motor/reading not found or not associated
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting motor reading", e);
         }
     }

}