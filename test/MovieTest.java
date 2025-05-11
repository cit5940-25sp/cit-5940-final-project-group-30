import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

public class MovieTest {

    @Test
    public void testMovieFields() {
        Movie movie = new Movie(
            "Inception",
            2010,
            Arrays.asList("Action", "Sci-Fi"),
            Arrays.asList("Christopher Nolan"),
            Arrays.asList("Leonardo DiCaprio"),
            Arrays.asList("Jonathan Nolan"),
            Arrays.asList("Wally Pfister"),
            Arrays.asList("Hans Zimmer")
        );

        assertEquals("Inception", movie.getTitle());
        assertEquals(2010, movie.getYear());
        assertTrue(movie.getGenres().contains("Action"));
        assertTrue(movie.getDirectors().contains("Christopher Nolan"));
        assertTrue(movie.getActors().contains("Leonardo DiCaprio"));
        assertTrue(movie.getWriters().contains("Jonathan Nolan"));
        assertTrue(movie.getCinematographers().contains("Wally Pfister"));
        assertTrue(movie.getComposers().contains("Hans Zimmer"));
        assertEquals("Inception (2010)", movie.toString());
    }
}
