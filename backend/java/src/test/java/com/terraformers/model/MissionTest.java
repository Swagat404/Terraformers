package com.terraformers.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MissionTest {

    // Mocks for associated entities
    @Mock
    private MissionLog mockMissionLog1;
    @Mock
    private MissionLog mockMissionLog2;
    @Mock
    private Avatar mockAvatar1;
    @Mock
    private Avatar mockAvatar2;
    @Mock
    private Mission mockOtherMission; // For transfer tests

    // Class Under Test
    private Mission mission;

    // Test Constants
    private final int MISSION_ID = 301;
    private final String MISSION_NAME = "Explore Sector 5";
    private final Mission.MissionStatus INITIAL_STATUS = Mission.MissionStatus.Planned;
    private final String OBJECTIVE = "Map geological features and search for water ice.";

    @BeforeEach
    void setUp() {
        // Use constructor to initialize basic fields
        mission = new Mission(MISSION_ID, MISSION_NAME, INITIAL_STATUS, OBJECTIVE);
    }

    // --- Constructor Test ---
    @Test
    void shouldInitializeCorrectlyViaConstructor() {
        assertNotNull(mission);
        assertEquals(MISSION_ID, mission.getMissionID());
        assertEquals(MISSION_NAME, mission.getMissionName());
        assertEquals(INITIAL_STATUS, mission.getStatus());
        assertEquals(OBJECTIVE, mission.getObjective());
        assertNotNull(mission.getMissionLogs());
        assertTrue(mission.getMissionLogs().isEmpty());
        assertNotNull(mission.getAvatars());
        assertTrue(mission.getAvatars().isEmpty());
    }

    // --- Basic Setters ---
    @Test
    void shouldSetMissionName() {
        mission.setMissionName("New Name");
        assertEquals("New Name", mission.getMissionName());
    }

    @Test
    void shouldSetStatus() {
        mission.setStatus(Mission.MissionStatus.Ongoing);
        assertEquals(Mission.MissionStatus.Ongoing, mission.getStatus());
    }

    @Test
    void shouldSetObjective() {
        mission.setObjective("New Objective");
        assertEquals("New Objective", mission.getObjective());
    }

    // --- MissionLog Association Management ---

    @Test
    void addMissionLog_shouldAddLogAndSetBidirectionalLink_whenLogIsNew() {
        // Arrange: Log initially belongs to null
        when(mockMissionLog1.getMission()).thenReturn(null);

        // Act
        mission.addMissionLog(mockMissionLog1);

        // Assert State
        assertEquals(1, mission.numberOfMissionLogs());
        assertTrue(mission.getMissionLogs().contains(mockMissionLog1));

        // Assert Interactions (verify link was set on log)
        verify(mockMissionLog1).setMission(mission);
    }

     @Test
    void addMissionLog_shouldAddLogAndSetBidirectionalLink_whenTransferring() {
        // Arrange: Log belongs to another mission
        when(mockMissionLog1.getMission()).thenReturn(mockOtherMission);

        // Act
        mission.addMissionLog(mockMissionLog1);

        // Assert State
        assertEquals(1, mission.numberOfMissionLogs());
        assertTrue(mission.getMissionLogs().contains(mockMissionLog1));

        // Assert Interactions (verify link was set on log)
        verify(mockMissionLog1).setMission(mission);
    }

    @Test
    void addMissionLog_shouldNotAddDuplicateLog() {
        // Arrange: Add the log once
        when(mockMissionLog1.getMission()).thenReturn(null);
        mission.addMissionLog(mockMissionLog1);
        assertEquals(1, mission.numberOfMissionLogs());

        // Act: Try adding the same log again
        mission.addMissionLog(mockMissionLog1);

        // Assert State didn't change
        assertEquals(1, mission.numberOfMissionLogs());
    }

    @Test
    void removeMissionLog_shouldRemoveLogAndBreakBidirectionalLink_whenLogPointsHere() {
        // Arrange: Add log, make it point HERE
        when(mockMissionLog1.getMission()).thenReturn(null);
        mission.addMissionLog(mockMissionLog1);
        assertEquals(1, mission.numberOfMissionLogs());
        when(mockMissionLog1.getMission()).thenReturn(mission); // Point HERE

        // Act
        mission.removeMissionLog(mockMissionLog1);

        // Assert State
        assertEquals(0, mission.numberOfMissionLogs());
        assertFalse(mission.getMissionLogs().contains(mockMissionLog1));

        // Assert Interactions (setMission(null) SHOULD have been called by removeMissionLog)
        verify(mockMissionLog1).setMission(null);
    }

    @Test
    void removeMissionLog_shouldRemoveLogOnly_whenLogPointsElsewhere() {
        // Arrange: Add log, make it point elsewhere
        when(mockMissionLog1.getMission()).thenReturn(null);
        mission.addMissionLog(mockMissionLog1);
        assertEquals(1, mission.numberOfMissionLogs());
        when(mockMissionLog1.getMission()).thenReturn(mockOtherMission); // Point ELSEWHERE

        // Act
        mission.removeMissionLog(mockMissionLog1);

        // Assert State
        assertEquals(0, mission.numberOfMissionLogs());
        assertFalse(mission.getMissionLogs().contains(mockMissionLog1));

        // Assert Interactions (setMission(null) should NOT have been called)
        verify(mockMissionLog1, never()).setMission(null);
    }

    // --- Avatar Association Management --- (Similar pattern to MissionLog)

    @Test
    void addAvatar_shouldAddAvatarAndSetBidirectionalLink_whenAvatarIsNew() {
        // Arrange: Avatar initially belongs to null
        when(mockAvatar1.getMission()).thenReturn(null);

        // Act
        mission.addAvatar(mockAvatar1);

        // Assert State
        assertEquals(1, mission.numberOfAvatars());
        assertTrue(mission.getAvatars().contains(mockAvatar1));

        // Assert Interactions (verify link was set on avatar)
        verify(mockAvatar1).setMission(mission);
    }

    @Test
    void addAvatar_shouldAddAvatarAndSetBidirectionalLink_whenTransferring() {
        // Arrange: Avatar belongs to another mission
        when(mockAvatar1.getMission()).thenReturn(mockOtherMission);

        // Act
        mission.addAvatar(mockAvatar1);

        // Assert State
        assertEquals(1, mission.numberOfAvatars());
        assertTrue(mission.getAvatars().contains(mockAvatar1));

        // Assert Interactions (verify link was set on avatar)
        verify(mockAvatar1).setMission(mission);
    }

    @Test
    void addAvatar_shouldNotAddDuplicateAvatar() {
        // Arrange: Add the avatar once
        when(mockAvatar1.getMission()).thenReturn(null);
        mission.addAvatar(mockAvatar1);
        assertEquals(1, mission.numberOfAvatars());

        // Act: Try adding the same avatar again
        mission.addAvatar(mockAvatar1);

        // Assert State didn't change
        assertEquals(1, mission.numberOfAvatars());
    }

     @Test
    void removeAvatar_shouldRemoveAvatarAndBreakBidirectionalLink_whenAvatarPointsHere() {
        // Arrange: Add avatar, make it point HERE
        when(mockAvatar1.getMission()).thenReturn(null);
        mission.addAvatar(mockAvatar1);
        assertEquals(1, mission.numberOfAvatars());
        when(mockAvatar1.getMission()).thenReturn(mission); // Point HERE

        // Act
        mission.removeAvatar(mockAvatar1);

        // Assert State
        assertEquals(0, mission.numberOfAvatars());
        assertFalse(mission.getAvatars().contains(mockAvatar1));

        // Assert Interactions (setMission(null) SHOULD have been called by removeAvatar)
        verify(mockAvatar1).setMission(null);
    }

    @Test
    void removeAvatar_shouldRemoveAvatarOnly_whenAvatarPointsElsewhere() {
        // Arrange: Add avatar, make it point elsewhere
        when(mockAvatar1.getMission()).thenReturn(null);
        mission.addAvatar(mockAvatar1);
        assertEquals(1, mission.numberOfAvatars());
        when(mockAvatar1.getMission()).thenReturn(mockOtherMission); // Point ELSEWHERE

        // Act
        mission.removeAvatar(mockAvatar1);

        // Assert State
        assertEquals(0, mission.numberOfAvatars());
        assertFalse(mission.getAvatars().contains(mockAvatar1));

        // Assert Interactions (setMission(null) should NOT have been called)
        verify(mockAvatar1, never()).setMission(null);
    }


    // Note: equals() and hashCode() tests assumed standard ID checks

}