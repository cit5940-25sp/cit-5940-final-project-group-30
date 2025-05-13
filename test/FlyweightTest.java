import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FlyweightTest {
    @Test
    void testGetPerson() {
        MovieFlyweight.Person p1 = MovieFlyweight.getPerson("Tom Hanks", "actor");
        MovieFlyweight.Person p2 = MovieFlyweight.getPerson("Tom Hanks", "actor");
        MovieFlyweight.Person p3 = MovieFlyweight.getPerson("Tom Hanks", "director");
        MovieFlyweight.Person p4 = MovieFlyweight.getPerson("Steven Spielberg", "director");

        assertSame(p1, p2);
        assertNotSame(p1, p3);
        assertNotSame(p1, p4);
        assertNotSame(p3, p4);
    }

    @Test
    void testPersonEquality() {
        MovieFlyweight.Person p1 = MovieFlyweight.getPerson("Tom Hanks", "actor");
        MovieFlyweight.Person p2 = MovieFlyweight.getPerson("tom hanks", "ACTOR");
        MovieFlyweight.Person p3 = MovieFlyweight.getPerson("Tom Hanks", "director");

        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
    }

    @Test
    void testPersonProperties() {
        MovieFlyweight.Person p = MovieFlyweight.getPerson("Quentin Tarantino", "director");
        assertEquals("Quentin Tarantino", p.getName());
        assertEquals("director", p.getRole());
    }
}
