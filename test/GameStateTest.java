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
        // Win‐condition still uses a Map<String,Integer>
        Map<String, Integer> wc = new HashMap<>();
        wc.put("Action", 3);

        p1 = new Player("Alice", wc);
        p2 = new Player("Bob", wc);
        gameState = new GameState(p1, p2);

        // Prepare the Movie with Person flyweights instead of raw Strings
        List<String> genres = List.of("Action");

        List<MovieFlyweight.Person> directors = List.of(
                MovieFlyweight.getPerson("George Miller", "director")
        );
        List<MovieFlyweight.Person> actors = List.of(
                MovieFlyweight.getPerson("Tom Hardy", "actor")
        );
        List<MovieFlyweight.Person> writers = List.of(
                MovieFlyweight.getPerson("George Miller", "writer")
        );
        List<MovieFlyweight.Person> cinematographers = List.of(
                MovieFlyweight.getPerson("John Seale", "cinematographer")
        );
        List<MovieFlyweight.Person> composers = List.of(
                MovieFlyweight.getPerson("Junkie XL", "composer")
        );

        testMovie = new Movie(
                "Mad Max: Fury Road",
                2015,
                genres,
                directors,
                actors,
                writers,
                cinematographers,
                composers
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
        // Add 6 movies with empty crew lists
        for (int i = 0; i < 6; i++) {
            Movie m = new Movie(
                    "Movie " + i,
                    2000 + i,
                    List.of("Genre"),
                    List.<MovieFlyweight.Person>of(),  // empty
                    List.<MovieFlyweight.Person>of(),
                    List.<MovieFlyweight.Person>of(),
                    List.<MovieFlyweight.Person>of(),
                    List.<MovieFlyweight.Person>of()
            );
            gameState.addMovieToHistory(m);
        }
        // Only five most recent should remain
        assertEquals(5, gameState.getMovieHistory().size());
    }

    @Test
    public void testConnectionAvailability() {
        String key = "actor:tom hardy";
        assertTrue(gameState.isConnectionAvailable(key));
        // use it three times
        gameState.useConnection(key);
        gameState.useConnection(key);
        gameState.useConnection(key);
        // fourth usage should be rejected
        assertFalse(gameState.isConnectionAvailable(key));
    }
}
