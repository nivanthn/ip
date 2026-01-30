package biscuit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that occurs during a specific time range.
 */
public class Event extends Task {

    private static final DateTimeFormatter OUTPUT_FMT = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates an {@code Event} task.
     *
     * @param description Description of the event.
     * @param from Event start date-time.
     * @param to Event end date-time.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString()
                + " (from: " + from.format(OUTPUT_FMT)
                + " to: " + to.format(OUTPUT_FMT) + ")";
    }
}
