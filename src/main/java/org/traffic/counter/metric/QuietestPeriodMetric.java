package org.traffic.counter.metric;

import org.traffic.counter.HalfHourReading;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.List;

/**
 * Finds the 1.5-hour period (3 temporally contiguous records) with the least total cars using a sliding window of up to 3 consecutive half-hour records.
 */
public class QuietestPeriodMetric implements TrafficMetric {

    private static final int WINDOW_SIZE = 3;
    private static final Duration EXPECTED_GAP = Duration.ofMinutes(30);

    // Sliding window state
    private final ArrayDeque<HalfHourReading> window = new ArrayDeque<>(WINDOW_SIZE);
    private int windowSum;

    // Best result seen so far
    private int minSum = Integer.MAX_VALUE;
    private List<HalfHourReading> quietestWindow = List.of();

    @Override
    public void accumulate(HalfHourReading reading) {
        resetIfNotContiguous(reading);
        addToWindow(reading);
        trackQuietestWindow();
    }

	// Ensure readings are exactly 30 min apart, otherwise start fresh
	private void resetIfNotContiguous(HalfHourReading reading) {
        if (window.isEmpty()) return;

        Duration gap = Duration.between(window.getLast().periodStart(), reading.periodStart());
        if (!gap.equals(EXPECTED_GAP)) {
            window.clear();
            windowSum = 0;
        }
    }

    private void addToWindow(HalfHourReading reading) {
        window.addLast(reading);
        windowSum += reading.carsSeen();

        // Slide forward: drop the oldest reading if we exceed the window size
        if (window.size() > WINDOW_SIZE) {
            windowSum -= window.removeFirst().carsSeen();
        }
    }

	// Once we have a full window, check if it's the quietest we've seen
	private void trackQuietestWindow() {
        if (window.size() == WINDOW_SIZE && windowSum < minSum) {
            minSum = windowSum;
            quietestWindow = List.copyOf(window);
        }
    }


    @Override
    public void printReport() {
        if (quietestWindow.isEmpty()) {
            System.out.println("Not enough data for a 1.5-hour window.");
            return;
        }

	    System.out.println("\nQuietest 1.5-hour period:");
        quietestWindow.forEach(r -> System.out.println(r.periodStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " " + r.carsSeen()));
    }
}

