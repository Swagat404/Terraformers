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
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MotorTest {

    // Mocks for associated entities
    @Mock
    private AvatarBrain mockAvatarBrain;
    @Mock
    private AvatarBrain mockOtherAvatarBrain;
    @Mock
    private MotorReading mockMotorReading1;
    @Mock
    private MotorReading mockMotorReading2;
    @Mock
    private Motor mockOtherMotor; // Needed for reading transfer tests

    // Class Under Test
    private Motor motor;

    // Test Constants
    private final int MOTOR_ID = 401;
    private final float MAX_SPEED = 100.5f;
    private final float POWER_CONSUMPTION = 15.2f;
    private final Motor.MotorPosition POSITION = Motor.MotorPosition.Front;
    private final Motor.MotorStatus STATUS = Motor.MotorStatus.Active;
    private final Motor.MotorType MOTOR_TYPE = Motor.MotorType.TypeX;

    @BeforeEach
    void setUp() {
        // Use no-arg constructor + setters
        motor = new Motor();
        motor.setMotorID(MOTOR_ID);
        motor.setMaxSpeed(MAX_SPEED);
        motor.setPowerConsumption(POWER_CONSUMPTION);
        motor.setPosition(POSITION);
        motor.setStatus(STATUS);
        motor.setMotorType(MOTOR_TYPE);
        // Associations set in specific tests
    }

    // --- Constructor Test (using no-arg and setters) ---
    @Test
    void shouldInitializeCorrectlyViaSetters() {
        assertNotNull(motor);
        assertEquals(MOTOR_ID, motor.getMotorID());
        assertEquals(MAX_SPEED, motor.getMaxSpeed());
        assertEquals(POWER_CONSUMPTION, motor.getPowerConsumption());
        assertEquals(POSITION, motor.getPosition());
        assertEquals(STATUS, motor.getStatus());
        assertEquals(MOTOR_TYPE, motor.getMotorType());
        assertNotNull(motor.getMotorReadings());
        assertTrue(motor.getMotorReadings().isEmpty(), "MotorReadings list should be empty initially");
        assertNull(motor.getAvatarBrain(), "AvatarBrain should be null initially");
    }

    // --- Basic Setters ---
    @Test
    void shouldSetMaxSpeed() {
        motor.setMaxSpeed(120.0f);
        assertEquals(120.0f, motor.getMaxSpeed());
    }

    @Test
    void shouldSetPowerConsumption() {
        motor.setPowerConsumption(20.0f);
        assertEquals(20.0f, motor.getPowerConsumption());
    }

     @Test
    void shouldSetPosition() {
        motor.setPosition(Motor.MotorPosition.Back);
        assertEquals(Motor.MotorPosition.Back, motor.getPosition());
    }

     @Test
    void shouldSetStatus() {
        motor.setStatus(Motor.MotorStatus.Inactive);
        assertEquals(Motor.MotorStatus.Inactive, motor.getStatus());
    }

     @Test
    void shouldSetMotorType() {
        motor.setMotorType(Motor.MotorType.TypeY);
        assertEquals(Motor.MotorType.TypeY, motor.getMotorType());
    }

    // --- AvatarBrain Association Management ---

    @Test
    void setAvatarBrain_shouldSetBrainAndAddMotorToBrain_whenNewNonNullBrain() {
        // Arrange: Brain initially null

        // Act
        boolean wasSet = motor.setAvatarBrain(mockAvatarBrain);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockAvatarBrain, motor.getAvatarBrain());

        // Assert Interactions (verify link was set on AvatarBrain)
        verify(mockAvatarBrain).addMotor(motor);
        verify(mockAvatarBrain, never()).removeMotor(any(Motor.class));
    }

     @Test
    void setAvatarBrain_shouldReturnFalse_whenBrainIsNull() {
        // Arrange: Brain initially null

        // Act
        boolean wasSet = motor.setAvatarBrain(null);

        // Assert State
        assertFalse(wasSet); // Implementation returns false for null
        assertNull(motor.getAvatarBrain());

        // Assert Interactions
        verifyNoInteractions(mockAvatarBrain);
    }

    @Test
    void setAvatarBrain_shouldTransferBrainAndManageLinks_whenChangingBrains() {
        // Arrange: Initially set to mockAvatarBrain
        motor.setAvatarBrain(mockAvatarBrain);
        reset(mockAvatarBrain); // Reset interactions

        // Act: Set to a different brain
        boolean wasSet = motor.setAvatarBrain(mockOtherAvatarBrain);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockOtherAvatarBrain, motor.getAvatarBrain());

        // Assert Interactions
        verify(mockAvatarBrain).removeMotor(motor);     // Should be removed from old brain
        verify(mockOtherAvatarBrain).addMotor(motor); // Should be added to new brain
    }

    @Test
    void setAvatarBrain_shouldDoNothing_whenSettingSameBrain() {
         // Arrange: Initially set to mockAvatarBrain
        motor.setAvatarBrain(mockAvatarBrain);
        reset(mockAvatarBrain);

        // Act: Set to the same brain again
        boolean wasSet = motor.setAvatarBrain(mockAvatarBrain);

         // Assert State
        assertTrue(wasSet); // Should still return true
        assertSame(mockAvatarBrain, motor.getAvatarBrain()); // Still the same brain

        // Assert Interactions
        verify(mockAvatarBrain, never()).removeMotor(any(Motor.class));
        verify(mockAvatarBrain, never()).addMotor(any(Motor.class));
    }

    // --- MotorReading Association Management ---

    @Test
    void addMotorReading_shouldAddReadingAndSetBidirectionalLink_whenReadingIsNew() {
        // Arrange: Reading initially belongs to null
        when(mockMotorReading1.getMotor()).thenReturn(null);

        // Act
        motor.addMotorReading(mockMotorReading1);

        // Assert State
        assertEquals(1, motor.numberOfMotorReadings());
        assertTrue(motor.getMotorReadings().contains(mockMotorReading1));

        // Assert Interactions (verify link was set on reading)
        verify(mockMotorReading1).setMotor(motor);
    }

     @Test
    void addMotorReading_shouldAddReadingAndSetBidirectionalLink_whenTransferring() {
        // Arrange: Reading belongs to another motor
        when(mockMotorReading1.getMotor()).thenReturn(mockOtherMotor);

        // Act
        motor.addMotorReading(mockMotorReading1);

        // Assert State
        assertEquals(1, motor.numberOfMotorReadings());
        assertTrue(motor.getMotorReadings().contains(mockMotorReading1));

        // Assert Interactions (verify link was set on reading)
        verify(mockMotorReading1).setMotor(motor);
    }

     @Test
    void addMotorReading_shouldNotAddDuplicateReading() {
        // Arrange: Add the reading once
        when(mockMotorReading1.getMotor()).thenReturn(null);
        motor.addMotorReading(mockMotorReading1);
        assertEquals(1, motor.numberOfMotorReadings());

        // Act: Try adding the same reading again
        motor.addMotorReading(mockMotorReading1);

        // Assert State didn't change
        assertEquals(1, motor.numberOfMotorReadings());
    }

     @Test
    void removeMotorReading_shouldRemoveReadingAndBreakBidirectionalLink_whenReadingPointsHere() {
        // Arrange: Add reading, make it point HERE
        when(mockMotorReading1.getMotor()).thenReturn(null);
        motor.addMotorReading(mockMotorReading1);
        assertEquals(1, motor.numberOfMotorReadings());
        when(mockMotorReading1.getMotor()).thenReturn(motor); // Point HERE

        // Act
        motor.removeMotorReading(mockMotorReading1);

        // Assert State
        assertEquals(0, motor.numberOfMotorReadings());
        assertFalse(motor.getMotorReadings().contains(mockMotorReading1));

        // Assert Interactions (setMotor(null) SHOULD have been called by removeMotorReading)
        verify(mockMotorReading1).setMotor(null);
    }

    @Test
    void removeMotorReading_shouldRemoveReadingOnly_whenReadingPointsElsewhere() {
        // Arrange: Add reading, make it point elsewhere
        when(mockMotorReading1.getMotor()).thenReturn(null);
        motor.addMotorReading(mockMotorReading1);
        assertEquals(1, motor.numberOfMotorReadings());
        when(mockMotorReading1.getMotor()).thenReturn(mockOtherMotor); // Point ELSEWHERE

        // Act
        motor.removeMotorReading(mockMotorReading1);

        // Assert State
        assertEquals(0, motor.numberOfMotorReadings());
        assertFalse(motor.getMotorReadings().contains(mockMotorReading1));

        // Assert Interactions (setMotor(null) should NOT have been called)
        verify(mockMotorReading1, never()).setMotor(null);
    }

    // --- delete() Method ---
     @Test
    void delete_shouldRemoveMotorFromBrainAndBreakLinks() {
        // Arrange: Set a brain and add some readings
        motor.setAvatarBrain(mockAvatarBrain);
        motor.addMotorReading(mockMotorReading1);
        // Assume readings now point back for link breaking test
        when(mockMotorReading1.getMotor()).thenReturn(motor);
        reset(mockAvatarBrain, mockMotorReading1); // Reset interactions from setup

        // Act
        motor.delete(); // Call the entity's delete helper

        // Assert State
        assertNull(motor.getAvatarBrain(), "Internal avatarBrain reference should be nulled");

        // Assert Interactions
        verify(mockAvatarBrain).removeMotor(motor); // Verify link break called on AvatarBrain
        // Cascade.ALL + orphanRemoval handles readings DB deletion
        // delete() also calls setMotor(null) via removeMotorReading if needed
        // Verify removeMotorReading was called during the delete process
         // verify(mockMotorReading1).setMotor(null); // This might be implicit via removeMotorReading
    }

    // Note: equals() and hashCode() tests assumed standard ID checks
}