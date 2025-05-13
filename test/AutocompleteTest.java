import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AutocompleteTest {
    private Autocomplete autocomplete;
    private Movie movie1, movie2, movie3;

    @BeforeEach
    void setUp() {
        autocomplete = new Autocomplete();

        movie1 = new Movie("The Shawshank Redemption", 1994,
                List.of("Drama"),
                List.of(MovieFlyweight.getPerson("Frank Darabont", "director")),
                List.of(MovieFlyweight.getPerson("Tim Robbins", "actor")));

        movie2 = new Movie("The Godfather", 1972,
                List.of("Crime", "Drama"),
                List.of(MovieFlyweight.getPerson("Francis Ford Coppola", "director")),
                List.of(MovieFlyweight.getPerson("Marlon Brando", "actor")));

        movie3 = new Movie("The Godfather: Part II", 1974,
                List.of("Crime", "Drama"),
                List.of(MovieFlyweight.getPerson("Francis Ford Coppola", "director")),
                List.of(MovieFlyweight.getPerson("Al Pacino", "actor")));

        autocomplete.insert(movie1);
        autocomplete.insert(movie2);
        autocomplete.insert(movie3);
    }

    @Test
    void testSuggestPartialMatch() {
        List<Movie> suggestions = autocomplete.suggestMovies("The God");
        assertEquals(2, suggestions.size(), "Partial match should return 2 movies");
        assertEquals(movie3, suggestions.get(0));
        assertEquals(movie2, suggestions.get(1));
    }

    @Test
    void testSuggestCaseInsensitive() {
        List<Movie> suggestions = autocomplete.suggestMovies("the gOd");
        assertEquals(2, suggestions.size());
    }

    @Test
    void testSuggestNoMatch() {
        List<Movie> suggestions = autocomplete.suggestMovies("Pulp Fiction");
        assertTrue(suggestions.isEmpty());
    }

    @Test
    void testSuggestEmptyPrefix() {
        List<Movie> suggestions = autocomplete.suggestMovies("");
        assertEquals(3, suggestions.size());
    }
}
