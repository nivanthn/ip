package biscuit;

/**
 * Represents a simple to-do task.
 */
public class Todo extends Task {

    /**
     * Creates a {@code Todo} with the given description.
     *
     * @param description Description of the to-do.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}