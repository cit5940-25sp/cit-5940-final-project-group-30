import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Player {
    private final String name;
    private final Map<String, Integer> winCondition;
    private final Map<String, Integer> progress = new HashMap<>();
    private final Set<Movie> usedMovies = new HashSet<>();

    public Player(String name, Map<String, Integer> winCondition) {
        this.name = name;
        this.winCondition = winCondition;
    }

    public boolean hasWon() {
        for (Map.Entry<String, Integer> entry : winCondition.entrySet()) {
            String genre = entry.getKey();
            int required = entry.getValue();

            int count = 0;
            for (Movie movie : usedMovies) {
                if (movie.getGenres().contains(genre)) {
                    count++;
                }
            }

            if (count >= required) {
                return true;
            }
        }
        return false;
    }


    public void addUsedMovie(Movie movie) {
        usedMovies.add(movie);
    }

    public boolean hasUsedMovie(Movie movie) {
        return usedMovies.contains(movie);
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getWinCondition() {
        return winCondition;
    }
}
