import java.util.Scanner;
import java.util.ArrayList;
public class Biscuit {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Task> arr = new ArrayList<>();

        System.out.println("___________________________________");
        System.out.println("Hello! I'm Biscuit");
        System.out.println("What can I do for you?");
        System.out.println("___________________________________");
        String input = sc.nextLine();
        while(!input.toLowerCase().equals("bye")) {
            System.out.println("    ___________________________________");
            if(input.toLowerCase().equals("list")) {
                for (int i = 0; i < arr.size(); i++) {
                    System.out.println("     " + (i+1) + ". " + arr.get(i));
                }
            } else if(input.toLowerCase().equals("mark")) {
                System.out.println("Which task would you like to mark done?");
                int index = Integer.parseInt(sc.nextLine());
                Task task = arr.get(index - 1);
                task.mark();
                System.out.println("Ok! The task below is marked done");
                System.out.println(task);
                
            } else if(input.toLowerCase().equals("unmark")) {
                System.out.println("Which task would you like to unmark?");
                int index = Integer.parseInt(sc.nextLine());
                Task task = arr.get(index - 1);
                task.unmark();
                System.out.println("Ok! The task below is marked undone");
                System.out.println(task);
                
            } else {
                Task task = new Task(input);
                arr.add(task);
                System.out.println("    added: " + task);
            } 

            System.out.println("    ___________________________________");
            input = sc.nextLine();
        }
        System.out.println("    Bye. Hope to see you again soon!");
        System.out.println("    ___________________________________");
    }
}
