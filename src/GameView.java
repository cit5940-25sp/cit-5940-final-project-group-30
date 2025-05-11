import java.util.Deque;

public class GameView {
    private final TextUI textUI;

    public GameView() {
        this.textUI = new TextUI();
    }

    public void displayBoard(GameState state) {
        textUI.clearScreen();

        textUI.displayMessage("Player 1: " + state.getPlayer1().getName() +
                " | Win Condition: " + state.getPlayer1().getWinCondition());
        textUI.displayMessage("Player 2: " + state.getPlayer2().getName() +
                " | Win Condition: " + state.getPlayer2().getWinCondition());
        textUI.displayMessage("Current Player: " + state.getCurrentPlayer().getName());
        textUI.displayMessage("Round: " + state.getRoundCount());

        textUI.displayMessage("\nLast 5 Movies:");
        Deque<Movie> history = state.getMovieHistory();
        Movie previous = null;
        for (Movie movie : history) {
            String connection = (previous != null) ?
                    findConnection(previous, movie) : "Starting Movie";
            textUI.displayMessage("- " + movie + " (" + connection + ")");
            previous = movie;
        }

        textUI.displayMessage("\nEnter a movie title:");
    }

    private String findConnection(Movie m1, Movie m2) {
        for (String actor : m1.getActors()) {
            if (m2.getActors().contains(actor)) {
                return "Actor: " + actor;
            }
        }

        for (String director : m1.getDirectors()) {
            if (m2.getDirectors().contains(director)) {
                return "Director: " + director;
            }
        }

        for (String writer : m1.getWriters()) {
            if (m2.getWriters().contains(writer)) {
                return "Writer: " + writer;
            }
        }

        for (String cinematographer : m1.getCinematographers()) {
            if (m2.getCinematographers().contains(cinematographer)) {
                return "Cinematographer: " + cinematographer;
            }
        }

        for (String composer : m1.getComposers()) {
            if (m2.getComposers().contains(composer)) {
                return "Composer: " + composer;
            }
        }

        return "Connected";
    }

    public void showError(String error) {
        textUI.displayError(error);
    }

    public void announceWinner(Player winner) {
        textUI.clearScreen();
        textUI.displayMessage("Game Over!");
        textUI.displayMessage("Winner: " + winner.getName());
        textUI.displayMessage("Press Enter to exit...");
        textUI.getInput();
    }

    public String getInput() {
        return textUI.getInput();
    }
}