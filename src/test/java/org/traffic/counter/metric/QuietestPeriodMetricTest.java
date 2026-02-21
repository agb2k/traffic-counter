package org.traffic.counter.metric;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.traffic.counter.TestHelper.*;

class QuietestPeriodMetricTest {

    @Test
    void findsQuietestContiguousWindow() {
        QuietestPeriodMetric metric = new QuietestPeriodMetric();
        metric.accumulate(reading("2021-12-01T05:00:00", 5));
        metric.accumulate(reading("2021-12-01T05:30:00", 12));
        metric.accumulate(reading("2021-12-01T06:00:00", 14));
        metric.accumulate(reading("2021-12-01T06:30:00", 15));

        assertEquals("""
                Quietest 1.5-hour period:
                2021-12-01T05:00:00 5
                2021-12-01T05:30:00 12
                2021-12-01T06:00:00 14""", captureReport(metric));
    }

    @Test
    void resetsWindowOnTimeGap() {
        QuietestPeriodMetric metric = new QuietestPeriodMetric();
        metric.accumulate(reading("2021-12-01T08:00:00", 1));
        metric.accumulate(reading("2021-12-01T08:30:00", 2));
        // gap here — next reading is not 30 min later
        metric.accumulate(reading("2021-12-01T15:00:00", 9));
        metric.accumulate(reading("2021-12-01T15:30:00", 11));
        metric.accumulate(reading("2021-12-01T16:00:00", 3));

        assertEquals("""
                Quietest 1.5-hour period:
                2021-12-01T15:00:00 9
                2021-12-01T15:30:00 11
                2021-12-01T16:00:00 3""", captureReport(metric));
    }

    @Test
    void exactlyThreeContiguousReadings() {
        QuietestPeriodMetric metric = new QuietestPeriodMetric();
        metric.accumulate(reading("2021-12-01T05:00:00", 5));
        metric.accumulate(reading("2021-12-01T05:30:00", 12));
        metric.accumulate(reading("2021-12-01T06:00:00", 14));

        assertEquals("""
                Quietest 1.5-hour period:
                2021-12-01T05:00:00 5
                2021-12-01T05:30:00 12
                2021-12-01T06:00:00 14""", captureReport(metric));
    }

    @Test
    void notEnoughContiguousData() {
        QuietestPeriodMetric metric = new QuietestPeriodMetric();
        metric.accumulate(reading("2021-12-01T05:00:00", 5));
        // gap
        metric.accumulate(reading("2021-12-01T10:00:00", 3));

        assertEquals("Not enough data for a 1.5-hour window.", captureReport(metric));
    }

    @Test
    void noValidWindowWhenAllReadingsHaveGaps() {
        QuietestPeriodMetric metric = new QuietestPeriodMetric();
        metric.accumulate(reading("2021-12-01T05:00:00", 5));
        metric.accumulate(reading("2021-12-01T10:00:00", 3));
        metric.accumulate(reading("2021-12-01T15:00:00", 7));
        metric.accumulate(reading("2021-12-01T20:00:00", 2));

        assertEquals("Not enough data for a 1.5-hour window.", captureReport(metric));
    }
}

