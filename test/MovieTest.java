import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class MovieTest {

    @Test
    public void testMovieFields() {
        // genres remain simple strings
        List<String> genres = List.of("Action", "Sci-Fi");

        // all cast & crew now come from the Flyweight factory
        List<MovieFlyweight.Person> directors = List.of(
                MovieFlyweight.getPerson("Christopher Nolan", "director")
        );
        List<MovieFlyweight.Person> actors = List.of(
                MovieFlyweight.getPerson("Leonardo DiCaprio", "actor")
        );
        List<MovieFlyweight.Person> writers = List.of(
                MovieFlyweight.getPerson("Jonathan Nolan", "writer")
        );
        List<MovieFlyweight.Person> cinematographers = List.of(
                MovieFlyweight.getPerson("Wally Pfister", "cinematographer")
        );
        List<MovieFlyweight.Person> composers = List.of(
                MovieFlyweight.getPerson("Hans Zimmer", "composer")
        );

        Movie movie = new Movie(
                "Inception",
                2010,
                genres,
                directors,
                actors,
                writers,
                cinematographers,
                composers
        );

        // basic fields
        assertEquals("Inception", movie.getTitle());
        assertEquals(2010, movie.getYear());
        assertEquals(genres, movie.getGenres());

        // now check each Person list by name
        assertEquals(1, movie.getDirectors().size());
        assertEquals("Christopher Nolan",
                movie.getDirectors().get(0).getName());

        assertEquals(1, movie.getActors().size());
        assertEquals("Leonardo DiCaprio",
                movie.getActors().get(0).getName());

        assertEquals(1, movie.getWriters().size());
        assertEquals("Jonathan Nolan",
                movie.getWriters().get(0).getName());

        assertEquals(1, movie.getCinematographers().size());
        assertEquals("Wally Pfister",
                movie.getCinematographers().get(0).getName());

        assertEquals(1, movie.getComposers().size());
        assertEquals("Hans Zimmer",
                movie.getComposers().get(0).getName());

        // toString
        assertEquals("Inception (2010)", movie.toString());
    }
}
