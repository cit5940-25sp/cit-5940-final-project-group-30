import javax.swing.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GameController {
    private final GameState gameState;
    private final GameView view;
    private final MovieGraph movieGraph;
    private final Autocomplete autocomplete;
    private Timer turnTimer;

    public GameController(Player player1, Player player2, MovieGraph movieGraph, GameView view) {
        this.gameState   = new GameState(player1, player2);
        this.view        = view;
        this.movieGraph  = movieGraph;
        this.autocomplete = new Autocomplete();

        // preload autocomplete with every movie
        for (Movie movie : movieGraph.getAllMovies()) {
            autocomplete.insert(movie);
        }
    }

    public void startGame(Movie startingMovie) {
        gameState.addMovieToHistory(startingMovie);
        view.displayBoard(gameState);
        startTurnTimer();
    }

    private void startTurnTimer() {
        if (turnTimer != null) {
            turnTimer.cancel();
        }
        turnTimer = new Timer();
        turnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handleTimeout();
            }
        }, 30_000); // 30 seconds per turn
    }

    private void handleTimeout() {
        Player loser = gameState.getCurrentPlayer();
        Player winner = (loser == gameState.getPlayer1())
                ? gameState.getPlayer2()
                : gameState.getPlayer1();

        view.announceWinner(winner);
    }

    public boolean processInput(String input) {
        // 1) Lookup movie by title
        Movie movie = movieGraph.getMovieByTitle(input);
        if (movie == null) {
            SwingUtilities.invokeLater(() -> view.showError("Movie not found!"));
            return false;
        }

        // 2) Reject if already used by this player
        if (gameState.getCurrentPlayer().hasUsedMovie(movie)) {
            SwingUtilities.invokeLater(() -> view.showError("Movie already used!"));
            return false;
        }

        // 3) Determine connection key (or skip on first move)
        Movie lastMovie = gameState.getMovieHistory().peekFirst();
        String connectionKey = "start";  // default for the very first turn
        if (lastMovie != null) {
            // a) check adjacency
            if (!movieGraph.areConnected(lastMovie, movie)) {
                SwingUtilities.invokeLater(() -> view.showError("Movies are not connected!"));
                return false;
            }
            // b) find which person links them
            connectionKey = findConnectionKey(lastMovie, movie);
            // c) enforce max-3 usage per connection
            if (!gameState.isConnectionAvailable(connectionKey)) {
                SwingUtilities.invokeLater(() -> view.showError("Connection used too many times!"));
                return false;
            }
        }

        // 4) Commit the move
        gameState.useConnection(connectionKey);
        gameState.getCurrentPlayer().addUsedMovie(movie);
        gameState.addMovieToHistory(movie);

        // 5) Win check
        if (gameState.getCurrentPlayer().hasWon(movie)) {
            SwingUtilities.invokeLater(() -> view.announceWinner(gameState.getCurrentPlayer()));
            return true;
        }

        // 6) Switch turns
        gameState.switchPlayer();
        view.displayBoard(gameState);
        startTurnTimer();
        return true;
    }

    /**
     * Inspect both movies' Person lists and return a key
     * like "actor:tom hanks" to track usage.
     */
    private String findConnectionKey(Movie m1, Movie m2) {
        for (MovieFlyweight.Person actor : m1.getActors()) {
            if (m2.getActors().contains(actor)) {
                return "actor:" + actor.getName().toLowerCase();
            }
        }
        for (MovieFlyweight.Person director : m1.getDirectors()) {
            if (m2.getDirectors().contains(director)) {
                return "director:" + director.getName().toLowerCase();
            }
        }
        for (MovieFlyweight.Person writer : m1.getWriters()) {
            if (m2.getWriters().contains(writer)) {
                return "writer:" + writer.getName().toLowerCase();
            }
        }
        for (MovieFlyweight.Person cinematographer : m1.getCinematographers()) {
            if (m2.getCinematographers().contains(cinematographer)) {
                return "cinematographer:" + cinematographer.getName().toLowerCase();
            }
        }
        for (MovieFlyweight.Person composer : m1.getComposers()) {
            if (m2.getComposers().contains(composer)) {
                return "composer:" + composer.getName().toLowerCase();
            }
        }
        return "unknown";
    }

    /** For the “:suggest” command in InputHandler */
    public List<Movie> getSuggestions(String prefix) {
        return autocomplete.suggestMovies(prefix);
    }

    /** Expose the view so InputHandler can show errors or prompt again */
    public GameView getView() {
        return view;
    }

    public GameState getGameState() {
        return gameState;
    }
}
