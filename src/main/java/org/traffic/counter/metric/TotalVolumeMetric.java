package org.traffic.counter.metric;
import org.traffic.counter.HalfHourReading;

/** Accumulates the total number of cars seen across all readings. */
public class TotalVolumeMetric implements TrafficMetric {
    private int totalCars;

	@Override
    public void accumulate(HalfHourReading reading) {
        totalCars += reading.carsSeen();
    }

    @Override
    public void printReport() {
        System.out.println("Total cars seen: " + totalCars);
    }
}
