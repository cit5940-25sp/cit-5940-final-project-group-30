import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MovieGraphTest {
    private MovieGraph movieGraph;
    private Movie movie1, movie2, movie3;

    @BeforeEach
    void setUp() {
        movieGraph = new MovieGraph();

        movie1 = new Movie("The Shawshank Redemption", 1994,
                List.of("Drama"),
                List.of(MovieFlyweight.getPerson("Frank Darabont", "director")),
                List.of(MovieFlyweight.getPerson("Tim Robbins", "actor")));

        movie2 = new Movie("The Green Mile", 1999,
                List.of("Drama", "Fantasy"),
                List.of(MovieFlyweight.getPerson("Frank Darabont", "director")),
                List.of(MovieFlyweight.getPerson("Tom Hanks", "actor")));

        movie3 = new Movie("Forrest Gump", 1994,
                List.of("Drama", "Romance"),
                List.of(MovieFlyweight.getPerson("Robert Zemeckis", "director")),
                List.of(MovieFlyweight.getPerson("Tom Hanks", "actor")));

        movieGraph.addMovie(movie1);
        movieGraph.addMovie(movie2);
        movieGraph.addMovie(movie3);
        movieGraph.buildConnections();
    }

    @Test
    void testAddMovie() {
        assertEquals(3, movieGraph.getAllMovies().size());
    }

    @Test
    void testAreConnected() {
        // Connected by director
        assertTrue(movieGraph.areConnected(movie1, movie2));
        // Connected by actor
        assertTrue(movieGraph.areConnected(movie2, movie3));
        // Not connected
        assertFalse(movieGraph.areConnected(movie1, movie3));
    }

    @Test
    void testGetNeighbors() {
        List<Movie> neighbors = movieGraph.getNeighbors(movie2);
        assertEquals(2, neighbors.size());
        assertTrue(neighbors.contains(movie1));
        assertTrue(neighbors.contains(movie3));
    }

    @Test
    void testGetMovieByTitle() {
        assertEquals(movie1, movieGraph.getMovieByTitle("The Shawshank Redemption"));
        assertEquals(movie1, movieGraph.getMovieByTitle("the shawshank redemption"));
        assertNull(movieGraph.getMovieByTitle("Nonexistent Movie"));
    }

    @Test
    void testGetRandomMovie() {
        Movie randomMovie = movieGraph.getRandomMovie();
        assertNotNull(randomMovie);
        assertTrue(movieGraph.getAllMovies().contains(randomMovie));
    }
}
