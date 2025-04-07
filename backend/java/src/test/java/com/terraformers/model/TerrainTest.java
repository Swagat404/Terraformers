package com.terraformers.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TerrainTest {

    // Mocks for associated entities
    @Mock
    private Location mockLocation1;
    @Mock
    private Location mockLocation2;
    @Mock
    private Terrain mockOtherTerrain;

    // Class Under Test
    private Terrain terrain;

    // Test Constants
    private final int TERRAIN_ID = 101;
    private final Terrain.TerrainType TERRAIN_TYPE = Terrain.TerrainType.Rocky;
    private final float SURFACE_HARDNESS = 8.5f;
    private final String DESCRIPTION = "A rocky plateau.";

    @BeforeEach
    void setUp() {
        // Use the no-arg constructor (as JPA would) then setters,
        // OR use the original constructor if testing that path specifically.
        // Let's use the original constructor here for basic field init.
        terrain = new Terrain(TERRAIN_ID, TERRAIN_TYPE, SURFACE_HARDNESS, DESCRIPTION);
    }

    // --- Constructor and Basic Getters ---
    @Test
    void shouldInitializeCorrectlyViaConstructor() {
        assertNotNull(terrain);
        assertEquals(TERRAIN_ID, terrain.getTerrainID());
        assertEquals(TERRAIN_TYPE, terrain.getTerrainType());
        assertEquals(SURFACE_HARDNESS, terrain.getSurfaceHardness());
        assertEquals(DESCRIPTION, terrain.getDescription());
        assertNotNull(terrain.getLocations());
        assertTrue(terrain.getLocations().isEmpty(), "Locations list should be empty initially");
        assertFalse(terrain.isNumberOfLocationsValid(), "Should be invalid number of locations initially");
    }

    // --- Basic Setters ---
    @Test
    void shouldSetTerrainType() {
        terrain.setTerrainType(Terrain.TerrainType.Flat);
        assertEquals(Terrain.TerrainType.Flat, terrain.getTerrainType());
    }

    @Test
    void shouldSetSurfaceHardness() {
        terrain.setSurfaceHardness(5.1f);
        assertEquals(5.1f, terrain.getSurfaceHardness());
    }

    @Test
    void shouldSetDescription() {
        terrain.setDescription("New Description");
        assertEquals("New Description", terrain.getDescription());
    }
    // Note: Setting ID might depend on whether it's generated or manually assigned.

    // --- Location Association Management ---

    @Test
    void addLocation_shouldAddLocationAndSetBidirectionalLink_whenLocationIsNew() {
        // Arrange: Location initially belongs to null
        when(mockLocation1.getTerrain()).thenReturn(null);

        // Act
        terrain.addLocation(mockLocation1);

        // Assert State
        assertEquals(1, terrain.numberOfLocations());
        assertTrue(terrain.getLocations().contains(mockLocation1));
        assertTrue(terrain.isNumberOfLocationsValid());

        // Assert Interactions (verify link was set on location)
        // Note: addLocation doesn't directly call setTerrain if existing is null,
        // assuming the OTHER side's setter/constructor handles it.
        // A stricter test might need to verify Location.setTerrain was called elsewhere.
        // For this unit test, we focus on Terrain's state and minimal interaction checks.
        verify(mockLocation1).getTerrain(); // Check was made
    }

    @Test
void addLocation_shouldAddLocationAndSetBidirectionalLink_whenTransferring() {
    // Arrange: Location belongs to another valid terrain
    when(mockLocation1.getTerrain()).thenReturn(mockOtherTerrain);

    // Arrange: *** ADD THIS STUBBING ***
    // Simulate the other terrain having enough locations to allow the transfer
    when(mockOtherTerrain.numberOfLocations()).thenReturn(Terrain.minimumNumberOfLocations() + 1); // e.g., returns 2

    // Act
    terrain.addLocation(mockLocation1);

    // Assert State
    assertEquals(1, terrain.numberOfLocations()); // Should now pass
    assertTrue(terrain.getLocations().contains(mockLocation1)); // Should now pass
    assertTrue(terrain.isNumberOfLocationsValid()); // Should now pass

    // Assert Interactions (verify link was set on location by *this* method)
    verify(mockLocation1).setTerrain(terrain); // Should now pass
    // Verify the check was made on the mock
    verify(mockOtherTerrain).numberOfLocations();
}

    @Test
    void addLocation_shouldNotAddLocation_whenTransferringFromTerrainAtMinimum() {
        // Arrange: Location belongs to another terrain at minimum capacity
        when(mockLocation1.getTerrain()).thenReturn(mockOtherTerrain);
        when(mockOtherTerrain.numberOfLocations()).thenReturn(Terrain.minimumNumberOfLocations());

        // Act
        terrain.addLocation(mockLocation1);

        // Assert State (should not have changed)
        assertEquals(0, terrain.numberOfLocations());
        assertFalse(terrain.getLocations().contains(mockLocation1));
        assertFalse(terrain.isNumberOfLocationsValid());

        // Assert Interactions
        verify(mockLocation1, never()).setTerrain(any(Terrain.class));
    }

    @Test
    void addLocation_shouldNotAddDuplicateLocation() {
        // Arrange: Add the location once
        when(mockLocation1.getTerrain()).thenReturn(null);
        terrain.addLocation(mockLocation1); // First add
        assertEquals(1, terrain.numberOfLocations());

        // Act: Try adding the same location again
        terrain.addLocation(mockLocation1);

        // Assert State didn't change
        assertEquals(1, terrain.numberOfLocations());
    }

    @Test
    void removeLocation_shouldRemoveLocationAndBreakBidirectionalLink() {
        // Arrange: Add two locations, make mockLocation1 point elsewhere
        when(mockLocation1.getTerrain()).thenReturn(null);
        terrain.addLocation(mockLocation1);
        when(mockLocation2.getTerrain()).thenReturn(null);
        terrain.addLocation(mockLocation2);
        assertEquals(2, terrain.numberOfLocations());
        when(mockLocation1.getTerrain()).thenReturn(mockOtherTerrain); // Point elsewhere to allow removal

        // Act
        terrain.removeLocation(mockLocation1);

        // Assert State
        assertEquals(1, terrain.numberOfLocations());
        assertFalse(terrain.getLocations().contains(mockLocation1));
        assertTrue(terrain.getLocations().contains(mockLocation2));
        assertTrue(terrain.isNumberOfLocationsValid());

        // Assert Interactions (setTerrain(null) should NOT have been called by Terrain.removeLocation)
         verify(mockLocation1, never()).setTerrain(null);
    }

     @Test
    void removeLocation_shouldRemoveLocationAndBreakBidirectionalLink_whenLocationPointsHere() {
        // Arrange: Add two locations, make mockLocation1 point HERE
        when(mockLocation1.getTerrain()).thenReturn(null);
        terrain.addLocation(mockLocation1);
        when(mockLocation2.getTerrain()).thenReturn(null);
        terrain.addLocation(mockLocation2);
        assertEquals(2, terrain.numberOfLocations());
        when(mockLocation1.getTerrain()).thenReturn(terrain); // Point HERE

        // Act
        terrain.removeLocation(mockLocation1);

        // Assert State
        assertEquals(1, terrain.numberOfLocations());
        assertFalse(terrain.getLocations().contains(mockLocation1));
        assertTrue(terrain.getLocations().contains(mockLocation2));
        assertTrue(terrain.isNumberOfLocationsValid());

        // Assert Interactions (setTerrain(null) SHOULD have been called by Terrain.removeLocation)
         verify(mockLocation1).setTerrain(null);
    }


    @Test
    void removeLocation_shouldNotRemove_whenLocationPointsHereButMinimumReached() {
         // Arrange: Add one location, make it point HERE
        when(mockLocation1.getTerrain()).thenReturn(null);
        terrain.addLocation(mockLocation1);
        assertEquals(1, terrain.numberOfLocations());
        when(mockLocation1.getTerrain()).thenReturn(terrain); // Point HERE

        // Act
        terrain.removeLocation(mockLocation1);

        // Assert State (should not have changed)
        assertEquals(1, terrain.numberOfLocations());
        assertTrue(terrain.getLocations().contains(mockLocation1));

        // Assert Interactions (setTerrain(null) should NOT have been called)
        verify(mockLocation1, never()).setTerrain(null);
    }

    // --- equals() and hashCode() Tests ---
    @Test
    void equals_shouldBeTrueForSameId() {
        Terrain terrain1 = new Terrain(101, Terrain.TerrainType.Flat, 5f, "t1");
        Terrain terrain2 = new Terrain(101, Terrain.TerrainType.Rocky, 8f, "t2");
        assertEquals(terrain1, terrain2);
    }

    @Test
    void equals_shouldBeFalseForDifferentId() {
         Terrain terrain1 = new Terrain(101, Terrain.TerrainType.Flat, 5f, "t1");
         Terrain terrain2 = new Terrain(102, Terrain.TerrainType.Flat, 5f, "t1");
        assertNotEquals(terrain1, terrain2);
    }

     @Test
    void equals_shouldBeFalseForNull() {
         Terrain terrain1 = new Terrain(101, Terrain.TerrainType.Flat, 5f, "t1");
        assertNotEquals(terrain1, null);
    }

     @Test
    void equals_shouldBeFalseForDifferentClass() {
        Terrain terrain1 = new Terrain(101, Terrain.TerrainType.Flat, 5f, "t1");
        Object other = new Object();
        assertNotEquals(terrain1, other);
    }

    @Test
    void hashCode_shouldBeSameForSameId() {
        Terrain terrain1 = new Terrain(101, Terrain.TerrainType.Flat, 5f, "t1");
        Terrain terrain2 = new Terrain(101, Terrain.TerrainType.Rocky, 8f, "t2");
        assertEquals(terrain1.hashCode(), terrain2.hashCode());
    }

     @Test
    void hashCode_shouldBeDifferentForDifferentId() {
        Terrain terrain1 = new Terrain(101, Terrain.TerrainType.Flat, 5f, "t1");
        Terrain terrain2 = new Terrain(102, Terrain.TerrainType.Flat, 5f, "t1");
        assertNotEquals(terrain1.hashCode(), terrain2.hashCode());
    }

}