package com.terraformers.controller;

import com.terraformers.dto.*; // Import all DTOs needed
import com.terraformers.model.*; // Import all Models needed
import com.terraformers.service.*; // Import all Services needed
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/avatars")
public class AvatarRestController {

    // Inject necessary services
    @Autowired private AvatarService avatarService;
    @Autowired private AvatarLogService avatarLogService; // Assuming this service exists
    @Autowired private SensorService sensorService;       // Assuming this service exists

    // --- DTO Conversion Helpers ---
    // (Assume convertToDto/Entity exist for Avatar, AvatarLog, Sensor)

     private AvatarDTO convertToDto(Avatar avatar) {
         if (avatar == null) return null;
         AvatarDTO dto = new AvatarDTO();
         dto.setAvatarID(avatar.getAvatarID());
         dto.setAvatarName(avatar.getAvatarName());
         dto.setAvatarColor(avatar.getAvatarColor());
         if (avatar.getLocation() != null) dto.setLocationId(avatar.getLocation().getLocationID());
         if (avatar.getAvatarBrain() != null) dto.setAvatarBrainId(avatar.getAvatarBrain().getAvatarBrainID());
         if (avatar.getMission() != null) dto.setMissionId(avatar.getMission().getMissionID());
         return dto;
    }
     private Avatar convertToEntity(AvatarDTO dto) {
         if (dto == null) return null;
         Avatar avatar = new Avatar();
         avatar.setAvatarID(dto.getAvatarID());
         avatar.setAvatarName(dto.getAvatarName());
         avatar.setAvatarColor(dto.getAvatarColor());
         return avatar;
     }
      private AvatarLogDTO convertToDto(AvatarLog log) {
         if (log == null) return null;
         AvatarLogDTO dto = new AvatarLogDTO();
         dto.setLogID(log.getLogID());
         dto.setLogDate(log.getLogDate());
         dto.setLogTime(log.getLogTime());
         dto.setLogType(log.getLogType());
         dto.setLogMessage(log.getLogMessage());
         if (log.getAvatar() != null) {
             dto.setAvatarId(log.getAvatar().getAvatarID());
         }
         return dto;
     }
      private AvatarLog convertToEntity(AvatarLogDTO dto) {
         if (dto == null) return null;
         AvatarLog log = new AvatarLog();
         log.setLogID(dto.getLogID());
         log.setLogDate(dto.getLogDate());
         log.setLogTime(dto.getLogTime());
         log.setLogType(dto.getLogType());
         log.setLogMessage(dto.getLogMessage());
         // Service links to Avatar based on avatarId
         return log;
     }
      private SensorDTO convertToDto(Sensor sensor) {
         if (sensor == null) return null;
         SensorDTO dto = new SensorDTO();
         dto.setSensorID(sensor.getSensorID());
         dto.setMountPosition(sensor.getMountPosition());
         dto.setStatus(sensor.getStatus());
         dto.setSensorType(sensor.getSensorType());
         if (sensor.getAvatar() != null) {
             dto.setAvatarId(sensor.getAvatar().getAvatarID());
         }
         return dto;
     }
      private Sensor convertToEntity(SensorDTO dto) {
         if (dto == null) return null;
         Sensor sensor = new Sensor();
         sensor.setSensorID(dto.getSensorID());
         sensor.setMountPosition(dto.getMountPosition());
         sensor.setStatus(dto.getStatus());
         sensor.setSensorType(dto.getSensorType());
         // Service links based on avatarId
         return sensor;
     }


    // --- Standard CRUD Endpoints for Avatar ---

