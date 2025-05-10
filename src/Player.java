import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Player {
    private final String name;
    private final Map<String, Integer> winCondition;
    private final Set<Movie> usedMovies = new HashSet<>();

    public Player(String name, Map<String, Integer> winCondition) {
        this.name = name;
        this.winCondition = winCondition;
    }

    public boolean hasWon(Movie movie) {
        for (String genre : movie.getGenres()) {
            if (winCondition.containsKey(genre)) {
                winCondition.put(genre, winCondition.get(genre) - 1);
                if (winCondition.get(genre) <= 0) {
                    return true;
                }
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

    public String getName() { return name; }
    public Map<String, Integer> getWinCondition() { return winCondition; }
}