import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Runs the Biscuit command-line application.
 */
public class Biscuit {

    private static final DateTimeFormatter DEADLINE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter EVENT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final String HORIZONTAL_LINE = "    ___________________________________";
    private static final String COMMANDS_PROMPT =
            "    These are the commands that are available: add, list, mark, unmark, delete, bye";

    /**
     * Starts the Biscuit application.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Storage storage = new Storage();
            List<Task> tasks = loadTasks(storage);

            printWelcome();

            String input = scanner.nextLine().trim();
            while (true) {
                printLine();

                try {
                    Command command = Command.parse(input);
                    if (command == Command.BYE) {
                        break;
                    }

                    handleCommand(command, scanner, tasks, storage);
                } catch (BiscuitException e) {
                    System.out.println("    " + e.getMessage());
                }

                printLine();
                printPrompt();
                input = scanner.nextLine().trim();
            }

            System.out.println("    Bye. Hope to see you again soon!");
            printLine();
        }
    }

    private static List<Task> loadTasks(Storage storage) {
        try {
            return storage.load();
        } catch (BiscuitException e) {
            System.out.println("    " + e.getMessage());
            System.out.println("    Starting with an empty task list.");
            return new ArrayList<>();
        }
    }

    private static void printWelcome() {
        printLine();
        System.out.println("    Hello! I'm Biscuit");
        System.out.println("    What can I do for you?");
        printPrompt();
        printLine();
    }

    private static void printPrompt() {
        System.out.println(COMMANDS_PROMPT);
    }

    private static void printLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    private static void handleCommand(Command command, Scanner scanner, List<Task> tasks, Storage storage)
            throws BiscuitException {
        switch (command) {
        case LIST:
            listTasks(tasks);
            break;
        case ADD:
            addTask(scanner, tasks, storage);
            break;
        case MARK:
            markTask(scanner, tasks, storage);
            break;
        case UNMARK:
            unmarkTask(scanner, tasks, storage);
            break;
        case DELETE:
            deleteTask(scanner, tasks, storage);
            break;
        default:
            break;
        }
    }

    private static void addTask(Scanner scanner, List<Task> tasks, Storage storage) throws BiscuitException {
        System.out.println("    Which type of task would you like to add?");
        System.out.println("    The types are: todo, event, deadline");

        String type = scanner.nextLine().trim().toLowerCase();
        switch (type) {
        case "todo":
            addTodo(scanner, tasks, storage);
            break;
        case "event":
            addEvent(scanner, tasks, storage);
            break;
        case "deadline":
            addDeadline(scanner, tasks, storage);
            break;
        default:
            throw new BiscuitException("Not a valid task type. Use: todo, event, deadline");
        }
    }

    private static void addTodo(Scanner scanner, List<Task> tasks, Storage storage) throws BiscuitException {
        System.out.println("    Enter todo description:");
        String description = readNonEmptyLine(scanner, "Description cannot be empty.");

        Todo todo = new Todo(description);
        tasks.add(todo);
        storage.save(tasks);
        System.out.println("    added: " + todo);
    }

    private static void addEvent(Scanner scanner, List<Task> tasks, Storage storage) throws BiscuitException {
        System.out.println("    Enter event description:");
        String description = readNonEmptyLine(scanner, "Description cannot be empty.");
        System.out.println("    When is the event from (YYYY-MM-DD HH:mm):");
        String from = readNonEmptyLine(scanner, "Event start time cannot be empty.");
        System.out.println("    When is the event until (YYYY-MM-DD HH:mm):");
        String to = readNonEmptyLine(scanner, "Event end time cannot be empty.");

        LocalDateTime fromDt = parseEventDateTime(from, "event start");
        LocalDateTime toDt = parseEventDateTime(to, "event end");
        if (toDt.isBefore(fromDt)) {
            throw new BiscuitException("Event end must be after the event start.");
        }

        Event event = new Event(description, fromDt, toDt);
        tasks.add(event);
        storage.save(tasks);
        System.out.println("    added: " + event);
    }


    private static void addDeadline(Scanner scanner, List<Task> tasks, Storage storage) throws BiscuitException {
        System.out.println("    Enter deadline description:");
        String description = readNonEmptyLine(scanner, "Description cannot be empty.");
        System.out.println("    When is the deadline (YYYY-MM-DD):");
        String byRaw = readNonEmptyLine(scanner, "Deadline date cannot be empty.");

        LocalDate by = parseDeadlineDate(byRaw);

        Deadline deadline = new Deadline(description, by);
        tasks.add(deadline);
        storage.save(tasks);
        System.out.println("    added: " + deadline);
    }


    private static void listTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("    No tasks yet.");
            return;
        }

        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + ". " + tasks.get(i));
        }
    }

    private static void markTask(Scanner scanner, List<Task> tasks, Storage storage) throws BiscuitException {
        System.out.println("    Which task would you like to mark done?");
        int index = readValidIndex(scanner, tasks, "mark");

        Task task = tasks.get(index - 1);
        task.mark();
        storage.save(tasks);
        System.out.println("    Ok! The task below is marked done");
        System.out.println(task);
    }

    private static void unmarkTask(Scanner scanner, List<Task> tasks, Storage storage) throws BiscuitException {
        System.out.println("    Which task would you like to unmark?");
        int index = readValidIndex(scanner, tasks, "unmark");

        Task task = tasks.get(index - 1);
        task.unmark();
        storage.save(tasks);
        System.out.println("    Ok! The task below is marked undone");
        System.out.println(task);
    }

    private static void deleteTask(Scanner scanner, List<Task> tasks, Storage storage) throws BiscuitException {
        System.out.println("    Which task would you like to delete?");
        int index = readValidIndex(scanner, tasks, "delete");

        Task removed = tasks.remove(index - 1);
        storage.save(tasks);
        System.out.println("    Ok! I've deleted this task:");
        System.out.println("    " + removed);
    }

    private static int readValidIndex(Scanner scanner, List<Task> tasks, String action)
            throws BiscuitException {
        if (tasks.isEmpty()) {
            throw new BiscuitException("No tasks to " + action + " yet.");
        }

        String raw = scanner.nextLine().trim();
        int index;
        try {
            index = Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new BiscuitException("Please enter a task number (1 to " + tasks.size() + ").");
        }

        if (index < 1 || index > tasks.size()) {
            throw new BiscuitException("Task number out of range. Enter 1 to " + tasks.size() + ".");
        }
        return index;
    }

    private static String readNonEmptyLine(Scanner scanner, String errorMessage) throws BiscuitException {
        String line = scanner.nextLine();
        if (line == null || line.trim().isEmpty()) {
            throw new BiscuitException(errorMessage);
        }
        return line.trim();
    }

    private static LocalDate parseDeadlineDate(String raw) throws BiscuitException {
        try {
            return LocalDate.parse(raw.trim(), DEADLINE_FMT);
        } catch (DateTimeParseException e) {
            throw new BiscuitException("Invalid deadline date. Use YYYY-MM-DD (e.g., 2026-01-20).");
        }
    }


    private static LocalDateTime parseEventDateTime(String raw, String fieldName) throws BiscuitException {
        try {
            return LocalDateTime.parse(raw.trim(), EVENT_FMT);
        } catch (DateTimeParseException e) {
            throw new BiscuitException(
                    "Invalid " + fieldName + " format. Use YYYY-MM-DD HH:mm (e.g., 2026-01-21 19:00).");
        }
    }
}
