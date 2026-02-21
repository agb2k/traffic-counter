package org.traffic.counter;

import java.time.LocalDateTime;

/** A single half-hour observation: timestamp and car count. */
public record HalfHourReading(LocalDateTime periodStart, int carsSeen) {

    /** Parses a raw log line (e.g. "2021-12-01T05:00:00 5") into a HalfHourReading. */
    public static HalfHourReading parse(String rawLine) {
        String[] parts = rawLine.strip().split("\\s+", 2);
        if (parts.length != 2) throw new IllegalArgumentException("Malformed line: " + rawLine);

        return new HalfHourReading(LocalDateTime.parse(parts[0]), Integer.parseInt(parts[1]));
    }
}

