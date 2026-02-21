package org.traffic.counter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.traffic.counter.metric.TotalVolumeMetric;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.traffic.counter.TestHelper.*;

class TrafficLogProcessorTest {

    @TempDir
    Path tempDir;

    @Test
    void processesAllValidLines() throws IOException {
        Path file = writeTemp("""
                2021-12-01T05:00:00 5
                2021-12-01T05:30:00 12
                2021-12-01T06:00:00 14
                """);

        TotalVolumeMetric total = new TotalVolumeMetric();
        TrafficLogProcessor.process(file, List.of(total));

        assertEquals("Total cars seen: 31", captureReport(total));
    }

    @Test
    void skipsMalformedLines() throws IOException {
        Path file = writeTemp("""
                2021-12-01T05:00:00 5
                this is garbage
                2021-12-01T06:00:00 14
                """);

        TotalVolumeMetric total = new TotalVolumeMetric();
        TrafficLogProcessor.process(file, List.of(total));

        assertEquals("Total cars seen: 19", captureReport(total));
    }

    @Test
    void skipsBlankLines() throws IOException {
        Path file = writeTemp("""
                2021-12-01T05:00:00 5
                
                2021-12-01T06:00:00 14
                """);

        TotalVolumeMetric total = new TotalVolumeMetric();
        TrafficLogProcessor.process(file, List.of(total));

        assertEquals("Total cars seen: 19", captureReport(total));
    }

    @Test
    void handlesEmptyFile() throws IOException {
        Path file = writeTemp("");

        TotalVolumeMetric total = new TotalVolumeMetric();
        TrafficLogProcessor.process(file, List.of(total));

        assertEquals("Total cars seen: 0", captureReport(total));
    }

    private Path writeTemp(String content) throws IOException {
        Path file = tempDir.resolve("test-input.txt");
        Files.writeString(file, content);
        return file;
    }
}

