package com.terraformers.service;

import java.util.List; // Needed for linking
import java.util.Optional;
import java.util.stream.Collectors; // Needed for delete logic etc.

import org.springframework.beans.factory.annotation.Autowired; // Needed to link sensor
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terraformers.model.Avatar;
import com.terraformers.model.Sensor;
import com.terraformers.repository.AvatarRepository;
import com.terraformers.repository.SensorRepository;

// Import custom exception (create this class if desired)
// import com.terraformers.exception.ResourceNotFoundException;

@Service
public class SensorService {

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired // Needed for create/update links
    private AvatarRepository avatarRepository;

    @Transactional(readOnly = true)
    public List<Sensor> getAllSensors() {
        return sensorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Sensor> getSensorById(int id) {
        return sensorRepository.findById(id);
    }

    /**
     * Creates a new Sensor associated with a specific Avatar.
     */
    @Transactional
    public Sensor createSensorForAvatar(Sensor sensorDetails, int avatarId) {
        if (sensorDetails == null) throw new IllegalArgumentException("Sensor details cannot be null");

        Avatar avatar = avatarRepository.findById(avatarId)
                .orElseThrow(() -> new RuntimeException("Avatar not found with id: " + avatarId + " while creating sensor"));
                // .orElseThrow(() -> new ResourceNotFoundException("Avatar", "id", avatarId));

        Sensor newSensor = new Sensor();
        // newSensor.setSensorID(0); // If ID is generated

        // Copy attributes
        newSensor.setSensorID(sensorDetails.getSensorID()); // If using manual IDs
        newSensor.setMountPosition(sensorDetails.getMountPosition());
        newSensor.setStatus(sensorDetails.getStatus());
        newSensor.setSensorType(sensorDetails.getSensorType());
        // newSensor.setSensorReadings(new ArrayList<>()); // List initialized in entity

        // Set the mandatory association using JPA-aware setter
        newSensor.setAvatar(avatar); // This should also add sensor to avatar.getSensors()

        return sensorRepository.save(newSensor);
    }

    /**
     * Updates an existing Sensor. Can optionally change the associated Avatar.
     */
    @Transactional
    public Sensor updateSensor(int id, Sensor sensorDetails, Integer newAvatarId) {
        Sensor existingSensor = sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found with id: " + id));

        if (sensorDetails == null) throw new IllegalArgumentException("Sensor details cannot be null");

        // Update attributes
        existingSensor.setMountPosition(sensorDetails.getMountPosition());
        existingSensor.setStatus(sensorDetails.getStatus());
        existingSensor.setSensorType(sensorDetails.getSensorType());

        // Optionally update Avatar association
        if (newAvatarId != null && (existingSensor.getAvatar() == null || existingSensor.getAvatar().getAvatarID() != newAvatarId)) {
             Avatar newAvatar = avatarRepository.findById(newAvatarId)
                     .orElseThrow(() -> new RuntimeException("New Avatar not found with id: " + newAvatarId + " while updating sensor"));
             existingSensor.setAvatar(newAvatar); // Use JPA-aware setter
        }

        // Updating readings list handled separately

        return sensorRepository.save(existingSensor);
    }

    /**
     * Deletes a Sensor.
     * Cascade settings on Sensor entity handle deleting SensorReadings.
     */
    @Transactional
    public void deleteSensor(int id) {
        Sensor sensor = sensorRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Sensor not found with id: " + id));

        // Optional: Business logic checks (e.g., cannot delete 'Active' sensor?)

        // Ensure link is broken from Avatar side before deletion
        if (sensor.getAvatar() != null) {
             sensor.getAvatar().removeSensor(sensor); // Ensure parent state updated
        }

        sensorRepository.delete(sensor); // Or deleteById(id)
    }

     /**
      * Deletes a specific sensor only if it belongs to the specified avatar.
      */
     @Transactional
     public void deleteSensorFromAvatar(int avatarId, int sensorId) {
          Sensor sensor = sensorRepository.findById(sensorId)
                 .orElseThrow(() -> new RuntimeException("Sensor not found with id: " + sensorId));

         // Verify association
         if (sensor.getAvatar() == null || sensor.getAvatar().getAvatarID() != avatarId) {
             throw new IllegalStateException("Sensor " + sensorId + " is not associated with Avatar " + avatarId);
         }

          // Proceed with deletion (Cascade handles readings)
         if (sensor.getAvatar() != null) { // Should be true based on check above
             sensor.getAvatar().removeSensor(sensor); // Ensure parent state updated
         }
         sensorRepository.delete(sensor); // Or deleteById(sensorId)
     }


    /**
     * Finds all sensors associated with a specific Avatar ID.
     */
    @Transactional(readOnly = true)
    public List<Sensor> findSensorsByAvatarId(int avatarId) {
        // Check if avatar exists first
        if (!avatarRepository.existsById(avatarId)) {
             throw new RuntimeException("Avatar not found with id: " + avatarId);
        }
        // Use derived query in SensorRepository (most efficient):
        // return sensorRepository.findByAvatar_AvatarID(avatarId);

        // Manual filtering alternative:
        return sensorRepository.findAll().stream()
                .filter(sensor -> sensor.getAvatar() != null && sensor.getAvatar().getAvatarID() == avatarId)
                .collect(Collectors.toList());
    }

    // Add methods for adding/removing/getting SensorReadings for a Sensor if needed

}