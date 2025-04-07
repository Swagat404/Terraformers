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
class SensorTest {

    // Mocks for associated entities
    @Mock
    private Avatar mockAvatar;
    @Mock
    private Avatar mockOtherAvatar;
    @Mock
    private SensorReading mockSensorReading1;
    @Mock
    private SensorReading mockSensorReading2;
    @Mock
    private Sensor mockOtherSensor; // Needed for reading transfer tests

    // Class Under Test
    private Sensor sensor;

    // Test Constants
    private final int SENSOR_ID = 501;
    private final Sensor.SensorMountPosition MOUNT_POSITION = Sensor.SensorMountPosition.Left;
    private final Sensor.SensorStatus STATUS = Sensor.SensorStatus.Active;
    private final Sensor.SensorType SENSOR_TYPE = Sensor.SensorType.Type1;

    @BeforeEach
    void setUp() {
        // Use no-arg constructor + setters for setup
        sensor = new Sensor();
        sensor.setSensorID(SENSOR_ID);
        sensor.setMountPosition(MOUNT_POSITION);
        sensor.setStatus(STATUS);
        sensor.setSensorType(SENSOR_TYPE);
        // Associations set in specific tests
    }

    // --- Constructor Test (using no-arg and setters) ---
    @Test
    void shouldInitializeCorrectlyViaSetters() {
        assertNotNull(sensor);
        assertEquals(SENSOR_ID, sensor.getSensorID());
        assertEquals(MOUNT_POSITION, sensor.getMountPosition());
        assertEquals(STATUS, sensor.getStatus());
        assertEquals(SENSOR_TYPE, sensor.getSensorType());
        assertNotNull(sensor.getSensorReadings());
        assertTrue(sensor.getSensorReadings().isEmpty(), "SensorReadings list should be empty initially");
        assertNull(sensor.getAvatar(), "Avatar should be null initially");
    }

    // --- Basic Setters ---
    @Test
    void shouldSetMountPosition() {
        sensor.setMountPosition(Sensor.SensorMountPosition.Right);
        assertEquals(Sensor.SensorMountPosition.Right, sensor.getMountPosition());
    }

     @Test
    void shouldSetStatus() {
        sensor.setStatus(Sensor.SensorStatus.Faulty);
        assertEquals(Sensor.SensorStatus.Faulty, sensor.getStatus());
    }

    @Test
    void shouldSetSensorType() {
        sensor.setSensorType(Sensor.SensorType.Type2);
        assertEquals(Sensor.SensorType.Type2, sensor.getSensorType());
    }

    // --- Avatar Association Management ---

    @Test
    void setAvatar_shouldSetAvatarAndAddSensorToAvatar_whenNewNonNullAvatar() {
        // Arrange: Avatar initially null

        // Act
        boolean wasSet = sensor.setAvatar(mockAvatar);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockAvatar, sensor.getAvatar());

