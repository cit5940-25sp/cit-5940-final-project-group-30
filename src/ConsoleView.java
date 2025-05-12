import java.util.Deque;

public class ConsoleView {
    private final TextUI textUI = new TextUI();

    /**
     * Render the whole board: players, round, last 5 movies + how they're connected.
     */
    public void displayBoard(GameState state) {
        textUI.clearScreen();

        // Header: Player names and win conditions
        textUI.displayMessage("Player 1: "
                + state.getPlayer1().getName()
                + " | Win Condition: "
                + state.getPlayer1().getWinCondition()
        );
        textUI.displayMessage("Player 2: "
                + state.getPlayer2().getName()
                + " | Win Condition: "
                + state.getPlayer2().getWinCondition()
        );
        textUI.displayMessage("Current Player: "
                + state.getCurrentPlayer().getName()
        );
        textUI.displayMessage("Round: " + state.getRoundCount());

        // Movie history
        textUI.displayMessage("\nLast 5 Movies:");
        Deque<Movie> history = state.getMovieHistory();
        Movie previous = null;
        int round = state.getRoundCount();

        for (Movie movie : history) {
            String connection;
            if (previous == null && round == 1) {
                // Only the very first movie ever gets "Starting Movie"
                connection = "Starting Movie";
            } else if (previous != null) {
                // Compute actual connection
                connection = findConnection(previous, movie);
            } else {
                // In later rounds, first entry has no previous
                connection = "Connected";
            }

            // Print the movie and its connection
            textUI.displayMessage("- "
                    + movie.getTitle()
                    + " (" + movie.getYear() + ") "
                    + movie.getGenres()
                    + " → " + connection
            );

            previous = movie;
        }

        textUI.displayMessage("\nEnter a movie title:");
    }

    /**
     * Return a human-readable connection label, e.g. "Actor: Tom Hanks".
     */
    private String findConnection(Movie m1, Movie m2) {
        for (MovieFlyweight.Person p : m1.getActors()) {
            if (m2.getActors().contains(p)) {
                return "Actor: " + p.getName();
            }
        }
        for (MovieFlyweight.Person p : m1.getDirectors()) {
            if (m2.getDirectors().contains(p)) {
                return "Director: " + p.getName();
            }
        }
        for (MovieFlyweight.Person p : m1.getWriters()) {
            if (m2.getWriters().contains(p)) {
                return "Writer: " + p.getName();
            }
        }
        for (MovieFlyweight.Person p : m1.getCinematographers()) {
            if (m2.getCinematographers().contains(p)) {
                return "Cinematographer: " + p.getName();
            }
        }
        for (MovieFlyweight.Person p : m1.getComposers()) {
            if (m2.getComposers().contains(p)) {
                return "Composer: " + p.getName();
            }
        }
        return "Connected";  // fallback
    }

    public void showError(String error) {
        textUI.displayError(error);
    }

    public void announceWinner(Player winner) {
        textUI.clearScreen();
        textUI.displayMessage("Game Over!");
        textUI.displayMessage("Winner: " + winner.getName());
        textUI.displayMessage("Press Enter to exit...");
        // (no textUI.getInput() here)
        System.exit(0);
    }


    public String getInput() {
        return textUI.getInput();
    }
}