import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class FlyweightTest {

    @Test
    public void sameNameAndRoleYieldsSameInstance() {
        var p1 = MovieFlyweight.getPerson("Tom Hanks", "actor");
        var p2 = MovieFlyweight.getPerson("Tom Hanks", "actor");
        assertSame(p1, p2, "Flyweight must return the same instance for identical name+role");
    }

    @Test
    public void differentRoleYieldsDifferentInstance() {
        var a1 = MovieFlyweight.getPerson("John Williams", "composer");
        var a2 = MovieFlyweight.getPerson("John Williams", "director");
        assertNotSame(a1, a2, "Same name but different role should produce distinct instances");
    }
}
