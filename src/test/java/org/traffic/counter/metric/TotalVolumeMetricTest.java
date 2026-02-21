package org.traffic.counter.metric;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.traffic.counter.TestHelper.*;

class TotalVolumeMetricTest {

    @Test
    void accumulatesTotal() {
        TotalVolumeMetric metric = new TotalVolumeMetric();
        metric.accumulate(reading("2021-12-01T05:00:00", 5));
        metric.accumulate(reading("2021-12-01T05:30:00", 12));
        metric.accumulate(reading("2021-12-01T06:00:00", 14));

        assertEquals("Total cars seen: 31", captureReport(metric));
    }

    @Test
    void zeroWhenEmpty() {
        assertEquals("Total cars seen: 0", captureReport(new TotalVolumeMetric()));
    }

    @Test
    void singleReading() {
        TotalVolumeMetric metric = new TotalVolumeMetric();
        metric.accumulate(reading("2021-12-01T05:00:00", 7));
        assertEquals("Total cars seen: 7", captureReport(metric));
    }
}

