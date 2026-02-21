package org.traffic.counter.metric;
import org.traffic.counter.HalfHourReading;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Tracks the top 3 half-hour periods with the most cars using a min-heap capped at size 3.
 */
public class PeakPeriodsMetric implements TrafficMetric {
    private static final int TOP_N = 3;

    // Min-heap: smallest carsSeen on top, so we can evict it when a larger one arrives
    private final PriorityQueue<HalfHourReading> minHeap = new PriorityQueue<>(Comparator.comparingInt(HalfHourReading::carsSeen));

    @Override
    public void accumulate(HalfHourReading reading) {
        minHeap.offer(reading);

        // If we exceed capacity, drop the smallest (only the top N survive)
        if (minHeap.size() > TOP_N) minHeap.poll();
    }

    @Override
    public void printReport() {
	    System.out.println("\nTop " + TOP_N + " half-hour periods with the most cars:");

		// Heap is min-ordered, so sort descending for output (busiest first)
        minHeap.stream()
                .sorted(Comparator.comparingInt(HalfHourReading::carsSeen).reversed())
                .forEach(r -> System.out.println(r.periodStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + " " + r.carsSeen()));
    }
}
