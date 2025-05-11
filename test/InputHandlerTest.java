import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

public class InputHandlerTest {

    private ByteArrayOutputStream out;
    private InputHandler handler;

    // dummy controller that returns a fixed suggestion list
    private static class DummyController extends GameController {
        public DummyController() {
            super(new Player("P1", Map.of()), new Player("P2", Map.of()), new MovieGraph());
        }
        @Override
        public List<Movie> getSuggestions(String prefix) {
            return List.of(new Movie("Ghost", 1990,
                    List.of("Horror"),
                    List.of(), List.of(), List.of(), List.of(), List.of()));
        }
    }

    @BeforeEach
    void setUp() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        handler = new InputHandler(new DummyController(), new MovieGraph());
    }

    @Test
    void suggestCommandPrintsSuggestions() {
        handler.handleInput(":suggest Gho");
        String printed = out.toString();
        assertTrue(printed.contains("Suggestions:"));
        assertTrue(printed.contains("Ghost"), "Should list the dummy movie");
    }
}
