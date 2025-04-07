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
class AvatarLogTest {

    // Mocks for associated entities
    @Mock
    private Avatar mockAvatar;
    @Mock
    private Avatar mockOtherAvatar;

    // Class Under Test
    private AvatarLog avatarLog;

    // Test Constants
    private final int LOG_ID = 501;
    private final Date LOG_DATE = Date.valueOf(LocalDate.now().minusDays(1));
    private final Time LOG_TIME = Time.valueOf(LocalTime.now().minusHours(1));
    private final Log.LogType LOG_TYPE = Log.LogType.Info;
    private final String LOG_MESSAGE = "Avatar initiated startup.";

    @BeforeEach
    void setUp() {
        // Use no-arg constructor + setters
        avatarLog = new AvatarLog();
        avatarLog.setLogID(LOG_ID);
        avatarLog.setLogDate(LOG_DATE);
        avatarLog.setLogTime(LOG_TIME);
        avatarLog.setLogType(LOG_TYPE);
        avatarLog.setLogMessage(LOG_MESSAGE);
        // Association set in specific tests
    }

    // --- Constructor Test (using no-arg and setters) ---
    @Test
    void shouldInitializeCorrectlyViaSetters() {
        assertNotNull(avatarLog);
        assertEquals(LOG_ID, avatarLog.getLogID());
        assertEquals(LOG_DATE, avatarLog.getLogDate());
        assertEquals(LOG_TIME, avatarLog.getLogTime());
        assertEquals(LOG_TYPE, avatarLog.getLogType());
        assertEquals(LOG_MESSAGE, avatarLog.getLogMessage());
        assertNull(avatarLog.getAvatar(), "Avatar should be null initially");
    }

    // Note: Tests for inherited setters (ID, Date, Time, Type, Message) are in LogTest.

    // --- Avatar Association Management ---

    @Test
    void setAvatar_shouldSetAvatarAndAddLogToAvatar_whenNewNonNullAvatar() {
        // Arrange: Avatar initially null

        // Act
        boolean wasSet = avatarLog.setAvatar(mockAvatar);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockAvatar, avatarLog.getAvatar());

        // Assert Interactions (verify link was set on Avatar)
        verify(mockAvatar).addAvatarLog(avatarLog);
        verify(mockAvatar, never()).removeAvatarLog(any(AvatarLog.class));
    }

    @Test
    void setAvatar_shouldReturnFalse_whenAvatarIsNull() {
        // Arrange: Avatar initially null

        // Act
        boolean wasSet = avatarLog.setAvatar(null);

        // Assert State
        assertFalse(wasSet); // Implementation returns false for null
        assertNull(avatarLog.getAvatar());

        // Assert Interactions
        verifyNoInteractions(mockAvatar);
    }

    @Test
    void setAvatar_shouldTransferAvatarAndManageLinks_whenChangingAvatars() {
        // Arrange: Initially set to mockAvatar
        avatarLog.setAvatar(mockAvatar);
        reset(mockAvatar); // Reset interactions

        // Act: Set to a different avatar
        boolean wasSet = avatarLog.setAvatar(mockOtherAvatar);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockOtherAvatar, avatarLog.getAvatar());

        // Assert Interactions
        verify(mockAvatar).removeAvatarLog(avatarLog);     // Should be removed from old avatar
        verify(mockOtherAvatar).addAvatarLog(avatarLog); // Should be added to new avatar
    }

    @Test
    void setAvatar_shouldDoNothing_whenSettingSameAvatar() {
         // Arrange: Initially set to mockAvatar
        avatarLog.setAvatar(mockAvatar);
        reset(mockAvatar);

        // Act: Set same avatar again
        boolean wasSet = avatarLog.setAvatar(mockAvatar);

         // Assert State
        assertTrue(wasSet); // Should still return true
        assertSame(mockAvatar, avatarLog.getAvatar()); // Still the same avatar

        // Assert Interactions
        verify(mockAvatar, never()).removeAvatarLog(any(AvatarLog.class));
        verify(mockAvatar, never()).addAvatarLog(any(AvatarLog.class));
    }


    // --- delete() Method ---
    @Test
    void delete_shouldRemoveLogFromAvatar_whenAvatarIsSet() {
        // Arrange: Set an avatar
        avatarLog.setAvatar(mockAvatar);
        reset(mockAvatar); // Reset interactions from setup

        // Act
        avatarLog.delete(); // Call the entity's delete helper

        // Assert State
        assertNull(avatarLog.getAvatar(), "Internal avatar reference should be nulled");

        // Assert Interactions
        verify(mockAvatar).removeAvatarLog(avatarLog); // Verify link break called
    }

    @Test
    void delete_shouldDoNothingWithAvatarLink_whenAvatarIsNull() {
        // Arrange: Avatar is already null

        // Act
        avatarLog.delete();

        // Assert State
        assertNull(avatarLog.getAvatar());

        // Assert Interactions
        verifyNoInteractions(mockAvatar);
        verifyNoInteractions(mockOtherAvatar);
    }

    // Note: equals() and hashCode() are inherited from Log and assumed tested in LogTest.
}