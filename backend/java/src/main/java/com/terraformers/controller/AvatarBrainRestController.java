package com.terraformers.controller;

import com.terraformers.dto.AvatarBrainDTO;
import com.terraformers.dto.MotorDTO; // Import MotorDTO
import com.terraformers.model.AvatarBrain;
import com.terraformers.model.Motor; // Import Motor model
import com.terraformers.service.AvatarBrainService;
import com.terraformers.service.MotorService; // Inject MotorService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/avatar-brains") // Kebab-case for URLs is common
public class AvatarBrainRestController {

    @Autowired
    private AvatarBrainService avatarBrainService;

    @Autowired // Inject MotorService for nested resources
    private MotorService motorService;

    // --- DTO Conversion Helpers ---
    // (Assume these exist and work as previously defined)
    private AvatarBrainDTO convertToDto(AvatarBrain brain) {
        if (brain == null) return null;
        AvatarBrainDTO dto = new AvatarBrainDTO();
        dto.setAvatarBrainID(brain.getAvatarBrainID());
        dto.setAvatarBrainType(brain.getAvatarBrainType());
        dto.setAvatarSpeed(brain.getAvatarSpeed());
        dto.setAvatarMaxJumpHeight(brain.getAvatarMaxJumpHeight());
        dto.setAvatarLength(brain.getAvatarLength());
        dto.setAvatarWidth(brain.getAvatarWidth());
        dto.setAvatarHeight(brain.getAvatarHeight());
        if (brain.getAvatar() != null) {
            dto.setAvatarId(brain.getAvatar().getAvatarID());
        }
        return dto;
    }
    private AvatarBrain convertToEntity(AvatarBrainDTO dto) {
         if (dto == null) return null;
         AvatarBrain brain = new AvatarBrain();
         brain.setAvatarBrainID(dto.getAvatarBrainID());
         brain.setAvatarBrainType(dto.getAvatarBrainType());
         brain.setAvatarSpeed(dto.getAvatarSpeed());
         brain.setAvatarMaxJumpHeight(dto.getAvatarMaxJumpHeight());
         brain.setAvatarLength(dto.getAvatarLength());
         brain.setAvatarWidth(dto.getAvatarWidth());
         brain.setAvatarHeight(dto.getAvatarHeight());
         return brain;
     }
     // Need Motor DTO converters as well
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


    // --- Standard CRUD Endpoints for AvatarBrain ---
    @GetMapping
    public List<AvatarBrainDTO> getAllAvatarBrains() {
        return avatarBrainService.getAllAvatarBrains().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvatarBrainDTO> getAvatarBrainById(@PathVariable int id) {
         return avatarBrainService.getAvatarBrainById(id)
                .map(brain -> ResponseEntity.ok(convertToDto(brain)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvatarBrainDTO createAvatarBrain(@RequestBody AvatarBrainDTO brainDTO) {
         try {
            AvatarBrain brainToCreate = convertToEntity(brainDTO);
            AvatarBrain savedBrain = avatarBrainService.createAvatarBrain(brainToCreate);
            return convertToDto(savedBrain);
        } catch (IllegalArgumentException e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating avatar brain", e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvatarBrainDTO> updateAvatarBrain(@PathVariable int id, @RequestBody AvatarBrainDTO brainDTO) {
         try {
             AvatarBrain brainDetails = convertToEntity(brainDTO);
             AvatarBrain updatedBrain = avatarBrainService.updateAvatarBrain(id, brainDetails);
             return ResponseEntity.ok(convertToDto(updatedBrain));
        } catch (RuntimeException e) {
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating avatar brain", e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvatarBrain(@PathVariable int id) {
        try {
            avatarBrainService.deleteAvatarBrain(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
             throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (Exception e) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AvatarBrain not found with id: " + id, e);
        }
    }

    // --- Endpoints for Motors associated with an AvatarBrain ---

    /**
     * GET /api/avatar-brains/{brainId}/motors : Get all motors associated with a specific brain.
     */
    @GetMapping("/{brainId}/motors")
    public ResponseEntity<List<MotorDTO>> getBrainMotors(@PathVariable int brainId) {
        try {
            // We need a method in MotorService or AvatarBrainService to find motors by brainId
            // Let's assume MotorService has findMotorsByBrainId(brainId)
            List<Motor> motors = motorService.findMotorsByBrainId(brainId);
            List<MotorDTO> motorDTOs = motors.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(motorDTOs);
        } catch (RuntimeException e) { // Catch if brainId itself not found
             if (e.getMessage().contains("not found")) { // Potentially check for brain not found
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching motors for brain", e);
        }
    }

    /**
     * POST /api/avatar-brains/{brainId}/motors : Create and associate a new motor with a specific brain.
     */
    @PostMapping("/{brainId}/motors")
    @ResponseStatus(HttpStatus.CREATED)
    public MotorDTO addMotorToBrain(@PathVariable int brainId, @RequestBody MotorDTO motorDto) {
        try {
             // Convert DTO to a transient Motor entity
             Motor motorToAdd = convertToEntity(motorDto);
             // Use the service method that handles creation AND association
             Motor savedMotor = motorService.createMotorForBrain(motorToAdd, brainId);
             return convertToDto(savedMotor);
        } catch (IllegalArgumentException e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RuntimeException e) { // Catch if brainId not found
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AvatarBrain not found with id: " + brainId, e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding motor to brain", e);
        }
    }

     /**
      * DELETE /api/avatar-brains/{brainId}/motors/{motorId} : Disassociate and delete a motor from a specific brain.
      */
     @DeleteMapping("/{brainId}/motors/{motorId}")
     public ResponseEntity<Void> removeMotorFromBrain(@PathVariable int brainId, @PathVariable int motorId) {
         try {
             // Use a service method that handles verification and removal/deletion
             motorService.deleteMotorFromBrain(brainId, motorId);
             return ResponseEntity.noContent().build();
         } catch (RuntimeException e) { // Catch if brain or motor not found, or motor not associated
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
             }
             // Could add CONFLICT if motor belongs to different brain
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error removing motor from brain", e);
         }
     }
}