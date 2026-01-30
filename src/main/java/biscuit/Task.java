package biscuit;

/**
 * Represents a task in Biscuit.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a new task with the given description.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as done.
     */
    public void mark() {
        isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void unmark() {
        isDone = false;
    }

    /**
     * Returns whether the task is marked as done.
     *
     * @return True if done.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the description of the task.
     *
     * @return Task description.
     */
    protected String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return (isDone ? "[X] " : "[ ] ") + description;
    }
}