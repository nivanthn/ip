package biscuit;

import java.util.Scanner;

public class Ui {

    private static final String HORIZONTAL_LINE = "    ___________________________________";
    private static final String COMMANDS_PROMPT =
            "    These are the commands that are available: add, list, mark, unmark, delete, bye";

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

    public String readCommand(Scanner scanner) {
        return scanner.nextLine().trim();
    }

    public void showError(String message) {
        System.out.println("    " + message);
    }

    public void showGoodbye() {
        System.out.println("    Bye. Hope to see you again soon!");
        showLine();
    }

    public void showNoTasks() {
        System.out.println("    No tasks yet.");
    }

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