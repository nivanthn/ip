package biscuit;

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
            String input = ui.readCommand(scanner);

            while (true) {
                ui.showLine();

                try {
                    Command command = Parser.parseCommand(input);
                    if (command == Command.BYE) {
                        break;
                    }
                    handleCommand(command, scanner);
                } catch (BiscuitException e) {
                    ui.showError(e.getMessage());
                }

                ui.showLine();
                ui.showCommands();
                ui.showLine();
                input = ui.readCommand(scanner);
            }
        }

        ui.showGoodbye();
    }

    public static void main(String[] args) {
        new Biscuit().run();
    }

    /**
     * Dispatches a parsed command to the corresponding handler method.
     *
     * @param command The parsed command type.
     * @param scanner Scanner used to read additional user input.
     * @throws BiscuitException If the command cannot be executed due to invalid
     *                          user input.
     */
    private void handleCommand(Command command, Scanner scanner) throws BiscuitException {
        switch (command) {
        case LIST:
            listTasks();
            break;
        case ADD:
            addTask(scanner);
            break;
        case MARK:
            markTask(scanner);
            break;
        case UNMARK:
            unmarkTask(scanner);
            break;
        case DELETE:
            deleteTask(scanner);
            break;
        default:
            break;
        }
    }

    private void listTasks() {
        if (tasks.isEmpty()) {
            ui.showNoTasks();
            return;
        }
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + ". " + tasks.get(i));
        }
    }

    /**
     * Prompts the user for a task type and delegates to the corresponding
     * add-method.
     *
     * @param scanner Scanner used to read user input.
     * @throws BiscuitException If the user provides an invalid task type or invalid
     *                          task fields.
     */
    private void addTask(Scanner scanner) throws BiscuitException {
        System.out.println("    Which type of task would you like to add?");
        System.out.println("    The types are: todo, event, deadline");

        String type = Parser.requireNonEmpty(ui.readCommand(scanner), "Task type cannot be empty.").toLowerCase();

        switch (type) {
        case "todo":
            addTodo(scanner);
            break;
        case "event":
            addEvent(scanner);
            break;
        case "deadline":
            addDeadline(scanner);
            break;
        default:
            throw new BiscuitException("Not a valid task type. Use: todo, event, deadline");
        }
    }

    private void addTodo(Scanner scanner) throws BiscuitException {
        System.out.println("    Enter todo description:");
        String description = Parser.requireNonEmpty(ui.readCommand(scanner), "Description cannot be empty.");

        Todo todo = new Todo(description);
        tasks.add(todo);
        storage.save(tasks.asList());
        ui.showAdded(todo);
    }

    private void addDeadline(Scanner scanner) throws BiscuitException {
        System.out.println("    Enter deadline description:");
        String description = Parser.requireNonEmpty(ui.readCommand(scanner), "Description cannot be empty.");

        System.out.println("    When is the deadline (YYYY-MM-DD):");
        String byRaw = Parser.requireNonEmpty(ui.readCommand(scanner), "Deadline date cannot be empty.");
        LocalDate by = Parser.parseDeadlineDate(byRaw);

        Deadline deadline = new Deadline(description, by);
        tasks.add(deadline);
        storage.save(tasks.asList());
        ui.showAdded(deadline);
    }

    private void addEvent(Scanner scanner) throws BiscuitException {
        System.out.println("    Enter event description:");
        String description = Parser.requireNonEmpty(ui.readCommand(scanner), "Description cannot be empty.");

        System.out.println("    When is the event from (YYYY-MM-DD HH:mm):");
        String fromRaw = Parser.requireNonEmpty(ui.readCommand(scanner), "Event start time cannot be empty.");

        System.out.println("    When is the event until (YYYY-MM-DD HH:mm):");
        String toRaw = Parser.requireNonEmpty(ui.readCommand(scanner), "Event end time cannot be empty.");

        LocalDateTime from = Parser.parseEventDateTime(fromRaw, "event start");
        LocalDateTime to = Parser.parseEventDateTime(toRaw, "event end");
        if (to.isBefore(from)) {
            throw new BiscuitException("Event end must be after the event start.");
        }

        Event event = new Event(description, from, to);
        tasks.add(event);
        storage.save(tasks.asList());
        ui.showAdded(event);
    }

    private void markTask(Scanner scanner) throws BiscuitException {
        System.out.println("    Which task would you like to mark done?");
        int index = Parser.parseIndex(ui.readCommand(scanner), tasks, "mark");

        Task task = tasks.get(index - 1);
        task.mark();
        storage.save(tasks.asList());
        ui.showMarked(task);
    }

    private void unmarkTask(Scanner scanner) throws BiscuitException {
        System.out.println("    Which task would you like to unmark?");
        int index = Parser.parseIndex(ui.readCommand(scanner), tasks, "unmark");

        Task task = tasks.get(index - 1);
        task.unmark();
        storage.save(tasks.asList());
        ui.showUnmarked(task);
    }

    private void deleteTask(Scanner scanner) throws BiscuitException {
        System.out.println("    Which task would you like to delete?");
        int index = Parser.parseIndex(ui.readCommand(scanner), tasks, "delete");

        Task removed = tasks.remove(index - 1);
        storage.save(tasks.asList());
        ui.showDeleted(removed);
    }
}
