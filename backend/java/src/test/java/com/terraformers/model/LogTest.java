package com.terraformers.model;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Test class for the abstract Log class
// Needs a concrete implementation for testing
class LogTest {

    // Concrete subclass for testing purposes
    // Needs to be static or in a separate file for JUnit to work easily with it
    static class ConcreteLog extends Log {
        public ConcreteLog() {
            super();
        }

        public ConcreteLog(int aLogID, Date aLogDate, Time aLogTime, LogType aLogType, String aLogMessage) {
            super(aLogID, aLogDate, aLogTime, aLogType, aLogMessage);
        }
    }

    // Another concrete subclass to test equals/hashCode across different types
    static class AnotherConcreteLog extends Log {
         public AnotherConcreteLog(int aLogID, Date aLogDate, Time aLogTime, LogType aLogType, String aLogMessage) {
            super(aLogID, aLogDate, aLogTime, aLogType, aLogMessage);
        }
    }


    // Class Under Test (using ConcreteLog) - Initialized in setUp
    private ConcreteLog log;

    // Test Constants used for object created in setUp and some comparison tests
    private final int INITIAL_LOG_ID = 701;
    private final Date INITIAL_LOG_DATE = Date.valueOf(LocalDate.now().minusDays(1));
    private final Time INITIAL_LOG_TIME = Time.valueOf(LocalTime.now().minusHours(2));
    private final Log.LogType INITIAL_LOG_TYPE = Log.LogType.Warning;
    private final String INITIAL_LOG_MESSAGE = "Initial log message.";

    @BeforeEach
    void setUp() {
        // Create a standard log instance for tests that modify/check its state
        log = new ConcreteLog(INITIAL_LOG_ID, INITIAL_LOG_DATE, INITIAL_LOG_TIME, INITIAL_LOG_TYPE, INITIAL_LOG_MESSAGE);
    }

    // --- Constructor Test ---
    @Test
    void shouldInitializeCorrectlyViaConstructor() {
        // Define specific values for *this* test, different from setUp values
        Date testDate = Date.valueOf(LocalDate.now().minusDays(5));
        Time testTime = Time.valueOf(LocalTime.now().minusHours(5));
        Log.LogType testType = Log.LogType.Info;
        String testMessage = "Specific Test Message";
        int testId = 123;

        // Create the object using these specific values
        ConcreteLog testLog = new ConcreteLog(testId, testDate, testTime, testType, testMessage);

        // Assert using the specific values defined in this test
        assertNotNull(testLog);
        assertEquals(testId, testLog.getLogID());
        assertEquals(testDate, testLog.getLogDate());
        assertEquals(testTime, testLog.getLogTime());
        assertEquals(testType, testLog.getLogType());
        assertEquals(testMessage, testLog.getLogMessage());
    }

     @Test
    void shouldInitializeCorrectlyViaNoArgConstructor() {
         // Create using no-arg constructor
         ConcreteLog testLog = new ConcreteLog();

         // Assert default initial values
         assertNotNull(testLog);
         assertEquals(0, testLog.getLogID()); // Default int value
         assertNull(testLog.getLogDate());    // Default object value
         assertNull(testLog.getLogTime());    // Default object value
         assertNull(testLog.getLogType());    // Default object value
         assertNull(testLog.getLogMessage()); // Default object value
    }


    // --- Setter/Getter Tests (using the object from setUp) ---
    @Test
    void shouldSetAndGetLogID() {
        int newId = 702;
        assertTrue(log.setLogID(newId));
        assertEquals(newId, log.getLogID());
    }

    @Test
    void shouldSetAndGetLogDate() {
        Date newDate = Date.valueOf(LocalDate.now());
        // Ensure difference from INITIAL_LOG_DATE if tests run fast
        if (newDate.equals(INITIAL_LOG_DATE)) {
            newDate = Date.valueOf(LocalDate.now().plusDays(1));
        }
        assertTrue(log.setLogDate(newDate));
        assertEquals(newDate, log.getLogDate());
    }

    @Test
    void shouldSetAndGetLogTime() {
        Time newTime = Time.valueOf(LocalTime.now());
         // Ensure difference from INITIAL_LOG_TIME if tests run fast
        if (newTime.equals(INITIAL_LOG_TIME)) {
            newTime = Time.valueOf(LocalTime.now().plusSeconds(10));
        }
        assertTrue(log.setLogTime(newTime));
        assertEquals(newTime, log.getLogTime());
    }

    @Test
    void shouldSetAndGetLogType() {
        Log.LogType newType = Log.LogType.Error;
        assertTrue(log.setLogType(newType));
        assertEquals(newType, log.getLogType());
    }

