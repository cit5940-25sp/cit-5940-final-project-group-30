import java.util.HashMap;
import java.util.Map;

public class MovieFlyweight {
    private static Map<String, String> actorPool = new HashMap<>();
    private static Map<String, String> directorPool = new HashMap<>();
    // Add other pools (composer, writer, cinematographer)

    public static String getActor(String name) {
        return actorPool.computeIfAbsent(name, k -> k);
    }

    public static String getDirector(String name) {
        return directorPool.computeIfAbsent(name, k -> k);
    }
    // Add other getters
}