package biscuit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Parses and validates user input.
 * <p>
 * Provides helpers for interpreting commands, indices, and date/time fields.
 */
public class Parser {

    private static final DateTimeFormatter DEADLINE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter EVENT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Parses a raw user command into a {@link Command}.
     *
     * @param raw Raw user input.
     * @return Parsed {@link Command}.
     * @throws BiscuitException If the command is unknown or invalid.
     */
    public static Command parseCommand(String raw) throws BiscuitException {
        return Command.parse(raw);
    }

    /**
     * Ensures the given input is not null/blank.
     *
     * @param raw     Input to validate.
     * @param message Error message to use if validation fails.
     * @return Trimmed input string.
     * @throws BiscuitException If the input is null or blank.
     */
    public static String requireNonEmpty(String raw, String message) throws BiscuitException {
        if (raw == null || raw.trim().isEmpty()) {
            throw new BiscuitException(message);
        }
        return raw.trim();
    }

    /**
     * Parses and validates a 1-based task index entered by the user.
     *
     * @param raw    Raw index string entered by the user.
     * @param tasks  Current task list.
     * @param action Action name used to tailor error messages (e.g., "mark",
     *               "delete").
     * @return Validated 1-based index.
     * @throws BiscuitException If the list is empty, the input is not a number, or
     *                          is out of range.
     */
    public static int parseIndex(String raw, TaskList tasks, String action) throws BiscuitException {
        if (tasks.isEmpty()) {
            throw new BiscuitException("No tasks to " + action + " yet.");
        }

        int index;
        try {
            index = Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            throw new BiscuitException("Please enter a task number (1 to " + tasks.size() + ").");
        }

        if (index < 1 || index > tasks.size()) {
            throw new BiscuitException("Task number out of range. Enter 1 to " + tasks.size() + ".");
        }
        return index;
    }

    /**
     * Parses a deadline date in {@code yyyy-MM-dd} format.
     *
     * @param raw Raw date string.
     * @return Parsed {@link LocalDate}.
     * @throws BiscuitException If the date format is invalid.
     */
    public static LocalDate parseDeadlineDate(String raw) throws BiscuitException {
        try {
            return LocalDate.parse(raw.trim(), DEADLINE_FMT);
        } catch (DateTimeParseException e) {
            throw new BiscuitException("Invalid deadline date. Use YYYY-MM-DD (e.g., 2026-01-20).");
        }
    }

    /**
     * Parses an event date-time in {@code yyyy-MM-dd HH:mm} format.
     *
     * @param raw       Raw date-time string.
     * @param fieldName Field label used to tailor error messages (e.g., "event
     *                  start").
     * @return Parsed {@link LocalDateTime}.
     * @throws BiscuitException If the date-time format is invalid.
     */
    public static LocalDateTime parseEventDateTime(String raw, String fieldName) throws BiscuitException {
        try {
            return LocalDateTime.parse(raw.trim(), EVENT_FMT);
        } catch (DateTimeParseException e) {
            throw new BiscuitException(
                    "Invalid " + fieldName + " format. Use YYYY-MM-DD HH:mm (e.g., 2026-01-21 19:00).");
        }
    }
}
