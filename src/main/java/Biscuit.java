import java.util.Scanner;
public class Biscuit {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("___________________________________");
        System.out.println("Hello! I'm Biscuit");
        System.out.println("What can I do for you?");
        System.out.println("___________________________________");
        String hi = sc.nextLine();
        while(!hi.toLowerCase().equals("bye")) {
            System.out.println("    ___________________________________");
            System.out.println("    " + hi);
            System.out.println("    ___________________________________");
            hi = sc.nextLine();
        }
        System.out.println("        Bye. Hope to see you again soon!");
        System.out.println("    ___________________________________");
    }
}
