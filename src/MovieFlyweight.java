import java.util.HashMap;
import java.util.Map;

public class MovieFlyweight {
    private static final Map<String, Person> personPool = new HashMap<>();

    public static Person getPerson(String name, String role) {
        String key = name + "|" + role;
        return personPool.computeIfAbsent(key, k -> new Person(name, role));
    }

    public static class Person {
        private final String name;
        private final String role;

        public Person(String name, String role) {
            this.name = name;
            this.role = role;
        }

        public String getName() { return name; }
        public String getRole() { return role; }
    }
}