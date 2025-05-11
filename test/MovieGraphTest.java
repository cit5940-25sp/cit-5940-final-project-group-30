import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MovieGraphTest {

    @Test
    void twoMoviesWithSharedPersonAreConnected() {
        // setup two movies sharing the same actor flyweight
        var actor = MovieFlyweight.getPerson("Shared Star", "actor");
        var genres = List.of("Drama");

        Movie m1 = new Movie("First", 2001, genres,
                List.of(), List.of(actor),
                List.of(), List.of(), List.of());
        Movie m2 = new Movie("Second", 2002, genres,
                List.of(), List.of(actor),
                List.of(), List.of(), List.of());

        MovieGraph g = new MovieGraph();
        g.addMovie(m1);
        g.addMovie(m2);
        g.buildConnections();

        assertTrue(g.areConnected(m1, m2),
                "Movies sharing an actor should be connected");
        assertEquals(1, g.getNeighbors(m1).size());
        assertEquals(m2, g.getNeighbors(m1).get(0));
    }

    @Test
    void distinctMoviesWithoutSharedPersonAreNotConnected() {
        var a1 = MovieFlyweight.getPerson("Star A", "actor");
        var a2 = MovieFlyweight.getPerson("Star B", "actor");
        var genres = List.of("Action");

        Movie m1 = new Movie("A", 1999, genres,
                List.of(), List.of(a1),
                List.of(), List.of(), List.of());
        Movie m2 = new Movie("B", 2000, genres,
                List.of(), List.of(a2),
                List.of(), List.of(), List.of());

        MovieGraph g = new MovieGraph();
        g.addMovie(m1);
        g.addMovie(m2);
        g.buildConnections();

        assertFalse(g.areConnected(m1, m2),
                "Movies with no shared person should not be connected");
        assertTrue(g.getNeighbors(m1).isEmpty());
    }
}
