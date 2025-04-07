package com.terraformers.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// No Mockito needed as there are no dependencies or mocked interactions

// Test class for the abstract Reading class
// We need a concrete subclass implementation for testing purposes
class ReadingTest {

    // Concrete subclass for testing purposes
    // Needs to be static or in a separate file for JUnit to work easily with it
    static class ConcreteReading extends Reading {
        public ConcreteReading() {
            super();
        }

        public ConcreteReading(int aReadingID, LocalDateTime aTimeStamp) {
            super(aReadingID, aTimeStamp);
        }

        // Need to implement equals/hashCode based on superclass if not already sufficient
        // Inherited equals/hashCode from Reading should be fine

        // Optionally override toString if needed for specific tests
    }

    // Class Under Test (using the concrete implementation)
    private ConcreteReading reading;
    private ConcreteReading readingWithIdZero;

    // Test Constants
    private final int INITIAL_READING_ID = 901;
    private final LocalDateTime INITIAL_TIMESTAMP = LocalDateTime.now().minusHours(1).truncatedTo(ChronoUnit.SECONDS);

    @BeforeEach
    void setUp() {
        // Create instances using the concrete subclass
        reading = new ConcreteReading(INITIAL_READING_ID, INITIAL_TIMESTAMP);
        readingWithIdZero = new ConcreteReading(0, INITIAL_TIMESTAMP.minusDays(1)); // ID is 0
    }

    // --- Constructor Test ---
    @Test
    void shouldInitializeCorrectlyViaConstructor() {
        ConcreteReading testReading = new ConcreteReading(123, INITIAL_TIMESTAMP.plusHours(1));
        assertNotNull(testReading);
        assertEquals(123, testReading.getReadingID());
        assertEquals(INITIAL_TIMESTAMP.plusHours(1), testReading.getTimeStamp());
    }

    @Test
    void shouldInitializeCorrectlyViaNoArgConstructor() {
         ConcreteReading testReading = new ConcreteReading();
         assertNotNull(testReading);
         assertEquals(0, testReading.getReadingID()); // Default int value
         assertNull(testReading.getTimeStamp()); // Default object value
    }


    // --- Setter/Getter Tests ---
    @Test
    void shouldSetAndGetReadingID() {
        int newId = 902;
        boolean wasSet = reading.setReadingID(newId);
        assertTrue(wasSet);
        assertEquals(newId, reading.getReadingID());
    }

    @Test
    void shouldSetAndGetTimeStamp() {
        LocalDateTime newTimeStamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        if (newTimeStamp.equals(INITIAL_TIMESTAMP)) { // Ensure difference
            newTimeStamp = newTimeStamp.plusSeconds(1);
        }
        boolean wasSet = reading.setTimeStamp(newTimeStamp);
        assertTrue(wasSet);
        assertEquals(newTimeStamp, reading.getTimeStamp());
    }

    // --- Delete Test ---
    @Test
    void delete_shouldNotThrowException() {
        // The delete method is empty, verify no exceptions occur.
        assertDoesNotThrow(() -> reading.delete());
    }

    // --- equals() and hashCode() Tests ---

    @Test
    void equals_shouldBeTrueForSameIdAndClass() {
        ConcreteReading reading1 = new ConcreteReading(101, LocalDateTime.now());
        ConcreteReading reading2 = new ConcreteReading(101, LocalDateTime.now().minusDays(1)); // Different timestamp, same ID/Class
        assertEquals(reading1, reading2);
    }

    @Test
    void equals_shouldBeFalseForDifferentId() {
        ConcreteReading reading1 = new ConcreteReading(101, LocalDateTime.now());
        ConcreteReading reading2 = new ConcreteReading(102, LocalDateTime.now()); // Different ID
        assertNotEquals(reading1, reading2);
    }

    @Test
    void equals_shouldBeFalseForDifferentClass() {
        ConcreteReading reading1 = new ConcreteReading(101, LocalDateTime.now());
        // Simulate another subclass inheriting from Reading
        class AnotherReading extends Reading {
             public AnotherReading(int id, LocalDateTime ts) { super(id, ts); }
        }
        AnotherReading reading2 = new AnotherReading(101, LocalDateTime.now()); // Same ID, different Class
        assertNotEquals(reading1, reading2);
        assertNotEquals(reading1, new Object());
    }

    @Test
    void equals_shouldBeFalseForNull() {
        ConcreteReading reading1 = new ConcreteReading(101, LocalDateTime.now());
        assertNotEquals(reading1, null);
    }

     @Test
    void equals_shouldUseReferenceEqualityForTransientObjectsWithZeroId() {
         ConcreteReading transient1 = new ConcreteReading(0, LocalDateTime.now());
         ConcreteReading transient2 = new ConcreteReading(0, LocalDateTime.now()); // Different object, same ID (0)
         ConcreteReading transient1Ref = transient1;

         assertNotEquals(transient1, transient2, "Transient objects with ID 0 should not be equal unless same reference");
         assertEquals(transient1, transient1Ref, "Same reference should be equal");
    }

    @Test
    void hashCode_shouldBeSameForSameIdAndClass() {
        ConcreteReading reading1 = new ConcreteReading(101, LocalDateTime.now());
        ConcreteReading reading2 = new ConcreteReading(101, LocalDateTime.now().minusDays(1));
        assertEquals(reading1.hashCode(), reading2.hashCode());
    }

    @Test
    void hashCode_shouldBeDifferentForDifferentClass() {
         ConcreteReading reading1 = new ConcreteReading(101, LocalDateTime.now());
         class AnotherReading extends Reading {
              public AnotherReading(int id, LocalDateTime ts) { super(id, ts); }
         }
         AnotherReading reading2 = new AnotherReading(101, LocalDateTime.now()); // Same ID, different Class
         assertNotEquals(reading1.hashCode(), reading2.hashCode());
    }

     @Test
    void hashCode_shouldBeConsistentForTransientObjectsWithZeroId() {
        // Hashcode for ID=0 relies on Objects.hash(0, ConcreteReading.class) or similar
         ConcreteReading transient1 = new ConcreteReading(0, LocalDateTime.now());
         ConcreteReading transient2 = new ConcreteReading(0, LocalDateTime.now().minusDays(1));
         assertEquals(transient1.hashCode(), transient2.hashCode()); // Hash should be same if based on ID=0 and same class
    }
     @Test
    void hashCode_shouldDifferForTransientAndPersisted() {
        ConcreteReading transientReading = new ConcreteReading(0, LocalDateTime.now());
        ConcreteReading persistedReading = new ConcreteReading(101, LocalDateTime.now());
         assertNotEquals(transientReading.hashCode(), persistedReading.hashCode());
    }

}