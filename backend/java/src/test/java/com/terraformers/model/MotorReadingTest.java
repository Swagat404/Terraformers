package com.terraformers.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
class MotorReadingTest {

    // Mocks for associated entities
    @Mock
    private Motor mockMotor;
    @Mock
    private Motor mockOtherMotor;

    // Class Under Test
    private MotorReading motorReading;

    // Test Constants
    private final int READING_ID = 801;
    private final LocalDateTime TIMESTAMP = LocalDateTime.now().minusMinutes(5).truncatedTo(ChronoUnit.SECONDS);
    private final float CURRENT_SPEED = 85.5f;
    private final String DIRECTION = "Forward";
    private final float CURRENT_POWER = 12.1f;

    @BeforeEach
    void setUp() {
        // Use no-arg constructor + setters
        motorReading = new MotorReading();
        motorReading.setReadingID(READING_ID);
        motorReading.setTimeStamp(TIMESTAMP);
        motorReading.setCurrentSpeed(CURRENT_SPEED);
        motorReading.setDirection(DIRECTION);
        motorReading.setCurrentPower(CURRENT_POWER);
        // Association set in specific tests
    }

    // --- Constructor Test (using no-arg and setters) ---
    @Test
    void shouldInitializeCorrectlyViaSetters() {
        assertNotNull(motorReading);
        assertEquals(READING_ID, motorReading.getReadingID());
        assertEquals(TIMESTAMP, motorReading.getTimeStamp());
        assertEquals(CURRENT_SPEED, motorReading.getCurrentSpeed());
        assertEquals(DIRECTION, motorReading.getDirection());
        assertEquals(CURRENT_POWER, motorReading.getCurrentPower());
        assertNull(motorReading.getMotor(), "Motor should be null initially");
    }

    // --- Basic Setters ---
    @Test
    void shouldSetCurrentSpeed() {
        motorReading.setCurrentSpeed(99.0f);
        assertEquals(99.0f, motorReading.getCurrentSpeed());
    }

    @Test
    void shouldSetDirection() {
        motorReading.setDirection("Reverse");
        assertEquals("Reverse", motorReading.getDirection());
    }

    @Test
    void shouldSetCurrentPower() {
        motorReading.setCurrentPower(14.5f);
        assertEquals(14.5f, motorReading.getCurrentPower());
    }
    // Note: Tests for inherited setters (ID, Timestamp) are in ReadingTest.

    // --- Motor Association Management ---

    @Test
    void setMotor_shouldSetMotorAndAddReadingToMotor_whenNewNonNullMotor() {
        // Arrange: Motor initially null

        // Act
        boolean wasSet = motorReading.setMotor(mockMotor);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockMotor, motorReading.getMotor());

        // Assert Interactions (verify link was set on Motor)
        verify(mockMotor).addMotorReading(motorReading);
        // Verify no attempt to remove from a previous non-existent motor
        verify(mockMotor, never()).removeMotorReading(any(MotorReading.class));
    }

     @Test
    void setMotor_shouldReturnFalse_whenMotorIsNull() {
        // Arrange: Motor is initially null

        // Act
        boolean wasSet = motorReading.setMotor(null);

        // Assert State
        assertFalse(wasSet); // Based on current implementation returning false for null
        assertNull(motorReading.getMotor());

        // Assert Interactions (no add/remove should be called)
        verify(mockMotor, never()).addMotorReading(any(MotorReading.class));
        verify(mockMotor, never()).removeMotorReading(any(MotorReading.class));
    }

    @Test
    void setMotor_shouldTransferMotorAndManageLinks_whenChangingMotors() {
        // Arrange: Initially set to mockMotor
        motorReading.setMotor(mockMotor);
        reset(mockMotor); // Reset interactions from the initial set

        // Act: Set to a different motor
        boolean wasSet = motorReading.setMotor(mockOtherMotor);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockOtherMotor, motorReading.getMotor());

        // Assert Interactions
        verify(mockMotor).removeMotorReading(motorReading);     // Should be removed from old motor
        verify(mockOtherMotor).addMotorReading(motorReading); // Should be added to new motor
    }

    @Test
    void setMotor_shouldDoNothing_whenSettingSameMotor() {
         // Arrange: Initially set to mockMotor
        motorReading.setMotor(mockMotor);
        reset(mockMotor); // Reset interactions

        // Act: Set to the same motor again
        boolean wasSet = motorReading.setMotor(mockMotor);

         // Assert State
        assertTrue(wasSet); // Should still return true
        assertSame(mockMotor, motorReading.getMotor()); // Still the same motor

        // Assert Interactions (No further calls should happen)
        verify(mockMotor, never()).removeMotorReading(any(MotorReading.class));
        verify(mockMotor, never()).addMotorReading(any(MotorReading.class));
    }


    // --- delete() Method ---
    @Test
    void delete_shouldRemoveReadingFromMotor_whenMotorIsSet() {
        // Arrange: Set a motor
        motorReading.setMotor(mockMotor);
        reset(mockMotor); // Reset interactions from setup

        // Act
        motorReading.delete(); // Call the entity's delete helper

        // Assert State
        assertNull(motorReading.getMotor(), "Internal motor reference should be nulled");

        // Assert Interactions
        verify(mockMotor).removeMotorReading(motorReading); // Verify link break called
    }

    @Test
    void delete_shouldDoNothingWithMotorLink_whenMotorIsNull() {
        // Arrange: Motor is already null

        // Act
        motorReading.delete();

        // Assert State
        assertNull(motorReading.getMotor());

        // Assert Interactions (no mock interaction expected)
        verifyNoInteractions(mockMotor);
        verifyNoInteractions(mockOtherMotor);
    }

    // Note: equals() and hashCode() are inherited from Reading and assumed tested in ReadingTest.
}