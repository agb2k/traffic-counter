package org.traffic.counter.metric;
import org.traffic.counter.HalfHourReading;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
/** Cars seen per day. */
public class DailyVolumeMetric implements TrafficMetric {
    private final Map<LocalDate, Integer> dailyCounts = new LinkedHashMap<>();

	@Override
    public void accumulate(HalfHourReading reading) {
        dailyCounts.merge(reading.periodStart().toLocalDate(), reading.carsSeen(), Integer::sum);
    }

    @Override
    public void printReport() {
	    System.out.println("\nCars seen by date:");
        dailyCounts.forEach((date, count) -> System.out.println(date + " " + count));
    }
}
