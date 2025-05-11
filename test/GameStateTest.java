import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class GameStateTest {
    private Player p1;
    private Player p2;
    private GameState gameState;
    private Movie testMovie;

    @Before
    public void setUp() {
        Map<String, Integer> wc = new HashMap<>();
        wc.put("Action", 3);

        p1 = new Player("Alice", wc);
        p2 = new Player("Bob", wc);
        gameState = new GameState(p1, p2);

        testMovie = new Movie(
            "Mad Max: Fury Road",
            2015,
            Arrays.asList("Action"),
            Arrays.asList("George Miller"),
            Arrays.asList("Tom Hardy"),
            Arrays.asList("George Miller"),
            Arrays.asList("John Seale"),
            Arrays.asList("Junkie XL")
        );
    }

    @Test
    public void testInitialCurrentPlayer() {
        assertEquals(p1, gameState.getCurrentPlayer());
    }

    @Test
    public void testSwitchPlayer() {
        gameState.switchPlayer();
        assertEquals(p2, gameState.getCurrentPlayer());
    }

    @Test
    public void testAddMovieToHistoryAndRoundCount() {
        gameState.addMovieToHistory(testMovie);
        assertEquals(1, gameState.getRoundCount());
        assertEquals(testMovie, gameState.getMovieHistory().peekFirst());
    }

    @Test
    public void testHistoryLimitToFive() {
        for (int i = 0; i < 6; i++) {
            Movie m = new Movie(
                "Movie " + i,
                2000 + i,
                List.of("Genre"),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
            );
            gameState.addMovieToHistory(m);
        }
        assertEquals(5, gameState.getMovieHistory().size());
    }

    @Test
    public void testConnectionAvailability() {
        String key = "actor:tom hardy";
        assertTrue(gameState.isConnectionAvailable(key));
        gameState.useConnection(key);
        gameState.useConnection(key);
        gameState.useConnection(key);
        assertFalse(gameState.isConnectionAvailable(key));
    }
}
