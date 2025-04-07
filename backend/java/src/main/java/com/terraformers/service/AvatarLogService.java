package com.terraformers.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired; // To link log to avatar
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.terraformers.model.Avatar;
import com.terraformers.model.AvatarLog;
import com.terraformers.repository.AvatarLogRepository;
import com.terraformers.repository.AvatarRepository;

@Service
public class AvatarLogService {

    @Autowired
    private AvatarLogRepository avatarLogRepository;

    @Autowired
    private AvatarRepository avatarRepository;

    @Transactional(readOnly = true)
    public List<AvatarLog> getAllAvatarLogs() {
        // Use with caution, could be very large
        return avatarLogRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<AvatarLog> getAvatarLogById(int id) {
        return avatarLogRepository.findById(id);
    }

    /**
     * Finds all logs associated with a specific Avatar ID.
     */
    @Transactional(readOnly = true)
    public List<AvatarLog> findLogsByAvatarId(int avatarId) {
        // Check if avatar exists first
        if (!avatarRepository.existsById(avatarId)) {
             throw new RuntimeException("Avatar not found with id: " + avatarId);
        }
        // Efficient way using derived query (add to AvatarLogRepository):
        // return avatarLogRepository.findByAvatar_AvatarIDOrderByLogDateDescLogTimeDesc(avatarId);

        // Manual filtering alternative:
        return avatarLogRepository.findAll().stream()
                .filter(log -> log.getAvatar() != null && log.getAvatar().getAvatarID() == avatarId)
                 // Optional: Add sorting
                 // .sorted(Comparator.comparing(AvatarLog::getLogDate, Comparator.nullsLast(Comparator.reverseOrder()))
                 //          .thenComparing(AvatarLog::getLogTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    /**
     * Creates a new AvatarLog associated with a specific Avatar.
     */
    @Transactional
    public AvatarLog createLogForAvatar(AvatarLog logDetails, int avatarId) {
         if (logDetails == null) throw new IllegalArgumentException("Log details cannot be null");

         Avatar avatar = avatarRepository.findById(avatarId)
                 .orElseThrow(() -> new RuntimeException("Avatar not found with id: " + avatarId + " while creating avatar log"));

         AvatarLog newLog = new AvatarLog();
         // newLog.setLogID(0); // If ID is generated

         // Copy attributes (inherited and specific)
         newLog.setLogID(logDetails.getLogID()); // If manual IDs
         newLog.setLogDate(logDetails.getLogDate());
         newLog.setLogTime(logDetails.getLogTime());
         newLog.setLogType(logDetails.getLogType());
         newLog.setLogMessage(logDetails.getLogMessage());

         // Set association using JPA-aware setter
         newLog.setAvatar(avatar); // This should also add log to avatar.getAvatarLogs()

         return avatarLogRepository.save(newLog);
    }

    /**
     * Deletes an AvatarLog by its ID.
     * Ensures link from Avatar is broken first.
     */
    @Transactional
    public void deleteLog(int id) {
        AvatarLog log = avatarLogRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("AvatarLog not found with id: " + id));

        // Break link from parent Avatar
        if (log.getAvatar() != null) {
             log.getAvatar().removeAvatarLog(log);
             // No need to save avatar explicitly if within same transaction usually
        }

        avatarLogRepository.delete(log); // Or deleteById(id)
    }

     /**
      * Deletes a specific log only if it belongs to the specified avatar.
      */
     @Transactional
     public void deleteLog(int logId, int avatarId) {
         AvatarLog log = avatarLogRepository.findById(logId)
                 .orElseThrow(() -> new RuntimeException("AvatarLog not found with id: " + logId));

         // Verify association
         if (log.getAvatar() == null || log.getAvatar().getAvatarID() != avatarId) {
             throw new IllegalStateException("AvatarLog " + logId + " is not associated with Avatar " + avatarId);
         }

         // Proceed with deletion (link breaking handled by helper or cascade)
         deleteLog(logId); // Call the main delete method
     }

    // Update method for logs might be less common, but could be added if needed.
    // Usually logs are append-only.
}