package biscuit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Represents a task that needs to be done within a specific date range.
 */
public class DoWithinPeriodTask extends Task {

    private static final DateTimeFormatter OUTPUT_FMT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    private final LocalDate start;
    private final LocalDate end;

    /**
     * Creates a task that should be done between {@code start} and {@code end}
     * (inclusive).
     *
     * @param description Description of the task.
     * @param start       Start date of the period.
     * @param end         End date of the period.
     */
    public DoWithinPeriodTask(String description, LocalDate start, LocalDate end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "[P]" + super.toString()
                + " (between: " + start.format(OUTPUT_FMT)
                + " and " + end.format(OUTPUT_FMT) + ")";
    }
}
