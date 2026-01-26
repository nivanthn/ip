/**
 * Represents a task that has to be completed by a certain date.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates a {@code Deadline} task.
     *
     * @param description Description of the deadline.
     * @param by Deadline date in {@code yyyy-MM-dd} format.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}