package com.terraformers.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.terraformers.dto.MissionDTO;
import com.terraformers.model.Mission;
import com.terraformers.service.MissionService;

@RestController
@RequestMapping("/api/missions")
public class MissionRestController {

    @Autowired
    private MissionService missionService;

    // --- DTO Conversion Helpers ---
    private MissionDTO convertToDto(Mission mission) {
         if (mission == null) return null;
         MissionDTO dto = new MissionDTO();
         dto.setMissionID(mission.getMissionID());
         dto.setMissionName(mission.getMissionName());
         dto.setStatus(mission.getStatus());
         dto.setObjective(mission.getObjective());
         return dto;
    }
    private Mission convertToEntity(MissionDTO dto) {
         if (dto == null) return null;
         Mission mission = new Mission();
         mission.setMissionID(dto.getMissionID());
         mission.setMissionName(dto.getMissionName());
         mission.setStatus(dto.getStatus());
         mission.setObjective(dto.getObjective());
         return mission;
    }

    // --- Endpoints ---
    @GetMapping
    public List<MissionDTO> getAllMissions() { /* ... use service ... */ return null; } // Implement

    @GetMapping("/{id}")
    public ResponseEntity<MissionDTO> getMissionById(@PathVariable int id) { /* ... use service ... */ return null; } // Implement

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MissionDTO createMission(@RequestBody MissionDTO missionDTO) { /* ... use service ... */ return null; } // Implement

    @PutMapping("/{id}")
    public ResponseEntity<MissionDTO> updateMission(@PathVariable int id, @RequestBody MissionDTO missionDTO) { /* ... use service ... */ return null; } // Implement

    @PatchMapping("/{id}/status")
    public ResponseEntity<MissionDTO> updateStatus(@PathVariable int id, @RequestBody StatusUpdateRequest statusUpdate) { /* ... use service ... */ return null; } // Implement
    static class StatusUpdateRequest { /* ... see previous example ... */ }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMission(@PathVariable int id) { /* ... use service ... */ return null; } // Implement

    // Optional nested resource endpoints...
}