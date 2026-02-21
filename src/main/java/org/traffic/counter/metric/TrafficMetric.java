package org.traffic.counter.metric;

import org.traffic.counter.HalfHourReading;

/** Observer contract: accumulates readings during processing, then prints a report. */
public interface TrafficMetric {
    void accumulate(HalfHourReading reading);
    void printReport();
}

