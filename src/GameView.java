import java.util.Deque;

public class GameView {
    private final TextUI textUI;

    public GameView() {
        this.textUI = new TextUI();
    }

    public void displayBoard(GameState state) {
        textUI.clearScreen();

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

        textUI.displayMessage("\nLast 5 Movies:");
        Deque<Movie> history = state.getMovieHistory();
        Movie previous = null;
        for (Movie movie : history) {
            String conn = (previous != null)
                    ? findConnection(previous, movie)
                    : "Starting Movie";
            textUI.displayMessage("- " + movie + " (" + conn + ")");
            previous = movie;
        }

        textUI.displayMessage("\nEnter a movie title:");
    }

    private String findConnection(Movie m1, Movie m2) {
        for (MovieFlyweight.Person actor : m1.getActors()) {
            if (m2.getActors().contains(actor)) {
                return "Actor: " + actor.getName();
            }
        }
        for (MovieFlyweight.Person director : m1.getDirectors()) {
            if (m2.getDirectors().contains(director)) {
                return "Director: " + director.getName();
            }
        }
        for (MovieFlyweight.Person writer : m1.getWriters()) {
            if (m2.getWriters().contains(writer)) {
                return "Writer: " + writer.getName();
            }
        }
        for (MovieFlyweight.Person cinematographer : m1.getCinematographers()) {
            if (m2.getCinematographers().contains(cinematographer)) {
                return "Cinematographer: " + cinematographer.getName();
            }
        }
        for (MovieFlyweight.Person composer : m1.getComposers()) {
            if (m2.getComposers().contains(composer)) {
                return "Composer: " + composer.getName();
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
    }

    public String getInput() {
        return textUI.getInput();
    }
}