package org.traffic.counter.metric;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.traffic.counter.TestHelper.*;

class DailyVolumeMetricTest {

    @Test
    void groupsByDate() {
        DailyVolumeMetric metric = new DailyVolumeMetric();
        metric.accumulate(reading("2021-12-01T05:00:00", 5));
        metric.accumulate(reading("2021-12-01T05:30:00", 12));
        metric.accumulate(reading("2021-12-05T09:30:00", 18));

        assertEquals("""
                Cars seen by date:
                2021-12-01 17
                2021-12-05 18""", captureReport(metric));
    }

    @Test
    void singleDay() {
        DailyVolumeMetric metric = new DailyVolumeMetric();
        metric.accumulate(reading("2021-12-08T18:00:00", 33));
        metric.accumulate(reading("2021-12-08T19:00:00", 28));

        assertEquals("""
                Cars seen by date:
                2021-12-08 61""", captureReport(metric));
    }
}

