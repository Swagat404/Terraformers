package com.terraformers.controller;

import com.terraformers.dto.SensorDTO;
import com.terraformers.dto.SensorReadingDTO; // Import Reading DTO
import com.terraformers.model.Sensor;
import com.terraformers.model.SensorReading; // Import Reading Model
import com.terraformers.service.SensorService;
import com.terraformers.service.SensorReadingService; // Inject Reading Service
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sensors") // Base path for sensor resources
public class SensorRestController {

    @Autowired
    private SensorService sensorService;

    @Autowired // Inject service for nested readings
    private SensorReadingService sensorReadingService;

    // --- DTO Conversion Helpers ---
    // (Assume convertToDto(Sensor), convertToEntity(SensorDTO) exist)
    // (Assume convertToDto(SensorReading), convertToEntity(SensorReadingDTO) exist)
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
      private SensorReadingDTO convertToDto(SensorReading reading) {
        if (reading == null) return null;
        SensorReadingDTO dto = new SensorReadingDTO();
        dto.setReadingID(reading.getReadingID());
        dto.setTimeStamp(reading.getTimeStamp());
        dto.setValue(reading.getValue());
         if (reading.getSensor() != null) {
            dto.setSensorId(reading.getSensor().getSensorID());
        }
        return dto;
    }
     private SensorReading convertToEntity(SensorReadingDTO dto) {
         if (dto == null) return null;
         SensorReading reading = new SensorReading();
         reading.setReadingID(dto.getReadingID());
         reading.setTimeStamp(dto.getTimeStamp());
         reading.setValue(dto.getValue());
         // Service links based on sensorId
         return reading;
     }


    // --- Standard CRUD for Sensors ---
    // Like Motors, creating a Sensor often requires associating it with an Avatar immediately.
    // A standalone POST /api/sensors might require avatarId in the body.
    // Using POST /api/avatars/{avatarId}/sensors is often preferred.

    @GetMapping
    public List<SensorDTO> getAllSensors() {
        return sensorService.getAllSensors().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorDTO> getSensorById(@PathVariable int id) {
        return sensorService.getSensorById(id)
                .map(sensor -> ResponseEntity.ok(convertToDto(sensor)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Standalone PUT /api/sensors/{id} - If allowed (needs avatarId in DTO maybe?)
    @PutMapping("/{id}")
    public ResponseEntity<SensorDTO> updateSensorStandalone(@PathVariable int id, @RequestBody SensorDTO sensorDto) {
         try {
             Sensor sensorDetails = convertToEntity(sensorDto);
             Sensor updatedSensor = sensorService.updateSensor(id, sensorDetails, sensorDto.getAvatarId());
             return ResponseEntity.ok(convertToDto(updatedSensor));
        } catch (RuntimeException e) {
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
             }
              if (e.getMessage().contains("Avatar not found")) {
                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating sensor", e);
        }
    }


    // Standalone DELETE /api/sensors/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensorStandalone(@PathVariable int id) {
        try {
            sensorService.deleteSensor(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found with id: " + id, e);
        }
    }

    // --- Endpoints for Readings associated with a Sensor ---

    /**
     * GET /api/sensors/{sensorId}/readings : Get all readings for a specific sensor.
     */
    @GetMapping("/{sensorId}/readings")
    public ResponseEntity<List<SensorReadingDTO>> getSensorReadings(@PathVariable int sensorId) {
        try {
            // Assumes SensorReadingService has findReadingsBySensorId(sensorId)
            List<SensorReading> readings = sensorReadingService.findReadingsBySensorId(sensorId);
            List<SensorReadingDTO> readingDTOs = readings.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(readingDTOs);
        } catch (RuntimeException e) { // Catch if sensorId not found
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching readings for sensor", e);
        }
    }

    /**
     * POST /api/sensors/{sensorId}/readings : Create a new reading associated with a specific sensor.
     */
    @PostMapping("/{sensorId}/readings")
    @ResponseStatus(HttpStatus.CREATED)
    public SensorReadingDTO addReadingToSensor(@PathVariable int sensorId, @RequestBody SensorReadingDTO readingDto) {
         try {
             SensorReading readingToAdd = convertToEntity(readingDto);
             // Assumes SensorReadingService has createReadingForSensor(reading, sensorId)
             SensorReading savedReading = sensorReadingService.createReadingForSensor(readingToAdd, sensorId);
             return convertToDto(savedReading);
        } catch (IllegalArgumentException e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RuntimeException e) { // Catch if sensorId not found
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor not found with id: " + sensorId, e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding reading to sensor", e);
        }
    }

     // DELETE /api/sensors/{sensorId}/readings/{readingId} - Optional
     @DeleteMapping("/{sensorId}/readings/{readingId}")
     public ResponseEntity<Void> deleteSensorReading(@PathVariable int sensorId, @PathVariable int readingId) {
          try {
             // Assumes SensorReadingService has deleteReading(readingId, sensorId)
             sensorReadingService.deleteReading(readingId, sensorId);
             return ResponseEntity.noContent().build();
         } catch (RuntimeException e) { // Catch if sensor/reading not found or not associated
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting sensor reading", e);
         }
     }

     // --- Endpoints associated via AVATAR ---
     // These might live in AvatarRestController instead, but show concept here

     /**
      * GET /api/avatars/{avatarId}/sensors : Get sensors for a specific avatar
      */
     @GetMapping(path="/", params = "avatarId") // Example: Filter via /api/sensors?avatarId=...
     public List<SensorDTO> getSensorsByAvatar(@RequestParam int avatarId) {
         return sensorService.findSensorsByAvatarId(avatarId).stream()
                  .map(this::convertToDto)
                  .collect(Collectors.toList());
     }

}