import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class GameState {
    private Player currentPlayer;
    private Player player1;
    private Player player2;
    private int roundCount = 0;
    private final Deque<Movie> movieHistory = new ArrayDeque<>(5);
    private final Map<String, Integer> usedConnections = new HashMap<>();

    public GameState(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public Player getPlayer1() {
        return player1;
    }
    public Player getPlayer2() {
        return player2;
    }
    public int getRoundCount() {
        return roundCount;
    }
    public Deque<Movie> getMovieHistory() {
        return movieHistory;
    }

    public void addMovieToHistory(Movie movie) {
        if (movieHistory.size() == 5) {
            movieHistory.removeLast();
        }
        movieHistory.addFirst(movie);
        roundCount++;
    }

    public boolean isConnectionAvailable(String connectionKey) {
        return usedConnections.getOrDefault(connectionKey, 0) < 3;
    }

    public void useConnection(String connectionKey) {
        usedConnections.put(connectionKey, usedConnections.getOrDefault(connectionKey, 0) + 1);
    }
}