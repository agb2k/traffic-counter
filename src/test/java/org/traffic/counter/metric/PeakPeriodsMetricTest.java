package org.traffic.counter.metric;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.traffic.counter.TestHelper.*;

class PeakPeriodsMetricTest {

    @Test
    void tracksTopThree() {
        PeakPeriodsMetric metric = new PeakPeriodsMetric();
        metric.accumulate(reading("2021-12-01T05:00:00", 5));
        metric.accumulate(reading("2021-12-01T07:30:00", 46));
        metric.accumulate(reading("2021-12-01T08:00:00", 42));
        metric.accumulate(reading("2021-12-08T18:00:00", 33));
        metric.accumulate(reading("2021-12-01T06:00:00", 14));

        assertEquals("""
                Top 3 half-hour periods with the most cars:
                2021-12-01T07:30:00 46
                2021-12-01T08:00:00 42
                2021-12-08T18:00:00 33""", captureReport(metric));
    }

    @Test
    void fewerThanThreeReadings() {
        PeakPeriodsMetric metric = new PeakPeriodsMetric();
        metric.accumulate(reading("2021-12-01T05:00:00", 5));

        assertEquals("""
                Top 3 half-hour periods with the most cars:
                2021-12-01T05:00:00 5""", captureReport(metric));
    }

    @Test
    void exactlyThreeReadings() {
        PeakPeriodsMetric metric = new PeakPeriodsMetric();
        metric.accumulate(reading("2021-12-01T05:00:00", 10));
        metric.accumulate(reading("2021-12-01T05:30:00", 20));
        metric.accumulate(reading("2021-12-01T06:00:00", 30));

        assertEquals("""
                Top 3 half-hour periods with the most cars:
                2021-12-01T06:00:00 30
                2021-12-01T05:30:00 20
                2021-12-01T05:00:00 10""", captureReport(metric));
    }

    @Test
    void handlesTiedCounts() {
        PeakPeriodsMetric metric = new PeakPeriodsMetric();
        metric.accumulate(reading("2021-12-01T05:00:00", 10));
        metric.accumulate(reading("2021-12-01T05:30:00", 10));
        metric.accumulate(reading("2021-12-01T06:00:00", 10));
        metric.accumulate(reading("2021-12-01T06:30:00", 5));

        // All three 10s should survive, the 5 should be evicted
        String output = captureReport(metric);
        assertTrue(output.contains(" 10"));
        assertFalse(output.contains(" 5"));
    }
}

