import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {

    private static final DateTimeFormatter DEADLINE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter EVENT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static Command parseCommand(String raw) throws BiscuitException {
        return Command.parse(raw);
    }

    public static String requireNonEmpty(String raw, String message) throws BiscuitException {
        if (raw == null || raw.trim().isEmpty()) {
            throw new BiscuitException(message);
        }
        return raw.trim();
    }

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

    public static LocalDate parseDeadlineDate(String raw) throws BiscuitException {
        try {
            return LocalDate.parse(raw.trim(), DEADLINE_FMT);
        } catch (DateTimeParseException e) {
            throw new BiscuitException("Invalid deadline date. Use YYYY-MM-DD (e.g., 2026-01-20).");
        }
    }

    public static LocalDateTime parseEventDateTime(String raw, String fieldName) throws BiscuitException {
        try {
            return LocalDateTime.parse(raw.trim(), EVENT_FMT);
        } catch (DateTimeParseException e) {
            throw new BiscuitException(
                    "Invalid " + fieldName + " format. Use YYYY-MM-DD HH:mm (e.g., 2026-01-21 19:00).");
        }
    }
}
