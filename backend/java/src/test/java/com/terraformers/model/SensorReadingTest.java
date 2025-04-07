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
class SensorReadingTest {

    // Mocks for associated entities
    @Mock
    private Sensor mockSensor;
    @Mock
    private Sensor mockOtherSensor;

    // Class Under Test
    private SensorReading sensorReading;

    // Test Constants
    private final int READING_ID = 1001;
    private final LocalDateTime TIMESTAMP = LocalDateTime.now().minusMinutes(10).truncatedTo(ChronoUnit.SECONDS);
    private final float VALUE = 25.5f;

    @BeforeEach
    void setUp() {
        // Use the constructor that sets the association via the JPA-aware setter
        // Need to stub the addSensorReading call made *during* setSensor called by constructor
        // Or, use no-arg constructor and setters in tests. Let's try the latter for variety.

        sensorReading = new SensorReading();
        sensorReading.setReadingID(READING_ID); // Assuming setter exists/works from Reading
        sensorReading.setTimeStamp(TIMESTAMP); // Assuming setter exists/works from Reading
        sensorReading.setValue(VALUE);
        // Association set separately in tests or specific setup methods
    }

    // --- Constructor Test (using no-arg and setters) ---
    @Test
    void shouldInitializeCorrectlyViaSetters() {
        assertNotNull(sensorReading);
        assertEquals(READING_ID, sensorReading.getReadingID());
        assertEquals(TIMESTAMP, sensorReading.getTimeStamp());
        assertEquals(VALUE, sensorReading.getValue());
        assertNull(sensorReading.getSensor(), "Sensor should be null initially");
    }

    // --- Basic Setters ---
    @Test
    void shouldSetValue() {
        sensorReading.setValue(33.3f);
        assertEquals(33.3f, sensorReading.getValue());
    }
    // Note: Tests for inherited setters (ID, Timestamp) would be in ReadingTest.

    // --- Sensor Association Management ---

    @Test
    void setSensor_shouldSetSensorAndAddReadingToSensor_whenNewNonNullSensor() {
        // Arrange: Sensor initially null

        // Act
        boolean wasSet = sensorReading.setSensor(mockSensor);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockSensor, sensorReading.getSensor());

        // Assert Interactions (verify link was set on Sensor)
        verify(mockSensor).addSensorReading(sensorReading);
        // Verify no attempt to remove from a previous non-existent sensor
        verify(mockSensor, never()).removeSensorReading(any(SensorReading.class));
    }

     @Test
    void setSensor_shouldReturnFalse_whenSensorIsNull() {
        // Arrange: Sensor is initially null

        // Act
        boolean wasSet = sensorReading.setSensor(null);

        // Assert State
        assertFalse(wasSet); // Based on current implementation returning false for null
        assertNull(sensorReading.getSensor());

        // Assert Interactions (no add/remove should be called)
        verify(mockSensor, never()).addSensorReading(any(SensorReading.class));
        verify(mockSensor, never()).removeSensorReading(any(SensorReading.class));
    }

    @Test
    void setSensor_shouldTransferSensorAndManageLinks_whenChangingSensors() {
        // Arrange: Initially set to mockSensor
        sensorReading.setSensor(mockSensor);
        // Reset interactions on mockSensor from the initial set
        reset(mockSensor);
        // Ensure the mockSensor acknowledges this reading exists (for remove check)
        // when(mockSensor.getSensorReadings()).thenReturn(Collections.singletonList(sensorReading)); // Or similar setup if needed by removeSensorReading

        // Act: Set to a different sensor
        boolean wasSet = sensorReading.setSensor(mockOtherSensor);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockOtherSensor, sensorReading.getSensor());

        // Assert Interactions
        verify(mockSensor).removeSensorReading(sensorReading);     // Should be removed from old sensor
        verify(mockOtherSensor).addSensorReading(sensorReading); // Should be added to new sensor
    }

    @Test
    void setSensor_shouldDoNothing_whenSettingSameSensor() {
         // Arrange: Initially set to mockSensor
        sensorReading.setSensor(mockSensor);
        reset(mockSensor); // Reset interactions

        // Act: Set to the same sensor again
        boolean wasSet = sensorReading.setSensor(mockSensor);

         // Assert State
        assertTrue(wasSet); // Should still return true
        assertSame(mockSensor, sensorReading.getSensor()); // Still the same sensor

        // Assert Interactions (No further calls should happen)
        verify(mockSensor, never()).removeSensorReading(any(SensorReading.class));
        verify(mockSensor, never()).addSensorReading(any(SensorReading.class));
    }


    // --- delete() Method ---
    // Unit testing delete() is tricky as its main job is DB interaction (via repo).
    // We primarily test that it breaks the link correctly before the repo call happens.
    @Test
    void delete_shouldRemoveReadingFromSensor_whenSensorIsSet() {
        // Arrange: Set a sensor
        sensorReading.setSensor(mockSensor);
        reset(mockSensor); // Reset interactions from setup

        // Act
        sensorReading.delete(); // Call the entity's delete helper

        // Assert State
        assertNull(sensorReading.getSensor(), "Internal sensor reference should be nulled");

        // Assert Interactions
        verify(mockSensor).removeSensorReading(sensorReading); // Verify link break called
    }

    @Test
    void delete_shouldDoNothingWithSensorLink_whenSensorIsNull() {
        // Arrange: Sensor is already null

        // Act
        sensorReading.delete();

        // Assert State
        assertNull(sensorReading.getSensor());

        // Assert Interactions (no mock interaction expected)
        verifyNoInteractions(mockSensor);
        verifyNoInteractions(mockOtherSensor);
    }

    // Note: equals() and hashCode() are inherited from Reading and tested in ReadingTest
}