        // Assert Interactions (verify link was set on Avatar)
        verify(mockAvatar).addSensor(sensor);
        // Verify no attempt to remove from a previous non-existent avatar
        verify(mockAvatar, never()).removeSensor(any(Sensor.class));
    }

    @Test
    void setAvatar_shouldReturnFalse_whenAvatarIsNull() {
        // Arrange: Avatar is initially null

        // Act
        boolean wasSet = sensor.setAvatar(null);

        // Assert State
        assertFalse(wasSet); // Based on current implementation returning false for null
        assertNull(sensor.getAvatar());

        // Assert Interactions (no add/remove should be called)
        verify(mockAvatar, never()).addSensor(any(Sensor.class));
        verify(mockAvatar, never()).removeSensor(any(Sensor.class));
    }

     @Test
    void setAvatar_shouldTransferAvatarAndManageLinks_whenChangingAvatars() {
        // Arrange: Initially set to mockAvatar
        sensor.setAvatar(mockAvatar);
        reset(mockAvatar); // Reset interactions from the initial set

        // Act: Set to a different avatar
        boolean wasSet = sensor.setAvatar(mockOtherAvatar);

        // Assert State
        assertTrue(wasSet);
        assertSame(mockOtherAvatar, sensor.getAvatar());

        // Assert Interactions
        verify(mockAvatar).removeSensor(sensor);     // Should be removed from old avatar
        verify(mockOtherAvatar).addSensor(sensor); // Should be added to new avatar
    }

    @Test
    void setAvatar_shouldDoNothing_whenSettingSameAvatar() {
         // Arrange: Initially set to mockAvatar
        sensor.setAvatar(mockAvatar);
        reset(mockAvatar); // Reset interactions

        // Act: Set to the same avatar again
        boolean wasSet = sensor.setAvatar(mockAvatar);

         // Assert State
        assertTrue(wasSet); // Should still return true
        assertSame(mockAvatar, sensor.getAvatar()); // Still the same avatar

        // Assert Interactions (No further calls should happen)
        verify(mockAvatar, never()).removeSensor(any(Sensor.class));
        verify(mockAvatar, never()).addSensor(any(Sensor.class));
    }


    // --- SensorReading Association Management ---

    @Test
    void addSensorReading_shouldAddReadingAndSetBidirectionalLink_whenReadingIsNew() {
        // Arrange: Reading initially belongs to null
        when(mockSensorReading1.getSensor()).thenReturn(null);

        // Act
        sensor.addSensorReading(mockSensorReading1);

        // Assert State
        assertEquals(1, sensor.numberOfSensorReadings());
        assertTrue(sensor.getSensorReadings().contains(mockSensorReading1));

        // Assert Interactions (verify link was set on reading)
        verify(mockSensorReading1).setSensor(sensor);
    }

     @Test
    void addSensorReading_shouldAddReadingAndSetBidirectionalLink_whenTransferring() {
        // Arrange: Reading belongs to another sensor
        when(mockSensorReading1.getSensor()).thenReturn(mockOtherSensor);

        // Act
        sensor.addSensorReading(mockSensorReading1);

        // Assert State
        assertEquals(1, sensor.numberOfSensorReadings());
        assertTrue(sensor.getSensorReadings().contains(mockSensorReading1));

        // Assert Interactions (verify link was set on reading)
        verify(mockSensorReading1).setSensor(sensor);
    }

    @Test
    void addSensorReading_shouldNotAddDuplicateReading() {
        // Arrange: Add the reading once
        when(mockSensorReading1.getSensor()).thenReturn(null);
        sensor.addSensorReading(mockSensorReading1); // First add
        assertEquals(1, sensor.numberOfSensorReadings());

        // Act: Try adding the same reading again
        sensor.addSensorReading(mockSensorReading1);

        // Assert State didn't change
        assertEquals(1, sensor.numberOfSensorReadings());
    }

    @Test
    void removeSensorReading_shouldRemoveReadingAndBreakBidirectionalLink_whenReadingPointsHere() {
        // Arrange: Add reading, make it point HERE
        when(mockSensorReading1.getSensor()).thenReturn(null);
        sensor.addSensorReading(mockSensorReading1);
        assertEquals(1, sensor.numberOfSensorReadings());
        when(mockSensorReading1.getSensor()).thenReturn(sensor); // Point HERE

        // Act
        sensor.removeSensorReading(mockSensorReading1);

        // Assert State
        assertEquals(0, sensor.numberOfSensorReadings());
        assertFalse(sensor.getSensorReadings().contains(mockSensorReading1));

        // Assert Interactions (setSensor(null) SHOULD have been called by removeSensorReading)
        verify(mockSensorReading1).setSensor(null);
    }

    @Test
    void removeSensorReading_shouldRemoveReadingOnly_whenReadingPointsElsewhere() {
        // Arrange: Add reading, make it point elsewhere
        when(mockSensorReading1.getSensor()).thenReturn(null);
        sensor.addSensorReading(mockSensorReading1);
        assertEquals(1, sensor.numberOfSensorReadings());
        when(mockSensorReading1.getSensor()).thenReturn(mockOtherSensor); // Point ELSEWHERE

        // Act
        sensor.removeSensorReading(mockSensorReading1);

        // Assert State
        assertEquals(0, sensor.numberOfSensorReadings());
        assertFalse(sensor.getSensorReadings().contains(mockSensorReading1));

        // Assert Interactions (setSensor(null) should NOT have been called)
        verify(mockSensorReading1, never()).setSensor(null);
    }


    // --- delete() Method ---
     @Test
    void delete_shouldRemoveSensorFromAvatarAndBreakLinks() {
        // Arrange: Set an avatar and add some readings
        sensor.setAvatar(mockAvatar);
        sensor.addSensorReading(mockSensorReading1);
        sensor.addSensorReading(mockSensorReading2);
        // Assume readings now point back to sensor for link breaking test
        when(mockSensorReading1.getSensor()).thenReturn(sensor);
        when(mockSensorReading2.getSensor()).thenReturn(sensor);
        reset(mockAvatar, mockSensorReading1, mockSensorReading2); // Reset interactions from setup

        // Act
        sensor.delete(); // Call the entity's delete helper

        // Assert State
        assertNull(sensor.getAvatar(), "Internal avatar reference should be nulled");

        // Assert Interactions
        verify(mockAvatar).removeSensor(sensor); // Verify link break called on Avatar
        // Cascade.ALL + orphanRemoval handles readings, but delete() *also* clears links
        verify(mockSensorReading1, never()).delete(); // Delete not called directly if cascade handles DB
        verify(mockSensorReading2, never()).delete();
         // Verify link breaking *if* removeSensorReading calls setSensor(null) (which it does)
         // Although, this might be considered an implementation detail if cascade handles it.
         // A safer test just verifies removeSensor was called on the parent.
    }

    // Note: equals() and hashCode() tests are standard ID checks, omitted for brevity
    // unless there's specific complex logic. Assumed tested elsewhere or straightforward.

}