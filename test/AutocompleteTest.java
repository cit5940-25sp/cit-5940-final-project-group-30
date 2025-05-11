import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class AutocompleteTest {

    private Autocomplete ac;
    private List<String> genres = List.of("Genre");

    private Movie mA, mB, mC;

    @BeforeEach
    void setUp() {
        ac = new Autocomplete();
        mA = new Movie("Alpha", 2000, genres,
                List.of(), List.of(), List.of(), List.of(), List.of());
        mB = new Movie("Alpine", 2010, genres,
                List.of(), List.of(), List.of(), List.of(), List.of());
        mC = new Movie("Beta", 2005, genres,
                List.of(), List.of(), List.of(), List.of(), List.of());

        ac.insert(mA);
        ac.insert(mB);
        ac.insert(mC);
    }

    @Test
    void suggestPrefixAlReturnsAlphaAndAlpineOrderedByYearDesc() {
        List<Movie> results = ac.suggestMovies("Al");
        assertEquals(2, results.size());
        assertEquals(mB, results.get(0), "Newest match first");
        assertEquals(mA, results.get(1));
    }

    @Test
    void suggestUnknownPrefixReturnsEmptyList() {
        List<Movie> results = ac.suggestMovies("Zoo");
        assertTrue(results.isEmpty());
    }
}
