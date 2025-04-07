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
class AvatarBrainTest {

    // Mocks for associated entities
    @Mock private Motor mockMotor1;
    @Mock private Motor mockMotor2;
    @Mock private Avatar mockAvatar;
    @Mock private Avatar mockNewAvatar;
    @Mock private AvatarBrain mockOtherAvatarBrain; // For transfer/comparison tests
    @Mock private Motor mockOtherMotor; // For reading transfer test (if needed, unused here)


    // Class Under Test
    private AvatarBrain avatarBrain;

    // Test Constants
    private final int BRAIN_ID = 101;
    private final AvatarBrain.AvatarBrainType BRAIN_TYPE = AvatarBrain.AvatarBrainType.TypeA;
    private final int SPEED = 50;
    private final int JUMP_HEIGHT = 10;
    private final float LENGTH = 1.5f;
    private final float WIDTH = 0.8f;
    private final float HEIGHT = 0.5f;

    @BeforeEach
    void setUp() {
        // Use constructor for initial setup
        avatarBrain = new AvatarBrain(BRAIN_ID, BRAIN_TYPE, SPEED, JUMP_HEIGHT, LENGTH, WIDTH, HEIGHT);
    }

    // --- Constructor Test ---
    @Test
    void shouldInitializeCorrectlyViaConstructor() {
        assertNotNull(avatarBrain);
        assertEquals(BRAIN_ID, avatarBrain.getAvatarBrainID());
        assertEquals(BRAIN_TYPE, avatarBrain.getAvatarBrainType());
        assertEquals(SPEED, avatarBrain.getAvatarSpeed());
        assertEquals(JUMP_HEIGHT, avatarBrain.getAvatarMaxJumpHeight());
        assertEquals(LENGTH, avatarBrain.getAvatarLength());
        assertEquals(WIDTH, avatarBrain.getAvatarWidth());
        assertEquals(HEIGHT, avatarBrain.getAvatarHeight());
        assertNotNull(avatarBrain.getMotors());
        assertTrue(avatarBrain.getMotors().isEmpty());
        assertNull(avatarBrain.getAvatar());
        assertFalse(avatarBrain.hasAvatar());
    }

    // --- Basic Setters ---
     @Test
    void shouldSetAvatarBrainType() {
        avatarBrain.setAvatarBrainType(AvatarBrain.AvatarBrainType.TypeB);
        assertEquals(AvatarBrain.AvatarBrainType.TypeB, avatarBrain.getAvatarBrainType());
    }
     @Test
    void shouldSetAvatarSpeed() {
        avatarBrain.setAvatarSpeed(60);
        assertEquals(60, avatarBrain.getAvatarSpeed());
    }
    // ... (Add similar tests for other setters: MaxJumpHeight, Length, Width, Height)

    // --- Avatar Association (@OneToOne, mappedBy="avatarBrain") ---

    @Test
    void setAvatar_shouldSetAvatarAndSetBrainOnAvatar_whenNewNonNullAvatar() {
        // Arrange: Avatar initially null
        when(mockNewAvatar.getAvatarBrain()).thenReturn(null); // Assume new avatar has no brain

        // Act
        boolean wasSet = avatarBrain.setAvatar(mockNewAvatar);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockNewAvatar, avatarBrain.getAvatar());
        assertTrue(avatarBrain.hasAvatar());