    @GetMapping
    public List<AvatarDTO> getAllAvatars() { /* ... implementation ... */
        return avatarService.getAllAvatars().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvatarDTO> getAvatarById(@PathVariable int id) { /* ... implementation ... */
         return avatarService.getAvatarById(id)
                .map(avatar -> ResponseEntity.ok(convertToDto(avatar)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvatarDTO createAvatar(@RequestBody AvatarDTO avatarDTO) { /* ... implementation ... */
        try {
            Avatar avatarDetails = convertToEntity(avatarDTO);
            Avatar savedAvatar = avatarService.createAvatar(
                    avatarDetails, avatarDTO.getLocationId(),
                    avatarDTO.getAvatarBrainId(), avatarDTO.getMissionId()
            );
            return convertToDto(savedAvatar);
        } catch (IllegalArgumentException | IllegalStateException e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RuntimeException e) {
             if (e.getMessage().contains("not found")) {
                  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating avatar", e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvatarDTO> updateAvatar(@PathVariable int id, @RequestBody AvatarDTO avatarDTO) { /* ... implementation ... */
         try {
             Avatar avatarDetails = convertToEntity(avatarDTO);
             Avatar updatedAvatar = avatarService.updateAvatar(
                     id, avatarDetails, avatarDTO.getLocationId(),
                     avatarDTO.getAvatarBrainId(), avatarDTO.getMissionId()
             );
             return ResponseEntity.ok(convertToDto(updatedAvatar));
        } catch (IllegalArgumentException | IllegalStateException e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RuntimeException e) {
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating avatar", e);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvatar(@PathVariable int id) { /* ... implementation ... */
         try {
            avatarService.deleteAvatar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
             throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage(), e);
        } catch (RuntimeException e) {
             if (e.getMessage().contains("not found")) {
                  throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting avatar", e);
        }
    }

    // --- Endpoints for nested Logs/Sensors ---

    /**
     * GET /api/avatars/{avatarId}/logs : Get all logs for a specific avatar.
     */
    @GetMapping("/{avatarId}/logs")
    public ResponseEntity<List<AvatarLogDTO>> getAvatarLogs(@PathVariable int avatarId) {
         // Verify avatar exists first (optional, service method can do this)
         if (!avatarService.getAvatarById(avatarId).isPresent()) {
              return ResponseEntity.notFound().build();
         }
         try {
            // Assumes AvatarLogService has findLogsByAvatarId(avatarId)
            List<AvatarLog> logs = avatarLogService.findLogsByAvatarId(avatarId);
            List<AvatarLogDTO> logDTOs = logs.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(logDTOs);
         } catch (Exception e) {
              throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching logs for avatar", e);
         }
    }

    /**
     * POST /api/avatars/{avatarId}/logs : Create a new log associated with a specific avatar.
     */
    @PostMapping("/{avatarId}/logs")
    @ResponseStatus(HttpStatus.CREATED)
    public AvatarLogDTO addLogToAvatar(@PathVariable int avatarId, @RequestBody AvatarLogDTO logDto) {
        try {
            AvatarLog logToAdd = convertToEntity(logDto);
            // Assumes AvatarLogService has createLogForAvatar(log, avatarId)
            AvatarLog savedLog = avatarLogService.createLogForAvatar(logToAdd, avatarId);
            return convertToDto(savedLog);
       } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
       } catch (RuntimeException e) { // Catch if avatarId not found
            if (e.getMessage().contains("not found")) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Avatar not found with id: " + avatarId, e);
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding log to avatar", e);
       }
    }

    /**
     * GET /api/avatars/{avatarId}/sensors : Get all sensors for a specific avatar.
     */
    @GetMapping("/{avatarId}/sensors")
    public ResponseEntity<List<SensorDTO>> getAvatarSensors(@PathVariable int avatarId) {
         if (!avatarService.getAvatarById(avatarId).isPresent()) {
              return ResponseEntity.notFound().build();
         }
          try {
            // Assumes SensorService has findSensorsByAvatarId(avatarId)
            List<Sensor> sensors = sensorService.findSensorsByAvatarId(avatarId);
            List<SensorDTO> sensorDTOs = sensors.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(sensorDTOs);
         } catch (Exception e) {
              throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching sensors for avatar", e);
         }
    }

    /**
     * POST /api/avatars/{avatarId}/sensors : Create and associate a new sensor with a specific avatar.
     */
    @PostMapping("/{avatarId}/sensors")
    @ResponseStatus(HttpStatus.CREATED)
    public SensorDTO addSensorToAvatar(@PathVariable int avatarId, @RequestBody SensorDTO sensorDto) {
         try {
             Sensor sensorToAdd = convertToEntity(sensorDto);
             // Assumes SensorService has createSensorForAvatar(sensor, avatarId)
             Sensor savedSensor = sensorService.createSensorForAvatar(sensorToAdd, avatarId);
             return convertToDto(savedSensor);
        } catch (IllegalArgumentException e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RuntimeException e) { // Catch if avatarId not found
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Avatar not found with id: " + avatarId, e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding sensor to avatar", e);
        }
    }

    /**
     * DELETE /api/avatars/{avatarId}/sensors/{sensorId} : Disassociate and delete a sensor from an avatar.
     */
    @DeleteMapping("/{avatarId}/sensors/{sensorId}")
    public ResponseEntity<Void> removeSensorFromAvatar(@PathVariable int avatarId, @PathVariable int sensorId) {
         try {
             // Assumes SensorService has deleteSensorFromAvatar(avatarId, sensorId)
             sensorService.deleteSensorFromAvatar(avatarId, sensorId);
             return ResponseEntity.noContent().build();
         } catch (IllegalStateException e ) { // Catch if sensor not associated
              throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
         } catch (RuntimeException e) { // Catch if avatar or sensor not found
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error removing sensor from avatar", e);
         }
    }
}