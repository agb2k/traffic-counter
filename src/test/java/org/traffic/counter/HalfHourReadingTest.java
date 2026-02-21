package org.traffic.counter;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HalfHourReadingTest {

    @Test
    void parsesValidLine() {
        HalfHourReading reading = HalfHourReading.parse("2021-12-01T05:00:00 5");
        assertEquals(LocalDateTime.of(2021, 12, 1, 5, 0), reading.periodStart());
        assertEquals(5, reading.carsSeen());
    }

    @Test
    void handlesExtraWhitespace() {
        HalfHourReading reading = HalfHourReading.parse("  2021-12-01T05:00:00   12  ");
        assertEquals(12, reading.carsSeen());
    }

    @Test
    void parsesZeroCarCount() {
        HalfHourReading reading = HalfHourReading.parse("2021-12-01T23:30:00 0");
        assertEquals(0, reading.carsSeen());
    }

    @Test
    void rejectsMalformedInput() {
        assertThrows(IllegalArgumentException.class, () -> HalfHourReading.parse("garbage"));
        assertThrows(Exception.class, () -> HalfHourReading.parse("not-a-date 5"));
        assertThrows(Exception.class, () -> HalfHourReading.parse("2021-12-01T05:00:00 abc"));
    }
}

