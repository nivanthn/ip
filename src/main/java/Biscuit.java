import java.util.Scanner;
import java.util.ArrayList;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Biscuit {

    private static final DateTimeFormatter DEADLINE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter EVENT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<Task> arr = new ArrayList<>();

        System.out.println("    ___________________________________");
        System.out.println("    Hello! I'm Biscuit");
        System.out.println("    What can I do for you?");
        System.out.println("    These are the commands that are available: add, list, mark, unmark, delete, bye");
        System.out.println("    ___________________________________");

        String input = sc.nextLine().trim();

        while (!input.toLowerCase().equals("bye")) {
            System.out.println("    ___________________________________");

            try {
                handleCommand(input, sc, arr);
            } catch (BiscuitException e) {
                System.out.println("    " + e.getMessage());
            }

            System.out.println("    ___________________________________");
            System.out.println("    These are the commands that are available: add, list, mark, unmark, delete, bye");
            System.out.println("    ___________________________________");
            input = sc.nextLine().trim();
        }

        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println("    ___________________________________");
    }

    private static void handleCommand(String input, Scanner sc, ArrayList<Task> arr) throws BiscuitException {
        String cmd = input.trim().toLowerCase();
        switch (cmd) {
        case "list":
            list(sc, arr);
            break;
        case "add":
            add(sc, arr);
            break;
        case "mark":
            mark(sc, arr);
            break;
        case "unmark":
            unmark(sc, arr);
            break;
        case "delete":
            delete(sc, arr);
            break;
        default:
            throw new BiscuitException("Not a valid command");
        }
    }

    public static void add(Scanner sc, ArrayList<Task> arr) throws BiscuitException {
        System.out.println("    Which type of task would you like to add?");
        System.out.println("    The types are: todo, event, deadline");
        String type = sc.nextLine().trim().toLowerCase();
        if (type.equals("todo")) {
            System.out.println("    Enter todo description: ");
            String description = readNonEmptyLine(sc, "Description cannot be empty.");
            Todo todo = new Todo(description);
            arr.add(todo);
            System.out.println("    added: " + todo);
        
        } else if (type.equals("event")) {
            System.out.println("    Enter event description: ");
            String description = readNonEmptyLine(sc, "Description cannot be empty.");
            System.out.println("    When is the event from: ");
            String from = readNonEmptyLine(sc, "Event start time cannot be empty.");
            System.out.println("    When is the event until: ");
            String to = readNonEmptyLine(sc, "Event end time cannot be empty.");

            LocalDateTime fromDt = parseEventDateTime(from, "event start");
            LocalDateTime toDt = parseEventDateTime(to, "event end");
            if (toDt.isBefore(fromDt)) {
                throw new BiscuitException("Event end must be after the event start.");
            }

            Event event = new Event(description, from, to);
            System.out.println("    added: " + event);
            arr.add(event);

        } else if (type.equals("deadline")) {
            System.out.println("    Enter deadline description: ");
            String description = readNonEmptyLine(sc, "Description cannot be empty.");
            System.out.println("    When is the deadline: ");
            String by = readNonEmptyLine(sc, "Deadline date cannot be empty.");
            validateDeadlineDate(by);
            Deadline deadline = new Deadline(description, by);
            arr.add(deadline);
            System.out.println("    added: " + deadline);

        } else {
            throw new BiscuitException("Not a valid task type. Use: todo, event, deadline");
        }
    }

    public static void list(Scanner sc, ArrayList<Task> arr) {
        for (int i = 0; i < arr.size(); i++) {
            System.out.println("     " + (i+1) + ". " + arr.get(i));
        }
    }

    public static void mark(Scanner sc, ArrayList<Task> arr) throws BiscuitException {
        System.out.println("    Which task would you like to mark done?");
        int index = readValidIndex(sc, arr, "mark");
        Task task = arr.get(index - 1);
        task.mark();
        System.out.println("    Ok! The task below is marked done");
        System.out.println(task);
    }

    public static void unmark(Scanner sc, ArrayList<Task> arr) throws BiscuitException {
        System.out.println("    Which task would you like to unmark?");
        int index = readValidIndex(sc, arr, "unmark");
        Task task = arr.get(index - 1);
        task.unmark();
        System.out.println("    Ok! The task below is marked undone");
        System.out.println(task);
    }

        private static int readValidIndex(Scanner sc, ArrayList<Task> arr, String action) throws BiscuitException {
        if (arr.isEmpty()) {
            throw new BiscuitException("No tasks to " + action + " yet.");
        }

        String raw = sc.nextLine().trim();
        int index;
        try {
            index = Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new BiscuitException("Please enter a task number (1 to " + arr.size() + ").");
        }

        if (index < 1 || index > arr.size()) {
            throw new BiscuitException("Task number out of range. Enter 1 to " + arr.size() + ".");
        }
        return index;
    }

    public static void delete(Scanner sc, ArrayList<Task> arr) throws BiscuitException {
        System.out.println("    Which task would you like to delete?");
        int index = readValidIndex(sc, arr, "delete");
        Task removed = arr.remove(index - 1);
        System.out.println("    Ok! I've deleted this task:");
        System.out.println("    " + removed);
    }

    private static String readNonEmptyLine(Scanner sc, String errorMessage) throws BiscuitException {
        String s = sc.nextLine();
        if (s == null || s.trim().isEmpty()) {
            throw new BiscuitException(errorMessage);
        }
        return s.trim();
    }

    private static void validateDeadlineDate(String by) throws BiscuitException {
        try {
            LocalDate.parse(by.trim(), DEADLINE_FMT);
        } catch (DateTimeParseException e) {
            throw new BiscuitException("Invalid deadline date. Use YYYY-MM-DD (e.g., 2026-01-20).");
        }
    }

    private static LocalDateTime parseEventDateTime(String raw, String fieldName) throws BiscuitException {
        try {
            return LocalDateTime.parse(raw.trim(), EVENT_FMT);
        } catch (DateTimeParseException e) {
            throw new BiscuitException(
                "Invalid " + fieldName + " format. Use YYYY-MM-DD HH:mm (e.g., 2026-01-21 19:00)."
            );
        }
    }

}
