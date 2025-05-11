import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map;

public class GameController {
    private final GameState gameState;
    private final GameView view;
    private final MovieGraph movieGraph;
    private final Autocomplete autocomplete;
    private Timer turnTimer;

    public GameController(Player player1, Player player2, MovieGraph movieGraph) {
        this.gameState = new GameState(player1, player2);
        this.view = new GameView();
        this.movieGraph = movieGraph;
        this.autocomplete = new Autocomplete();

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
        }, 30000); // 30 seconds
    }

    private void handleTimeout() {
        Player loser = gameState.getCurrentPlayer();
        Player winner = (loser == gameState.getPlayer1()) ?
                gameState.getPlayer2() : gameState.getPlayer1();

        view.announceWinner(winner);
        System.exit(0);
    }

    public void processInput(String input) {
        Movie movie = movieGraph.getMovieByTitle(input);

        if (movie == null) {
            view.showError("Movie not found!");
            return;
        }

        if (gameState.getCurrentPlayer().hasUsedMovie(movie)) {
            view.showError("Movie already used!");
            return;
        }

        Movie lastMovie = gameState.getMovieHistory().peekFirst();
        if (lastMovie != null && !movieGraph.areConnected(lastMovie, movie)) {
            view.showError("Movies are not connected!");
            return;
        }

        String connectionKey = findConnectionKey(lastMovie, movie);
        if (!gameState.isConnectionAvailable(connectionKey)) {
            view.showError("Connection used too many times!");
            return;
        }

        gameState.useConnection(connectionKey);
        gameState.getCurrentPlayer().addUsedMovie(movie);
        gameState.addMovieToHistory(movie);

        if (gameState.getCurrentPlayer().hasWon(movie)) {
            view.announceWinner(gameState.getCurrentPlayer());
            System.exit(0);
        }

        gameState.switchPlayer();
        view.displayBoard(gameState);
        startTurnTimer();
    }

    private String findConnectionKey(Movie m1, Movie m2) {
        for (String actor : m1.getActors()) {
            if (m2.getActors().contains(actor)) {
                return "actor:" + actor.toLowerCase();
            }
        }

        for (String director : m1.getDirectors()) {
            if (m2.getDirectors().contains(director)) {
                return "director:" + director.toLowerCase();
            }
        }

        for (String writer : m1.getWriters()) {
            if (m2.getWriters().contains(writer)) {
                return "writer:" + writer.toLowerCase();
            }
        }

        for (String cinematographer : m1.getCinematographers()) {
            if (m2.getCinematographers().contains(cinematographer)) {
                return "cinematographer:" + cinematographer.toLowerCase();
            }
        }

        for (String composer : m1.getComposers()) {
            if (m2.getComposers().contains(composer)) {
                return "composer:" + composer.toLowerCase();
            }
        }

        return "unknown";
    }

    public List<Movie> getSuggestions(String prefix) {
        return autocomplete.suggestMovies(prefix);
    }

    public GameView getView() {
        return view;
    }
}