# Traffic Counter

A Java 25 CLI tool that processes log files from an automated traffic counter and outputs four metrics:

1. **Total cars** seen across all readings
2. **Cars per day** in chronological order
3. **Top 3 busiest** half-hour periods
4. **Quietest 1.5-hour period** (3 temporally contiguous records with the least cars)

## How to Run

Run `Main.java` directly from IntelliJ. It uses the bundled sample input at `src/main/resources/input` by default.

## How It Works

- The file is processed in a **single pass** using a `BufferedReader`
- Each line is parsed into a `HalfHourReading` record via a static factory method
- Each reading is broadcast to a list of `TrafficMetric` observers that accumulate state independently
- After processing, each metric prints its report
- Adding a new metric is one class implementing `TrafficMetric` and one line in `Main`

```
HalfHourReading.parse()  →  TrafficLogProcessor  →  [TotalVolume, DailyVolume, PeakPeriods, QuietestPeriod]
```


## Key Decisions

- **Scalability and maintainability in mind throughout**: Chose data structures and patterns that work for both small and large inputs, while keeping the code easy to extend.
- **Stream and process, don't store everything**: A `BufferedReader` streams the file line-by-line, and each metric updates itself as lines are read, rather than loading the whole file into memory first. Memory usage stays constant regardless of file size.
- **Min-heap for top 3 periods**: A `PriorityQueue` capped at size 3 keeps only the largest readings. When a new one arrives, the smallest is evicted. O(1) memory and time per reading.
- **Sliding window for quietest period**: An `ArrayDeque` of size 3 tracks a running sum, sliding forward one reading at a time. If there's a time gap (not exactly 30 minutes apart), the window resets.
- **Unit tests for core functionality**: Each metric and the parser are tested independently, covering happy paths, edge cases, and error handling (e.g. malformed input, time gaps in the sliding window).

## Assumptions

- "3 contiguous half-hour records" means temporally contiguous (each exactly 30 minutes apart), not just consecutive lines in the file. Gaps in the data reset the sliding window.
- Input lines follow the format `<ISO-8601 timestamp> <integer>`. Malformed lines are logged to `stderr` and skipped.
- Input is chronologically ordered.

## Trade-offs

- Lombok is used for `@UtilityClass` on `TrafficLogProcessor` to reduce boilerplate. It's a compile-time-only dependency.
- `printReport()` writes directly to `stdout`. For a larger system, returning a structured result would be more testable, but for this scope it keeps things simple.
- The single-pass design means each reading passes through once. Metrics must track their own state (e.g. last 3 readings, top 3 seen so far) since they can't re-examine earlier data.




