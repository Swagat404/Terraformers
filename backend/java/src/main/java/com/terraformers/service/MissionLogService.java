package com.terraformers.service;

import java.util.List; // Needed for linking
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired; // Needed to link log
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terraformers.model.Mission;
import com.terraformers.model.MissionLog;
import com.terraformers.repository.MissionLogRepository;
import com.terraformers.repository.MissionRepository;

@Service
public class MissionLogService {

    @Autowired
    private MissionLogRepository missionLogRepository;

    @Autowired
    private MissionRepository missionRepository;

    @Transactional(readOnly = true)
    public List<MissionLog> getAllMissionLogs() {
        // Use with caution, could be very large!
        return missionLogRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<MissionLog> getMissionLogById(int id) {
        return missionLogRepository.findById(id);
    }

    /**
     * Finds all logs associated with a specific Mission ID.
     */
    @Transactional(readOnly = true)
    public List<MissionLog> findLogsByMissionId(int missionId) {
        // Check if mission exists first
        if (!missionRepository.existsById(missionId)) {
             throw new RuntimeException("Mission not found with id: " + missionId);
             // throw new ResourceNotFoundException("Mission", "id", missionId);
        }
        // Efficient way using derived query (add to MissionLogRepository):
        // return missionLogRepository.findByMission_MissionIDOrderByLogDateDescLogTimeDesc(missionId);

        // Manual filtering alternative:
        return missionLogRepository.findAll().stream()
                .filter(log -> log.getMission() != null && log.getMission().getMissionID() == missionId)
                // Optional: Add sorting
                .collect(Collectors.toList());
    }

    /**
     * Creates a new MissionLog associated with a specific Mission.
     */
    @Transactional
    public MissionLog createLogForMission(MissionLog logDetails, int missionId) {
         if (logDetails == null) throw new IllegalArgumentException("Log details cannot be null");

         Mission mission = missionRepository.findById(missionId)
                 .orElseThrow(() -> new RuntimeException("Mission not found with id: " + missionId + " while creating mission log"));

         MissionLog newLog = new MissionLog();
         // newLog.setLogID(0); // If ID is generated

         // Copy attributes (inherited and specific)
         newLog.setLogID(logDetails.getLogID()); // If manual IDs
         newLog.setLogDate(logDetails.getLogDate());
         newLog.setLogTime(logDetails.getLogTime());
         newLog.setLogType(logDetails.getLogType());
         newLog.setLogMessage(logDetails.getLogMessage());

         // Set association using JPA-aware setter
         newLog.setMission(mission); // This should also add log to mission.getMissionLogs()

         return missionLogRepository.save(newLog);
    }

    /**
     * Deletes a MissionLog by its ID.
     * Ensures link from Mission is broken first.
     */
    @Transactional
    public void deleteLog(int id) {
        MissionLog log = missionLogRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("MissionLog not found with id: " + id));

        // Break link from parent Mission
        if (log.getMission() != null) {
             log.getMission().removeMissionLog(log);
        }

        missionLogRepository.delete(log); // Or deleteById(id)
    }

     /**
      * Deletes a specific log only if it belongs to the specified mission.
      */
     @Transactional
     public void deleteLog(int logId, int missionId) {
         MissionLog log = missionLogRepository.findById(logId)
                 .orElseThrow(() -> new RuntimeException("MissionLog not found with id: " + logId));

         // Verify association
         if (log.getMission() == null || log.getMission().getMissionID() != missionId) {
             throw new IllegalStateException("MissionLog " + logId + " is not associated with Mission " + missionId);
         }

         // Proceed with deletion
         deleteLog(logId); // Call the main delete method
     }

    // Update method for logs typically not needed.
}