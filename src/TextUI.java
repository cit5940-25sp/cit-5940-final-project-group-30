import java.util.Scanner;

public class TextUI {
    private final Scanner scanner = new Scanner(System.in);

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayError(String error) {
        System.err.println("ERROR: " + error);
    }

    public String getInput() {
        System.out.print("> ");
        return scanner.nextLine();
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}