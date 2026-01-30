package biscuit;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that has to be completed by a certain date.
 */
public class Deadline extends Task {

    private static final DateTimeFormatter OUTPUT_FMT = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private final LocalDate by;
    
    /**
     * Creates a {@code Deadline} task.
     *
     * @param description Description of the deadline.
     * @param by Deadline date in {@code yyyy-MM-dd} format.
     */
    public Deadline(String description, LocalDate by) {
        super(description);
        this.by = by;
    }

    public LocalDate getBy() {
        return by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FMT) + ")";
    }
}