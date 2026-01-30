package biscuit;

import java.util.Scanner;

/**
 * Handles all interactions with the user (input prompts and output messages).
 * <p>
 * This class centralizes printing so that the rest of the codebase avoids
 * direct
 * user-facing output formatting.
 */
public class Ui {

    private static final String HORIZONTAL_LINE = "    ___________________________________";
    private static final String COMMANDS_PROMPT = 
            "    These are the commands that are available: add, list, mark, unmark, delete, bye";

    /**
     * Displays the welcome message and a list of available commands.
     */
    public void showWelcome() {
        showLine();
        System.out.println("    Hello! I'm Biscuit");
        System.out.println("    What can I do for you?");
        showCommands();
        showLine();
    }

    public void showLine() {
        System.out.println(HORIZONTAL_LINE);
    }

    public void showCommands() {
        System.out.println(COMMANDS_PROMPT);
    }

    /**
     * Reads a single line of input from the user.
     *
     * @param scanner Scanner connected to standard input.
     * @return Trimmed command string entered by the user.
     */
    public String readCommand(Scanner scanner) {
        return scanner.nextLine().trim();
    }

    /**
     * Displays an error message to the user.
     *
     * @param message Error message to display.
     */
    public void showError(String message) {
        System.out.println("    " + message);
    }

    /**
     * Displays the goodbye message and a separator line.
     */
    public void showGoodbye() {
        System.out.println("    Bye. Hope to see you again soon!");
        showLine();
    }

    /**
     * Informs the user that there are no tasks in the list.
     */
    public void showNoTasks() {
        System.out.println("    No tasks yet.");
    }

    /**
     * Displays a message confirming a task has been added.
     *
     * @param task The task that was added.
     */
    public void showAdded(Task task) {
        System.out.println("    added: " + task);
    }

    public void showMarked(Task task) {
        System.out.println("    Ok! The task below is marked done");
        System.out.println(task);
    }

    public void showUnmarked(Task task) {
        System.out.println("    Ok! The task below is marked undone");
        System.out.println(task);
    }

    public void showDeleted(Task task) {
        System.out.println("    Ok! I've deleted this task:");
        System.out.println("    " + task);
    }
}
