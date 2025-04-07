package com.terraformers.model;

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
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AvatarTest {

    // Mocks for associated entities
    @Mock private Location mockLocation;
    @Mock private Location mockOtherLocation;
    @Mock private AvatarLog mockAvatarLog1;
    @Mock private AvatarLog mockOtherAvatarLog; // Needed if testing transfer for logs
    @Mock private Sensor mockSensor1;
    @Mock private Sensor mockOtherSensor; // Needed if testing transfer for sensors
    @Mock private AvatarBrain mockAvatarBrain;
    @Mock private AvatarBrain mockOtherAvatarBrain;
    @Mock private Mission mockMission;
    @Mock private Mission mockOtherMission;
    @Mock private Avatar mockOtherAvatar; // For association checks

    // Class Under Test
    private Avatar avatar;

    // Test Constants
    private final int AVATAR_ID = 1;
    private final String AVATAR_NAME = "TestBot";
    private final Avatar.AvatarColor AVATAR_COLOR = Avatar.AvatarColor.Blue;

    @BeforeEach
    void setUp() {
        // Use no-arg constructor + setters for setup consistency
        avatar = new Avatar();
        avatar.setAvatarID(AVATAR_ID);
        avatar.setAvatarName(AVATAR_NAME);
        avatar.setAvatarColor(AVATAR_COLOR);
        // Associations set in specific tests
    }

    // --- Constructor Test (using no-arg and setters) ---
    @Test
    void shouldInitializeCorrectlyViaSetters() {
        assertNotNull(avatar);
        assertEquals(AVATAR_ID, avatar.getAvatarID());
        assertEquals(AVATAR_NAME, avatar.getAvatarName());
        assertEquals(AVATAR_COLOR, avatar.getAvatarColor());
        assertNull(avatar.getLocation());
        assertNull(avatar.getAvatarBrain());
        assertNull(avatar.getMission());
        assertNotNull(avatar.getAvatarLogs());
        assertTrue(avatar.getAvatarLogs().isEmpty());
        assertNotNull(avatar.getSensors());
        assertTrue(avatar.getSensors().isEmpty());
    }

    // --- Basic Setters ---
    @Test
    void shouldSetAvatarName() {
        avatar.setAvatarName("NewName");
        assertEquals("NewName", avatar.getAvatarName());
    }

    @Test
    void shouldSetAvatarColor() {
        avatar.setAvatarColor(Avatar.AvatarColor.Green);
        assertEquals(Avatar.AvatarColor.Green, avatar.getAvatarColor());
    }

    // --- Location Association (@OneToOne, Avatar owns FK) ---

    @Test
    void setLocation_shouldSetLocationAndManageLink() {
        // Arrange: New location, assume it has no avatar yet
        when(mockLocation.getAvatar()).thenReturn(null);

        // Act
        boolean wasSet = avatar.setLocation(mockLocation);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockLocation, avatar.getLocation());

        // Assert Interactions: Avatar told new Location to point back
        verify(mockLocation).setAvatar(avatar);
    }

    @Test
    void setLocation_shouldTransferLocationAndManageLinks() {
        // Arrange: Avatar initially has mockOtherLocation
        // Stub the relationship *before* setting it initially
        when(mockOtherLocation.getAvatar()).thenReturn(avatar); // Old location points here
        avatar.setLocation(mockOtherLocation);
        // --- DO NOT RESET mockOtherLocation HERE ---
        // reset(mockOtherLocation); // REMOVE THIS LINE

        // Arrange: New location has no avatar
        when(mockLocation.getAvatar()).thenReturn(null);

        // Act
        boolean wasSet = avatar.setLocation(mockLocation);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockLocation, avatar.getLocation());

        // Assert Interactions: Now these should pass because the stubbing on mockOtherLocation is still active
        verify(mockOtherLocation).setAvatar(null); // Old location's link should be nulled
        verify(mockLocation).setAvatar(avatar);    // New location should point here
    }
    @Test
    void setLocation_shouldUnsetLocationAndManageLink() {
         // Arrange: Avatar initially has mockLocation
        // Stub the link *before* setting
        when(mockLocation.getAvatar()).thenReturn(avatar); // Location points here
        avatar.setLocation(mockLocation);
        // --- DO NOT RESET mockLocation ---
        // reset(mockLocation); // REMOVE THIS LINE

        // Act: Set location to null
        boolean wasSet = avatar.setLocation(null);

        // Assert State
        assertTrue(wasSet); // Setter returns true
        assertNull(avatar.getLocation());

        // Assert Interactions: Old location link should be nulled because the check passed
        verify(mockLocation).setAvatar(null); // This should now pass
    }

    // --- AvatarBrain Association (@OneToOne, Avatar owns FK) ---
    // (Tests follow the same pattern as setLocation)
    @Test
    void setAvatarBrain_shouldSetBrainAndManageLink() {
        when(mockAvatarBrain.getAvatar()).thenReturn(null);
        assertTrue(avatar.setAvatarBrain(mockAvatarBrain));
        assertSame(mockAvatarBrain, avatar.getAvatarBrain());
        verify(mockAvatarBrain).setAvatar(avatar);
    }
    @Test
    void setAvatarBrain_shouldTransferBrainAndManageLinks() {
        // Arrange: Avatar initially has mockOtherAvatarBrain
        // Stub the link *before* setting
        when(mockOtherAvatarBrain.getAvatar()).thenReturn(avatar); // Old brain points here
        avatar.setAvatarBrain(mockOtherAvatarBrain);
        // --- DO NOT RESET mockOtherAvatarBrain ---
        // reset(mockOtherAvatarBrain); // REMOVE THIS LINE

        // Arrange: New brain has no avatar initially
        when(mockAvatarBrain.getAvatar()).thenReturn(null);

        // Act
        assertTrue(avatar.setAvatarBrain(mockAvatarBrain));

        // Assert State
        assertSame(mockAvatarBrain, avatar.getAvatarBrain());

        // Assert Interactions
        verify(mockOtherAvatarBrain).setAvatar(null); // Old brain's link should be nulled
        verify(mockAvatarBrain).setAvatar(avatar);    // New brain should point here
    }
    
    @Test
    void setAvatarBrain_shouldUnsetBrainAndManageLink() {
        // Arrange: Avatar initially has mockAvatarBrain
        // Stub the link *before* setting
        when(mockAvatarBrain.getAvatar()).thenReturn(avatar); // Brain points here
        avatar.setAvatarBrain(mockAvatarBrain);
        // --- DO NOT RESET mockAvatarBrain ---
        // reset(mockAvatarBrain); // REMOVE THIS LINE

        // Act: Set brain to null
        assertTrue(avatar.setAvatarBrain(null));

        // Assert State
        assertNull(avatar.getAvatarBrain());

        // Assert Interactions: Old brain link should be nulled
        verify(mockAvatarBrain).setAvatar(null); // This should now pass
    }

    // --- Mission Association (@ManyToOne, Avatar owns FK) ---
    @Test
    void setMission_shouldSetMissionAndAddAvatarToMission() {
        // Arrange: Mission initially null

        // Act
        boolean wasSet = avatar.setMission(mockMission);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockMission, avatar.getMission());

        // Assert Interactions
        verify(mockMission).addAvatar(avatar);
        verify(mockMission, never()).removeAvatar(any(Avatar.class));
    }

     @Test
    void setMission_shouldTransferMissionAndManageLinks() {
        // Arrange: Initially set to mockOtherMission
        avatar.setMission(mockOtherMission);
        reset(mockOtherMission);

        // Act: Set to mockMission
        boolean wasSet = avatar.setMission(mockMission);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockMission, avatar.getMission());

        // Assert Interactions
        verify(mockOtherMission).removeAvatar(avatar); // Removed from old
        verify(mockMission).addAvatar(avatar);       // Added to new
    }

    @Test
    void setMission_shouldUnsetMissionAndManageLink() {
        // Arrange: Initially set to mockMission
        avatar.setMission(mockMission);
        reset(mockMission);

        // Act: Set to null
        boolean wasSet = avatar.setMission(null);

        // Assert State
        assertTrue(wasSet);
        assertNull(avatar.getMission());

        // Assert Interactions
        verify(mockMission).removeAvatar(avatar);      // Removed from old
        verify(mockMission, never()).addAvatar(avatar); // Not added back
    }

     @Test
    void setMission_shouldDoNothing_whenSettingSameMission() {
        // Arrange: Initially set to mockMission
        avatar.setMission(mockMission);
        reset(mockMission);

        // Act: Set same mission again
        boolean wasSet = avatar.setMission(mockMission);

        // Assert State
        assertTrue(wasSet); // Returns true but no change
        assertSame(mockMission, avatar.getMission());

        // Assert Interactions
        verify(mockMission, never()).removeAvatar(any(Avatar.class));
        verify(mockMission, never()).addAvatar(any(Avatar.class));
    }

    // --- AvatarLog Association (@OneToMany, mappedBy="avatar") ---

    @Test
    void addAvatarLog_shouldAddLogAndSetBidirectionalLink() {
        // Arrange: Log is new
        when(mockAvatarLog1.getAvatar()).thenReturn(null);

        // Act
        avatar.addAvatarLog(mockAvatarLog1);

        // Assert State
        assertEquals(1, avatar.numberOfAvatarLogs());
        assertTrue(avatar.getAvatarLogs().contains(mockAvatarLog1));

        // Assert Interactions
        verify(mockAvatarLog1).setAvatar(avatar);
    }

     @Test
    void addAvatarLog_shouldNotAddDuplicate() {
        // Arrange: Add once
        when(mockAvatarLog1.getAvatar()).thenReturn(null);
        avatar.addAvatarLog(mockAvatarLog1);
        assertEquals(1, avatar.numberOfAvatarLogs());
        reset(mockAvatarLog1); // Reset interactions after first add

        // Act: Add again
        avatar.addAvatarLog(mockAvatarLog1);

        // Assert State
        assertEquals(1, avatar.numberOfAvatarLogs()); // Size unchanged

        // Assert Interactions (setAvatar should not be called again)
        verify(mockAvatarLog1, never()).setAvatar(avatar);
    }

    @Test
    void removeAvatarLog_shouldRemoveLogAndBreakBidirectionalLink_whenLogPointsHere() { // Renamed for clarity
         // Arrange: Add log
        when(mockAvatarLog1.getAvatar()).thenReturn(null);
        avatar.addAvatarLog(mockAvatarLog1);
        assertEquals(1, avatar.numberOfAvatarLogs());
        // Arrange: Ensure it points HERE *before* remove is called
        when(mockAvatarLog1.getAvatar()).thenReturn(avatar); // Points HERE

        // --- DO NOT RESET mockAvatarLog1 ---
        // reset(mockAvatarLog1); // REMOVE THIS LINE

        // Act
        avatar.removeAvatarLog(mockAvatarLog1);

        // Assert State
        assertEquals(0, avatar.numberOfAvatarLogs());
        assertFalse(avatar.getAvatarLogs().contains(mockAvatarLog1));

        // Assert Interactions
        verify(mockAvatarLog1).setAvatar(null); // Break link - this should now pass
    }



     @Test
    void removeAvatarLog_shouldDoNothingIfLogNotPresent() {
         // Arrange: Log list is empty

        // Act
        avatar.removeAvatarLog(mockAvatarLog1);

        // Assert State
        assertEquals(0, avatar.numberOfAvatarLogs());

        // Assert Interactions
        verify(mockAvatarLog1, never()).setAvatar(any());
    }


    // --- Sensor Association (@OneToMany, mappedBy="avatar") ---
    // (Tests follow the same pattern as AvatarLog)

    @Test
    void addSensor_shouldAddSensorAndSetBidirectionalLink() {
        when(mockSensor1.getAvatar()).thenReturn(null);
        avatar.addSensor(mockSensor1);
        assertEquals(1, avatar.numberOfSensors());
        assertTrue(avatar.getSensors().contains(mockSensor1));
        verify(mockSensor1).setAvatar(avatar);
    }

     @Test
    void addSensor_shouldNotAddDuplicate() {
        when(mockSensor1.getAvatar()).thenReturn(null);
        avatar.addSensor(mockSensor1);
        assertEquals(1, avatar.numberOfSensors());
        reset(mockSensor1);

        avatar.addSensor(mockSensor1); // Add again
        assertEquals(1, avatar.numberOfSensors());
        verify(mockSensor1, never()).setAvatar(avatar);
    }

    @Test
void removeSensor_shouldRemoveSensorAndBreakBidirectionalLink_whenSensorPointsHere() { // Renamed for clarity
    // Arrange: Add sensor
    when(mockSensor1.getAvatar()).thenReturn(null);
    avatar.addSensor(mockSensor1);
    assertEquals(1, avatar.numberOfSensors());
    // Arrange: Ensure it points HERE *before* remove is called
    when(mockSensor1.getAvatar()).thenReturn(avatar); // Points HERE

    // --- DO NOT RESET mockSensor1 ---
    // reset(mockSensor1); // REMOVE THIS LINE

    // Act
    avatar.removeSensor(mockSensor1);

    // Assert State
    assertEquals(0, avatar.numberOfSensors());
    assertFalse(avatar.getSensors().contains(mockSensor1));

    // Assert Interactions
    verify(mockSensor1).setAvatar(null); // Break link - this should now pass
}

     @Test
    void removeSensor_shouldDoNothingIfSensorNotPresent() {
        avatar.removeSensor(mockSensor1);
        assertEquals(0, avatar.numberOfSensors());
        verify(mockSensor1, never()).setAvatar(any());
    }


    // Note: equals() and hashCode() tests assumed standard ID checks
}