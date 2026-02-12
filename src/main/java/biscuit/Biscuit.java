package biscuit;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Entry point of the Biscuit chatbot.
 * <p>
 * Coordinates user interaction ({@link Ui}), command interpretation
 * ({@link Parser}),
 * persistence ({@link Storage}), and task operations ({@link TaskList}).
 */
public class Biscuit {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Constructs a Biscuit instance and initializes its dependencies.
     * <p>
     * Loads tasks from disk using {@link Storage}. If loading fails, starts with an
     * empty task list.
     */
    public Biscuit() {
        ui = new Ui();
        storage = new Storage();

        TaskList loaded;
        try {
            loaded = new TaskList(storage.load());
        } catch (BiscuitException e) {
            ui.showError(e.getMessage());
            ui.showError("Starting with an empty task list.");
            loaded = new TaskList();
        }
        tasks = loaded;
    }

    /**
     * Runs the main read-eval-print loop of the chatbot until the user exits.
     * <p>
     * Reads user commands, executes the corresponding actions, and persists changes
     * to disk.
     */
    public void run() {
        ui.showWelcome();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                ui.showLine();
                String input = ui.readCommand(scanner);

                String response;
                try {
                    response = execute(input);
                } catch (BiscuitException e) {
                    response = e.getMessage();
                }

                System.out.println(response);

                if (isExit(input)) {
                    break;
                }
            }
        }

        ui.showGoodbye();
    }

    public static void main(String[] args) {
        new Biscuit().run();
    }

    /**
     * Executes a single-line user command and returns the response message.
     * <p>
     * This is the main entry point for both the CLI and GUI. The CLI prints the
     * returned
     * message, while the GUI displays it in a dialog box.
     *
     * @param input Full command entered by the user.
     * @return Response message to be shown to the user.
     * @throws BiscuitException If the command is invalid or cannot be executed.
     */
    public String execute(String input) throws BiscuitException {
        String trimmed = (input == null) ? "" : input.trim();
        if (trimmed.isEmpty()) {
            throw new BiscuitException("Command cannot be empty.");
        }

        String[] parts = trimmed.split("\\s+", 2);
        String keyword = parts[0].toLowerCase();
        String args = (parts.length == 2) ? parts[1] : "";

        switch (keyword) {
        case "list":
            return formatList();

        case "todo":
            return handleTodo(args);

        case "deadline":
            return handleDeadline(args);

        case "event":
            return handleEvent(args);

        case "mark":
            return handleMark(args);

        case "unmark":
            return handleUnmark(args);

        case "delete":
            return handleDelete(args);

        case "find":
            return handleFind(args);

        case "bye":
            return "Bye. Hope to see you again soon!";

        case "within":
            return handleWithin(args);

        case "display":
        case "help":
            return getHelpMessage();
        default:
            throw new BiscuitException("Unknown command: " + keyword);
        }
    }

    /**
     * Checks whether the given user input indicates the application should exit.
     *
     * @param input Full command entered by the user.
     * @return True if the command is an exit command (e.g. {@code bye}), false
     *         otherwise.
     */
    private boolean isExit(String input) {
        return input != null && input.trim().equalsIgnoreCase("bye");
    }

    /**
     * Formats the current task list into a printable string.
     *
     * @return A formatted list of tasks, or a message indicating the list is empty.
     */
    private String formatList() {
        if (tasks.isEmpty()) {
            return "No tasks yet.";
        }

        StringBuilder sb = new StringBuilder("Here are your tasks:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Handles the {@code todo} command by creating and storing a {@link Todo} task.
     *
     * @param args Description text after the {@code todo} keyword.
     * @return Confirmation message describing the added todo.
     * @throws BiscuitException If the description is empty.
     */
    private String handleTodo(String args) throws BiscuitException {
        String description = Parser.requireNonEmpty(args, "The description of a todo cannot be empty.");
        Todo todo = new Todo(description);
        tasks.add(todo);
        storage.save(tasks.asList());
        return "Added: " + todo;
    }

    /**
     * Handles the {@code deadline} command by creating and storing a
     * {@link Deadline} task.
     * <p>
     * Expected format: {@code deadline <description> /by YYYY-MM-DD}
     *
     * @param args Arguments after the {@code deadline} keyword.
     * @return Confirmation message describing the added deadline.
     * @throws BiscuitException If the format is invalid or date cannot be parsed.
     */
    private String handleDeadline(String args) throws BiscuitException {
        // format: deadline <desc> /by YYYY-MM-DD
        String[] split = args.split("\\s+/by\\s+", 2);
        if (split.length < 2) {
            throw new BiscuitException("Usage: deadline <description> /by YYYY-MM-DD");
        }

        String description = Parser.requireNonEmpty(split[0].trim(),
                "The description of a deadline cannot be empty.");
        String byRaw = Parser.requireNonEmpty(split[1].trim(),
                "The /by date cannot be empty.");

        LocalDate by = Parser.parseDeadlineDate(byRaw);
        Deadline deadline = new Deadline(description, by);

        tasks.add(deadline);
        storage.save(tasks.asList());
        return "Added: " + deadline;
    }

    /**
     * Handles the {@code event} command by creating and storing an {@link Event}
     * task.
     * <p>
     * Expected format:
     * {@code event <description> /from YYYY-MM-DD HH:mm /to YYYY-MM-DD HH:mm}
     *
     * @param args Arguments after the {@code event} keyword.
     * @return Confirmation message describing the added event.
     * @throws BiscuitException If the format is invalid, dates cannot be parsed, or
     *                          end is before start.
     */
    private String handleEvent(String args) throws BiscuitException {
        // format: event <desc> /from YYYY-MM-DD HH:mm /to YYYY-MM-DD HH:mm
        String[] fromSplit = args.split("\\s+/from\\s+", 2);
        if (fromSplit.length < 2) {
            throw new BiscuitException("Usage: event <description> /from YYYY-MM-DD HH:mm /to YYYY-MM-DD HH:mm");
        }

        String description = Parser.requireNonEmpty(fromSplit[0].trim(),
                "The description of an event cannot be empty.");

        String[] toSplit = fromSplit[1].split("\\s+/to\\s+", 2);
        if (toSplit.length < 2) {
            throw new BiscuitException("Usage: event <description> /from YYYY-MM-DD HH:mm /to YYYY-MM-DD HH:mm");
        }

        LocalDateTime from = Parser.parseEventDateTime(toSplit[0].trim(), "event start");
        LocalDateTime to = Parser.parseEventDateTime(toSplit[1].trim(), "event end");
        if (to.isBefore(from)) {
            throw new BiscuitException("Event end must be after the event start.");
        }

        Event event = new Event(description, from, to);
        tasks.add(event);
        storage.save(tasks.asList());
        return "Added: " + event;
    }

    private String handleMark(String args) throws BiscuitException {
        int index = Parser.parseIndex(Parser.requireNonEmpty(args, "Please provide a task number."), tasks, "mark");
        Task task = tasks.get(index - 1);
        task.mark();
        storage.save(tasks.asList());
        return "Marked as done: " + task;
    }

    private String handleUnmark(String args) throws BiscuitException {
        int index = Parser.parseIndex(Parser.requireNonEmpty(args, "Please provide a task number."), tasks, "unmark");
        Task task = tasks.get(index - 1);
        task.unmark();
        storage.save(tasks.asList());
        return "Marked as not done: " + task;
    }

    private String handleDelete(String args) throws BiscuitException {
        int index = Parser.parseIndex(Parser.requireNonEmpty(args, "Please provide a task number."), tasks, "delete");
        Task removed = tasks.remove(index - 1);
        storage.save(tasks.asList());
        return "Deleted: " + removed;
    }

    /**
     * Handles the {@code find} command by searching tasks containing the given
     * keyword.
     *
     * @param args Keyword text after the {@code find} keyword.
     * @return A formatted list of matching tasks, or a message indicating no
     *         matches were found.
     * @throws BiscuitException If the keyword is empty.
     */
    private String handleFind(String args) throws BiscuitException {
        if (tasks.isEmpty()) {
            return "No tasks yet.";
        }
        String keyword = Parser.requireNonEmpty(args, "Keyword cannot be empty.");
        List<Task> matches = tasks.find(keyword);

        if (matches.isEmpty()) {
            return "No matching tasks found for: " + keyword;
        }

        StringBuilder sb = new StringBuilder("Matching tasks:\n");
        for (int i = 0; i < matches.size(); i++) {
            sb.append("  ").append(i + 1).append(". ").append(matches.get(i)).append("\n");
        }
        return sb.toString().trim();
    }

    private String getHelpMessage() {
        return String.join("\n",
                "Available commands:",
                "  list",
                "  todo <description>",
                "  deadline <description> /by YYYY-MM-DD",
                "  event <description> /from YYYY-MM-DD HH:mm /to YYYY-MM-DD HH:mm",
                "  within <description> /from YYYY-MM-DD /to YYYY-MM-DD",
                "  mark <taskNumber>",
                "  unmark <taskNumber>",
                "  delete <taskNumber>",
                "  find <keyword>",
                "  display   (or: help)",
                "  bye");
    }

    /**
     * Returns a response for the GUI.
     * GUI interaction is single-line input -> single-line (or multi-line) output.
     */
    public String getResponse(String input) {
        try {
            return execute(input);
        } catch (BiscuitException e) {
            return e.getMessage();
        }
    }

    /**
     * Handles the {@code within} command by creating and storing a {@link DoWithinPeriodTask}.
     * <p>
     * Expected format: {@code within <description> /from YYYY-MM-DD /to YYYY-MM-DD}
     *
     * @param args Arguments after the {@code within} keyword.
     * @return Confirmation message describing the added task.
     * @throws BiscuitException If the format is invalid or dates cannot be parsed.
     */
    private String handleWithin(String args) throws BiscuitException {
        String[] fromSplit = args.split("\\s+/from\\s+", 2);
        if (fromSplit.length < 2) {
            throw new BiscuitException("Usage: within <description> /from YYYY-MM-DD /to YYYY-MM-DD");
        }

        String description = Parser.requireNonEmpty(fromSplit[0].trim(),
                "The description of a within-period task cannot be empty.");

        String[] toSplit = fromSplit[1].split("\\s+/to\\s+", 2);
        if (toSplit.length < 2) {
            throw new BiscuitException("Usage: within <description> /from YYYY-MM-DD /to YYYY-MM-DD");
        }

        LocalDate start = Parser.parseDate(toSplit[0].trim(), "start");
        LocalDate end = Parser.parseDate(toSplit[1].trim(), "end");

        if (end.isBefore(start)) {
            throw new BiscuitException("End date must be on or after the start date.");
        }

        DoWithinPeriodTask task = new DoWithinPeriodTask(description, start, end);
        tasks.add(task);
        storage.save(tasks.asList());
        return "Added: " + task;
    }


}
