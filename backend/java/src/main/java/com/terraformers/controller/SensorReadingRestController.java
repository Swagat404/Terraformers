package com.terraformers.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.terraformers.dto.SensorReadingDTO;
import com.terraformers.model.SensorReading;
import com.terraformers.service.SensorReadingService;

@RestController
@RequestMapping("/api/sensor-readings") // Separate endpoint for readings
public class SensorReadingRestController {

    @Autowired
    private SensorReadingService sensorReadingService;

    // --- DTO Conversion Helpers ---
     // Assume convertToDto(SensorReading), convertToEntity(SensorReadingDTO) exist
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


    // GET /api/sensor-readings - Get all (Use with caution - potentially large)
    @GetMapping
    public List<SensorReadingDTO> getAllSensorReadings(
            @RequestParam(required = false) Integer sensorId // Allow optional filtering by sensorId
    ) {
        List<SensorReading> readings;
        if (sensorId != null) {
             try {
                readings = sensorReadingService.findReadingsBySensorId(sensorId);
            } catch (RuntimeException e) {
                 if (e.getMessage().contains("not found")) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
                }
                 throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching readings", e);
            }
        } else {
            readings = sensorReadingService.getAllSensorReadings();
        }
        return readings.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // GET /api/sensor-readings/{id} - Get one by ID
    @GetMapping("/{id}")
    public ResponseEntity<SensorReadingDTO> getSensorReadingById(@PathVariable int id) {
        return sensorReadingService.getSensorReadingById(id)
                .map(reading -> ResponseEntity.ok(convertToDto(reading)))
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/sensor-readings - Create new (Requires sensorId in body)
    // Note: POST /api/sensors/{sensorId}/readings is often preferred
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SensorReadingDTO createSensorReading(@RequestBody SensorReadingDTO readingDto) {
         if (readingDto.getSensorId() == null) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing required sensorId for creating sensor reading");
        }
         try {
            SensorReading readingToCreate = convertToEntity(readingDto);
            SensorReading savedReading = sensorReadingService.createReadingForSensor(readingToCreate, readingDto.getSensorId());
            return convertToDto(savedReading);
        } catch (IllegalArgumentException e) {
             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RuntimeException e) { // Catch sensor not found
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e); // 400 as linked entity missing
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating sensor reading", e);
        }
    }

    // PUT /api/sensor-readings/{id} - Update existing
    @PutMapping("/{id}")
    public ResponseEntity<SensorReadingDTO> updateSensorReading(
            @PathVariable int id,
            @RequestBody SensorReadingDTO readingDto) {

        Integer newSensorId = readingDto.getSensorId(); // Allow changing sensor via PUT? Risky. Usually null this out.
         if (newSensorId != null) {
             // Logic to handle changing sensor might be complex/undesirable for a Reading update
             // Consider disallowing sensorId changes via PUT here or handle carefully in service
              System.err.println("Warning: Attempting to change sensor association via PUT /api/sensor-readings/{id}. This might be disallowed.");
              // For now, we pass null to the service update method to prevent changing sensor via this PUT
              newSensorId = null;
         }

         try {
             SensorReading readingDetails = convertToEntity(readingDto);
             SensorReading updatedReading = sensorReadingService.updateSensorReading(id, readingDetails, newSensorId);
             return ResponseEntity.ok(convertToDto(updatedReading));
        } catch (RuntimeException e) { // Catch "not found" etc.
             if (e.getMessage().contains("not found")) {
                 throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
             }
              if (e.getMessage().contains("Sensor not found")) { // If trying to link to non-existent sensor
                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
             }
             throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating sensor reading", e);
        }
    }


    // DELETE /api/sensor-readings/{id} - Delete specific reading
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSensorReading(@PathVariable int id) {
         try {
            sensorReadingService.deleteReading(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "SensorReading not found with id: " + id, e);
        }
    }
}