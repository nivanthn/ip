/**
 * Represents a task that occurs during a specific time range.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an {@code Event} task.
     *
     * @param description Description of the event.
     * @param from Event start date-time in {@code yyyy-MM-dd HH:mm} format.
     * @param to Event end date-time in {@code yyyy-MM-dd HH:mm} format.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
