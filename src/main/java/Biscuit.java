import java.util.Scanner;
import java.util.ArrayList;
public class Biscuit {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Task> arr = new ArrayList<>();

        System.out.println("    ___________________________________");
        System.out.println("    Hello! I'm Biscuit");
        System.out.println("    What can I do for you?");
        System.out.println("    These are the commands that are available: add, list, mark, unmark, bye");
        System.out.println("    ___________________________________");

        String input = sc.nextLine();

        while (!input.toLowerCase().equals("bye")) {
            System.out.println("    ___________________________________");

            if (input.toLowerCase().equals("list")) {
                list(sc, arr);

            } else if (input.toLowerCase().equals("mark")) {
                mark(sc, arr);
                
            } else if (input.toLowerCase().equals("unmark")) {
                unmark(sc, arr);

            } else if (input.toLowerCase().equals("add")) {
                add(sc, arr);
                
            } else {
                System.out.println("    Not valid");
            }

            System.out.println("    ___________________________________");
            System.out.println("    add, list, mark, unmark, bye");
            input = sc.nextLine();
        }

        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println("    ___________________________________");
    }

    public static void add(Scanner sc, ArrayList<Task> arr) {
        System.out.println("    Which type of task would you like to add?");
        System.out.println("    The types are: todo, event, deadline");
        String type = sc.nextLine();
        if (type.toLowerCase().equals("todo")) {
            System.out.println("    Enter todo description: ");
            String description = sc.nextLine();
            Todo todo = new Todo(description);
            arr.add(todo);
            System.out.println("    added: " + todo);
        
        } else if (type.toLowerCase().equals("event")) {
            System.out.println("    Enter event description: ");
            String description = sc.nextLine();
            System.out.println("    When is the event from: ");
            String from = sc.nextLine();
            System.out.println("    When is the event until: ");
            String to = sc.nextLine();
            Event event = new Event(description, from, to);
            System.out.println("    added: " + event);
            arr.add(event);

        } else if (type.toLowerCase().equals("deadline")) {
            System.out.println("    Enter deadline description: ");
            String description = sc.nextLine();
            System.out.println("    When is the deadline: ");
            String by = sc.nextLine();
            Deadline deadline = new Deadline(description, by);
            arr.add(deadline);
            System.out.println("    added: " + deadline);

        } else {
            System.out.println("    Not a valid type");
        }
    }

    public static void list(Scanner sc, ArrayList<Task> arr) {
        for (int i = 0; i < arr.size(); i++) {
            System.out.println("     " + (i+1) + ". " + arr.get(i));
        }
    }

    public static void mark(Scanner sc, ArrayList<Task> arr) {
        System.out.println("    Which task would you like to mark done?");
        int index = Integer.parseInt(sc.nextLine());
        Task task = arr.get(index - 1);
        task.mark();
        System.out.println("    Ok! The task below is marked done");
        System.out.println(task);
    }

    public static void unmark(Scanner sc, ArrayList<Task> arr) {
        System.out.println("    Which task would you like to unmark?");
        int index = Integer.parseInt(sc.nextLine());
        Task task = arr.get(index - 1);
        task.unmark();
        System.out.println("    Ok! The task below is marked undone");
        System.out.println(task);
    }
}
