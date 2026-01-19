import java.util.Scanner;
import java.util.ArrayList;
public class Biscuit {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<String> arr = new ArrayList<>();

        System.out.println("___________________________________");
        System.out.println("Hello! I'm Biscuit");
        System.out.println("What can I do for you?");
        System.out.println("___________________________________");
        String hi = sc.nextLine();
        while(!hi.toLowerCase().equals("bye")) {
            System.out.println("    ___________________________________");
            if(hi.toLowerCase().equals("list")) {
                for (int i = 0; i < arr.size(); i++) {
                    System.out.println("     " + i + ". " + arr.get(i));
                }
            } else {
                arr.add(hi);
                System.out.println("    added: " + hi);
            }
            System.out.println("    ___________________________________");
            hi = sc.nextLine();
        }
        System.out.println("        Bye. Hope to see you again soon!");
        System.out.println("    ___________________________________");
    }
}