    @Test
    void shouldSetAndGetLogMessage() {
        String newMessage = "Updated log message.";
        assertTrue(log.setLogMessage(newMessage));
        assertEquals(newMessage, log.getLogMessage());
    }

    // --- Delete Test ---
    @Test
    void delete_shouldNotThrowException() {
        assertDoesNotThrow(() -> log.delete());
    }

    // --- equals() and hashCode() Tests ---

    @Test
    void equals_shouldBeTrueForSameIdAndClass() {
        // Create new instances with same ID but different other attributes
        ConcreteLog log1 = new ConcreteLog(101, INITIAL_LOG_DATE, INITIAL_LOG_TIME, Log.LogType.Info, "M1");
        ConcreteLog log2 = new ConcreteLog(101, Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), Log.LogType.Warning, "M2");
        assertEquals(log1, log2);
    }

    @Test
    void equals_shouldBeFalseForDifferentId() {
        ConcreteLog log1 = new ConcreteLog(101, INITIAL_LOG_DATE, INITIAL_LOG_TIME, Log.LogType.Info, "M1");
        ConcreteLog log2 = new ConcreteLog(102, INITIAL_LOG_DATE, INITIAL_LOG_TIME, Log.LogType.Info, "M1");
        assertNotEquals(log1, log2);
    }

    @Test
    void equals_shouldBeFalseForDifferentClass() {
        ConcreteLog log1 = new ConcreteLog(101, INITIAL_LOG_DATE, INITIAL_LOG_TIME, Log.LogType.Info, "M1");
        AnotherConcreteLog log2 = new AnotherConcreteLog(101, INITIAL_LOG_DATE, INITIAL_LOG_TIME, Log.LogType.Info, "M1"); // Same ID, different Class
        assertNotEquals(log1, log2);
        assertNotEquals(log1, new Object());
    }

    @Test
    void equals_shouldBeFalseForNull() {
        ConcreteLog log1 = new ConcreteLog(101, INITIAL_LOG_DATE, INITIAL_LOG_TIME, Log.LogType.Info, "M1");
        assertNotEquals(log1, null);
    }

     @Test
    void equals_shouldUseReferenceEqualityForTransientObjectsWithZeroId() {
         ConcreteLog transient1 = new ConcreteLog(0, INITIAL_LOG_DATE, INITIAL_LOG_TIME, Log.LogType.Info, "T1");
         ConcreteLog transient2 = new ConcreteLog(0, INITIAL_LOG_DATE, INITIAL_LOG_TIME, Log.LogType.Info, "T2"); // Different object, same ID (0)
         ConcreteLog transient1Ref = transient1;

         assertNotEquals(transient1, transient2, "Transient objects with ID 0 should not be equal unless same reference");
         assertEquals(transient1, transient1Ref, "Same reference should be equal");
    }


    @Test
    void hashCode_shouldBeSameForSameIdAndClass() {
        ConcreteLog log1 = new ConcreteLog(101, INITIAL_LOG_DATE, INITIAL_LOG_TIME, Log.LogType.Info, "M1");
        ConcreteLog log2 = new ConcreteLog(101, Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), Log.LogType.Warning, "M2");
        assertEquals(log1.hashCode(), log2.hashCode());
    }

    @Test
    void hashCode_shouldBeDifferentForDifferentClass() {
        ConcreteLog log1 = new ConcreteLog(101, INITIAL_LOG_DATE, INITIAL_LOG_TIME, Log.LogType.Info, "M1");
        AnotherConcreteLog log2 = new AnotherConcreteLog(101, INITIAL_LOG_DATE, INITIAL_LOG_TIME, Log.LogType.Info, "M1"); // Same ID, different Class
        assertNotEquals(log1.hashCode(), log2.hashCode());
    }

    @Test
    void hashCode_shouldBeConsistentForTransientObjectsWithZeroId() {
         ConcreteLog transient1 = new ConcreteLog(0, INITIAL_LOG_DATE, INITIAL_LOG_TIME, Log.LogType.Info, "T1");
         ConcreteLog transient2 = new ConcreteLog(0, Date.valueOf(LocalDate.now()), Time.valueOf(LocalTime.now()), Log.LogType.Warning, "T2");
         assertEquals(transient1.hashCode(), transient2.hashCode()); // Hash should be same if based on ID=0 and same class
    }

     @Test
    void hashCode_shouldDifferForTransientAndPersisted() {
        ConcreteLog transientLog = new ConcreteLog(0, INITIAL_LOG_DATE, INITIAL_LOG_TIME, Log.LogType.Info, "T1");
        ConcreteLog persistedLog = new ConcreteLog(101, INITIAL_LOG_DATE, INITIAL_LOG_TIME, Log.LogType.Info, "P1");
         assertNotEquals(transientLog.hashCode(), persistedLog.hashCode());
    }
}