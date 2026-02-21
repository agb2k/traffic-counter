package org.traffic.counter;

import lombok.experimental.UtilityClass;
import org.traffic.counter.metric.TrafficMetric;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Reads a traffic log file line-by-line, broadcasting each parsed reading to all metrics. */
@UtilityClass
public class TrafficLogProcessor {

    public void process(Path inputFilePath, List<TrafficMetric> metrics) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(inputFilePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;

                accumulateMetrics(line, metrics);
            }
        }
    }

    private void accumulateMetrics(String line, List<TrafficMetric> metrics) {
        try {
            HalfHourReading reading = HalfHourReading.parse(line);
            metrics.forEach(metric -> metric.accumulate(reading));
        } catch (Exception _) {
            System.err.println("Skipping malformed line: " + line);
        }
    }
}

