package org.traffic.counter;

import org.traffic.counter.metric.TrafficMetric;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

public final class TestHelper {

    private TestHelper() {}

    public static HalfHourReading reading(String timestamp, int cars) {
        return new HalfHourReading(LocalDateTime.parse(timestamp), cars);
    }

    /** Captures stdout from printReport() so tests can assert against it. */
    public static String captureReport(TrafficMetric metric) {
        PrintStream original = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        try {
            metric.printReport();
        } finally {
            System.setOut(original);
        }
        return out.toString().strip();
    }
}
