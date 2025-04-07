package com.terraformers.model;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MissionLogTest {

    // Mocks for associated entities
    @Mock
    private Mission mockMission;
    @Mock
    private Mission mockOtherMission;

    // Class Under Test
    private MissionLog missionLog;

    // Test Constants
    private final int LOG_ID = 601;
    // Use java.sql.Date/Time as per model, create from java.time for convenience
    private final Date LOG_DATE = Date.valueOf(LocalDate.now().minusDays(2));
    private final Time LOG_TIME = Time.valueOf(LocalTime.now().minusHours(1));
    private final Log.LogType LOG_TYPE = Log.LogType.Error;
    private final String LOG_MESSAGE = "Mission objective cannot be reached.";

    @BeforeEach
    void setUp() {
        // Use no-arg constructor + setters
        missionLog = new MissionLog();
        missionLog.setLogID(LOG_ID);
        missionLog.setLogDate(LOG_DATE);
        missionLog.setLogTime(LOG_TIME);
        missionLog.setLogType(LOG_TYPE);
        missionLog.setLogMessage(LOG_MESSAGE);
        // Association set in specific tests
    }

    // --- Constructor Test (using no-arg and setters) ---
    @Test
    void shouldInitializeCorrectlyViaSetters() {
        assertNotNull(missionLog);
        assertEquals(LOG_ID, missionLog.getLogID());
        assertEquals(LOG_DATE, missionLog.getLogDate());
        assertEquals(LOG_TIME, missionLog.getLogTime());
        assertEquals(LOG_TYPE, missionLog.getLogType());
        assertEquals(LOG_MESSAGE, missionLog.getLogMessage());
        assertNull(missionLog.getMission(), "Mission should be null initially");
    }

    // Note: Tests for inherited setters (ID, Date, Time, Type, Message) are in LogTest.

    // --- Mission Association Management ---

    @Test
    void setMission_shouldSetMissionAndAddLogToMission_whenNewNonNullMission() {
        // Arrange: Mission initially null

        // Act
        boolean wasSet = missionLog.setMission(mockMission);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockMission, missionLog.getMission());

        // Assert Interactions (verify link was set on Mission)
        verify(mockMission).addMissionLog(missionLog);
        verify(mockMission, never()).removeMissionLog(any(MissionLog.class));
    }

    @Test
    void setMission_shouldReturnFalse_whenMissionIsNull() {
        // Arrange: Mission initially null

        // Act
        boolean wasSet = missionLog.setMission(null);

        // Assert State
        assertFalse(wasSet); // Implementation returns false for null
        assertNull(missionLog.getMission());

        // Assert Interactions
        verifyNoInteractions(mockMission);
    }

    @Test
    void setMission_shouldTransferMissionAndManageLinks_whenChangingMissions() {
        // Arrange: Initially set to mockMission
        missionLog.setMission(mockMission);
        reset(mockMission); // Reset interactions

        // Act: Set to a different mission
        boolean wasSet = missionLog.setMission(mockOtherMission);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockOtherMission, missionLog.getMission());

        // Assert Interactions
        verify(mockMission).removeMissionLog(missionLog);     // Should be removed from old mission
        verify(mockOtherMission).addMissionLog(missionLog); // Should be added to new mission
    }

    @Test
    void setMission_shouldDoNothing_whenSettingSameMission() {
         // Arrange: Initially set to mockMission
        missionLog.setMission(mockMission);
        reset(mockMission);

        // Act: Set to the same mission again
        boolean wasSet = missionLog.setMission(mockMission);

         // Assert State
        assertTrue(wasSet); // Should still return true
        assertSame(mockMission, missionLog.getMission()); // Still the same mission

        // Assert Interactions
        verify(mockMission, never()).removeMissionLog(any(MissionLog.class));
        verify(mockMission, never()).addMissionLog(any(MissionLog.class));
    }


    // --- delete() Method ---
    @Test
    void delete_shouldRemoveLogFromMission_whenMissionIsSet() {
        // Arrange: Set a mission
        missionLog.setMission(mockMission);
        reset(mockMission); // Reset interactions from setup

        // Act
        missionLog.delete(); // Call the entity's delete helper

        // Assert State
        assertNull(missionLog.getMission(), "Internal mission reference should be nulled");

        // Assert Interactions
        verify(mockMission).removeMissionLog(missionLog); // Verify link break called
    }

    @Test
    void delete_shouldDoNothingWithMissionLink_whenMissionIsNull() {
        // Arrange: Mission is already null

        // Act
        missionLog.delete();

        // Assert State
        assertNull(missionLog.getMission());

        // Assert Interactions
        verifyNoInteractions(mockMission);
        verifyNoInteractions(mockOtherMission);
    }

    // Note: equals() and hashCode() are inherited from Log and assumed tested in LogTest.
}