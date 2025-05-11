import java.util.List;

public class InputHandler {
    private final GameController controller;
    private final MovieGraph movieGraph;

    public InputHandler(GameController controller, MovieGraph movieGraph) {
        this.controller = controller;
        this.movieGraph = movieGraph;
    }

    public void handleInput(String input) {
        if (input.startsWith(":")) {
            handleCommand(input.substring(1));
        } else {
            controller.processInput(input);
        }
    }

    private void handleCommand(String command) {
        String[] parts = command.split(" ");
        switch (parts[0].toLowerCase()) {
            case "suggest":
                if (parts.length > 1) {
                    List<Movie> suggestions = controller.getSuggestions(parts[1]);
                    displaySuggestions(suggestions);
                }
                break;
            case "help":
                displayHelp();
                break;
            case "quit":
                System.exit(0);
                break;
            default:
                controller.getView().showError("Unknown command");
        }
    }

    private void displaySuggestions(List<Movie> suggestions) {
        System.out.println("Suggestions:");
        for (int i = 0; i < Math.min(suggestions.size(), 5); i++) {
            System.out.println("- " + suggestions.get(i));
        }
    }

    private void displayHelp() {
        System.out.println("Available commands:");
        System.out.println(":suggest <prefix> - Show movie suggestions");
        System.out.println(":help - Show this help message");
        System.out.println(":quit - Exit the game");
    }
}