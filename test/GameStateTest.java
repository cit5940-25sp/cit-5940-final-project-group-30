import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Deque;
import java.util.List;
import java.util.Map;

public class GameStateTest {
    private GameState gameState;
    private Player player1, player2;
    private Movie movie1, movie2;

    @BeforeEach
    void setUp() {
        player1 = new Player("Player 1", Map.of("Drama", 2));
        player2 = new Player("Player 2", Map.of("Crime", 1));
        gameState = new GameState(player1, player2);

        movie1 = new Movie("Movie 1", 2000,
                List.of("Drama"),
                List.of(MovieFlyweight.getPerson("Director 1", "director")),
                List.of(MovieFlyweight.getPerson("Actor 1", "actor")));

        movie2 = new Movie("Movie 2", 2001,
                List.of("Crime"),
                List.of(MovieFlyweight.getPerson("Director 2", "director")),
                List.of(MovieFlyweight.getPerson("Actor 2", "actor")));
    }

    @Test
    void testInitialState() {
        assertEquals(player1, gameState.getCurrentPlayer());
        assertEquals(0, gameState.getRoundCount());
        assertTrue(gameState.getMovieHistory().isEmpty());
    }

    @Test
    void testSwitchPlayer() {
        gameState.switchPlayer();
        assertEquals(player2, gameState.getCurrentPlayer());
        gameState.switchPlayer();
        assertEquals(player1, gameState.getCurrentPlayer());
    }

    @Test
    void testAddMovieToHistory() {
        gameState.addMovieToHistory(movie1);
        assertEquals(1, gameState.getRoundCount());
        Deque<Movie> history = gameState.getMovieHistory();
        assertEquals(1, history.size());
        assertEquals(movie1, history.peekFirst());
    }

    @Test
    void testHistoryLimit() {
        for (int i = 0; i < 6; i++) {
            gameState.addMovieToHistory(new Movie("Movie " + i, 2000 + i,
                    List.of("Genre"),
                    List.of(MovieFlyweight.getPerson("Director", "director")),
                    List.of(MovieFlyweight.getPerson("Actor", "actor"))));
        }
        assertEquals(5, gameState.getMovieHistory().size());
    }
}