        // Assert Interactions
        verify(mockNewAvatar).setAvatarBrain(avatarBrain);
    }

    @Test
    void setAvatar_shouldTransferAvatarAndManageLinks_whenChangingAvatars() {
        // Arrange: Initially set to mockAvatar
        avatarBrain.setAvatar(mockAvatar);
        when(mockAvatar.getAvatarBrain()).thenReturn(avatarBrain); // Old avatar points here
        reset(mockAvatar);
        // Arrange: New avatar points elsewhere initially (or null)
        when(mockNewAvatar.getAvatarBrain()).thenReturn(mockOtherAvatarBrain); // Points to other brain

        // Act: Set to mockNewAvatar
        boolean wasSet = avatarBrain.setAvatar(mockNewAvatar);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockNewAvatar, avatarBrain.getAvatar());

        // Assert Interactions
        verify(mockNewAvatar).setAvatarBrain(avatarBrain); // New avatar should point here
        // Verify the *other* brain had its avatar link nulled by the internal logic
        // verify(mockOtherAvatarBrain).setAvatar(null); // Hard to verify internal field access on mock
        // Verify old avatar wasn't touched unnecessarily
        verify(mockAvatar, never()).setAvatarBrain(any());
    }

     @Test
    void setAvatar_shouldUnsetAvatarAndManageLinks_whenSettingNull() {
        // Arrange: Initially set to mockAvatar
        avatarBrain.setAvatar(mockAvatar);
        when(mockAvatar.getAvatarBrain()).thenReturn(avatarBrain); // Points here
        reset(mockAvatar);

        // Act: Set avatar to null
        boolean wasSet = avatarBrain.setAvatar(null);

        // Assert State
        assertTrue(wasSet);
        assertNull(avatarBrain.getAvatar());
        assertFalse(avatarBrain.hasAvatar());

        // Assert Interactions: Link on old avatar should be broken
        // This happens inside setAvatar when the *new* avatar (null) calls setAvatarBrain(this)
        // which doesn't happen. The logic relies on: anOldAvatarBrain.avatar = null;
        // Let's verify our mockAvatar wasn't unnecessarily called
        verify(mockAvatar, never()).setAvatarBrain(any());
        // We can't easily verify the old link was broken on the *other* side without more setup or a different approach
    }

    @Test
    void setAvatar_shouldDoNothing_whenSettingSameAvatar() {
        // Arrange: Initially set to mockAvatar
        avatarBrain.setAvatar(mockAvatar);
        reset(mockAvatar);

        // Act
        boolean wasSet = avatarBrain.setAvatar(mockAvatar);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockAvatar, avatarBrain.getAvatar());

    }


    // --- Motor Association (@OneToMany, mappedBy="avatarBrain") ---

    @Test
    void addMotor_shouldAddMotorAndSetBidirectionalLink_whenMotorIsNew() {
        // Arrange: Motor initially belongs to null
        when(mockMotor1.getAvatarBrain()).thenReturn(null);

        // Act
        avatarBrain.addMotor(mockMotor1);

        // Assert State
        assertEquals(1, avatarBrain.numberOfMotors());
        assertTrue(avatarBrain.getMotors().contains(mockMotor1));

        // Assert Interactions (verify link was set on motor)
        verify(mockMotor1).setAvatarBrain(avatarBrain);
    }

     @Test
    void addMotor_shouldAddMotorAndSetBidirectionalLink_whenTransferring() {
        // Arrange: Motor belongs to another brain
        when(mockMotor1.getAvatarBrain()).thenReturn(mockOtherAvatarBrain);

        // Act
        avatarBrain.addMotor(mockMotor1);

        // Assert State
        assertEquals(1, avatarBrain.numberOfMotors());
        assertTrue(avatarBrain.getMotors().contains(mockMotor1));

        // Assert Interactions (verify link was set on motor)
        verify(mockMotor1).setAvatarBrain(avatarBrain);
    }

    @Test
    void addMotor_shouldNotAddDuplicateMotor() {
        // Arrange: Add the motor once
        when(mockMotor1.getAvatarBrain()).thenReturn(null);
        avatarBrain.addMotor(mockMotor1);
        assertEquals(1, avatarBrain.numberOfMotors());
        reset(mockMotor1); // Reset interactions after first add

        // Act: Try adding the same motor again
        avatarBrain.addMotor(mockMotor1);

        // Assert State didn't change
        assertEquals(1, avatarBrain.numberOfMotors());

        // Assert Interactions (setAvatarBrain should not be called again)
        verify(mockMotor1, never()).setAvatarBrain(any());
    }
    
    @Test
void removeMotor_shouldRemoveMotorAndBreakBidirectionalLink_whenMotorPointsHere() {
    // Arrange: Add motor, make it point HERE
    when(mockMotor1.getAvatarBrain()).thenReturn(null); // Motor starts unlinked
    avatarBrain.addMotor(mockMotor1);                   // Add it. Inside, setAvatarBrain(this) is called on mockMotor1
    assertEquals(1, avatarBrain.numberOfMotors());      // Should be 1

    // Reset mock AFTER the add operation where the link was initially set
    reset(mockMotor1);

    // Re-stub: Now, when removeMotor asks, make it return the correct brain
    when(mockMotor1.getAvatarBrain()).thenReturn(avatarBrain); // Point HERE for the check

    // Act
    avatarBrain.removeMotor(mockMotor1); // Call remove

    // Assert State
    assertEquals(0, avatarBrain.numberOfMotors());          // Should be 0
    assertFalse(avatarBrain.getMotors().contains(mockMotor1)); // Should be false

    // Assert Interactions
    verify(mockMotor1).getAvatarBrain();                    // Verify the check was made inside removeMotor
    verify(mockMotor1).setAvatarBrain(null);                // **THIS IS FAILING (Wanted but not invoked)**
}

    @Test
    void removeMotor_shouldRemoveMotorOnly_whenMotorPointsElsewhere() {
        // Arrange: Add motor, make it point elsewhere
        when(mockMotor1.getAvatarBrain()).thenReturn(null);
        avatarBrain.addMotor(mockMotor1);
        assertEquals(1, avatarBrain.numberOfMotors());
        when(mockMotor1.getAvatarBrain()).thenReturn(mockOtherAvatarBrain); // Point ELSEWHERE
        reset(mockMotor1); // Reset interactions

        // Act
        avatarBrain.removeMotor(mockMotor1);

        // Assert State
        assertEquals(0, avatarBrain.numberOfMotors());
        assertFalse(avatarBrain.getMotors().contains(mockMotor1));

        // Assert Interactions (setAvatarBrain(null) should NOT have been called)
        verify(mockMotor1, never()).setAvatarBrain(null);
    }

     @Test
    void removeMotor_shouldDoNothingIfMotorNotPresent() {
         // Arrange: Motor list is empty

        // Act
        avatarBrain.removeMotor(mockMotor1);

        // Assert State
        assertEquals(0, avatarBrain.numberOfMotors());

        // Assert Interactions
        verify(mockMotor1, never()).setAvatarBrain(any());
    }

    // Note: equals() and hashCode() tests assumed standard ID checks
}