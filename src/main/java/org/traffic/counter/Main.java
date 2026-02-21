package org.traffic.counter;

import org.traffic.counter.metric.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Main {
    private static final String DEFAULT_RESOURCE = "input";

    static void main(String[] args) throws IOException, URISyntaxException {
        Path inputFile = resolveLogFile(args);

        List<TrafficMetric> metrics = List.of(new TotalVolumeMetric(), new DailyVolumeMetric(), new PeakPeriodsMetric(), new QuietestPeriodMetric());

        TrafficLogProcessor.process(inputFile, metrics);
        metrics.forEach(TrafficMetric::printReport);
    }

    private static Path resolveLogFile(String[] args) throws URISyntaxException {
        if (args.length >= 1) return Path.of(args[0]);

        return Path.of(Objects.requireNonNull(Main.class.getClassLoader().getResource(DEFAULT_RESOURCE)).toURI());
    }
}

