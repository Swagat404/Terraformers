package com.terraformers.service;

import java.util.List; // Needed for linking
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired; // Needed to link reading
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terraformers.model.Sensor;
import com.terraformers.model.SensorReading;
import com.terraformers.repository.SensorReadingRepository;
import com.terraformers.repository.SensorRepository;

@Service
public class SensorReadingService {

    @Autowired
    private SensorReadingRepository sensorReadingRepository;

    @Autowired // Needed to associate reading with a sensor
    private SensorRepository sensorRepository;

    @Transactional(readOnly = true)
    public List<SensorReading> getAllSensorReadings() {
        // Warning: Could be very large! Add pagination later if needed.
        return sensorReadingRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<SensorReading> getSensorReadingById(int id) {
        return sensorReadingRepository.findById(id);
    }

    /**
     * Creates a new SensorReading associated with a specific Sensor.
     */
    @Transactional
    public SensorReading createReadingForSensor(SensorReading readingDetails, int sensorId) {
        if (readingDetails == null) throw new IllegalArgumentException("Reading details cannot be null");

        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor not found with id: " + sensorId + " while creating sensor reading"));

        SensorReading newReading = new SensorReading();
        // newReading.setReadingID(0); // If ID is generated

        // Copy attributes (inherited and specific)
        newReading.setReadingID(readingDetails.getReadingID()); // If manual IDs
        newReading.setTimeStamp(readingDetails.getTimeStamp());
        newReading.setValue(readingDetails.getValue());

        // Set the mandatory Sensor association using JPA-aware setter
        newReading.setSensor(sensor); // This should also add reading to sensor.getSensorReadings()

        return sensorReadingRepository.save(newReading);
    }


    /**
     * Updates an existing SensorReading. Typically only attribute values change.
     * Changing the associated Sensor might be unusual but possible if needed.
     */
    @Transactional
    public SensorReading updateSensorReading(int id, SensorReading readingDetails, Integer newSensorId) {
         SensorReading existingReading = sensorReadingRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("SensorReading not found with id: " + id));

         if (readingDetails == null) throw new IllegalArgumentException("Reading details cannot be null");

         // Update attributes
         existingReading.setTimeStamp(readingDetails.getTimeStamp());
         existingReading.setValue(readingDetails.getValue());

         // Optionally update Sensor association if needed
         if (newSensorId != null && (existingReading.getSensor() == null || existingReading.getSensor().getSensorID() != newSensorId)) {
             Sensor newSensor = sensorRepository.findById(newSensorId)
                     .orElseThrow(() -> new RuntimeException("New Sensor not found with id: " + newSensorId));
             existingReading.setSensor(newSensor); // Use JPA-aware setter
         }

         return sensorReadingRepository.save(existingReading);
    }

    /**
     * Deletes a SensorReading by its ID.
     */
    @Transactional
    public void deleteReading(int id) {
         SensorReading reading = sensorReadingRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("SensorReading not found with id: " + id));

         // Break link from Sensor side *before* deleting if necessary
         // (often handled by orphanRemoval=true or cascade, but explicit break is safe)
         if (reading.getSensor() != null) {
              reading.getSensor().removeSensorReading(reading);
             // No need to save sensor if just removing from collection in memory before delete
         }

        sensorReadingRepository.deleteById(id);
    }

     /**
      * Deletes a specific reading only if it belongs to the specified sensor.
      * Useful for DELETE /api/sensors/{sensorId}/readings/{readingId} endpoint.
      */
     @Transactional
     public void deleteReading(int readingId, int sensorId) {
          SensorReading reading = sensorReadingRepository.findById(readingId)
                 .orElseThrow(() -> new RuntimeException("SensorReading not found with id: " + readingId));

         // Verify association
         if (reading.getSensor() == null || reading.getSensor().getSensorID() != sensorId) {
             throw new IllegalStateException("SensorReading " + readingId + " is not associated with Sensor " + sensorId);
         }

         // Proceed with deletion (link breaking happens via helpers or cascades)
         deleteReading(readingId); // Call the main delete method
     }


    /**
     * Finds all readings associated with a specific Sensor ID.
     */
    @Transactional(readOnly = true)
    public List<SensorReading> findReadingsBySensorId(int sensorId) {
        // Check if sensor exists first
        if (!sensorRepository.existsById(sensorId)) {
             throw new RuntimeException("Sensor not found with id: " + sensorId);
        }
        // Use derived query in SensorReadingRepository (most efficient):
        // return sensorReadingRepository.findBySensor_SensorIDOrderByTimeStampDesc(sensorId);

        // Manual filtering alternative:
        return sensorReadingRepository.findAll().stream()
                .filter(reading -> reading.getSensor() != null && reading.getSensor().getSensorID() == sensorId)
                .collect(Collectors.toList());
    }

}