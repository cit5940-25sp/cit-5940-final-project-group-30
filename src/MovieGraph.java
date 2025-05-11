import java.util.*;

public class MovieGraph {
    // Quick lookup: title (lowercase) → Movie
    private final Map<String, Movie> movieMap = new HashMap<>();
    // Map each Person → all Movies they appear in
    private final Map<MovieFlyweight.Person, List<Movie>> personToMovies = new HashMap<>();
    // Keep a list of all movies for random selection
    private final List<Movie> allMovies = new ArrayList<>();

    /**
     * Add a new movie node (no edges yet).
     */
    public void addMovie(Movie movie) {
        movieMap.put(movie.getTitle().toLowerCase(), movie);
        allMovies.add(movie);

        // index people by role
        for (MovieFlyweight.Person p : movie.getActors()) {
            personToMovies.computeIfAbsent(p, k -> new ArrayList<>()).add(movie);
        }
        for (MovieFlyweight.Person p : movie.getDirectors()) {
            personToMovies.computeIfAbsent(p, k -> new ArrayList<>()).add(movie);
        }
        for (MovieFlyweight.Person p : movie.getWriters()) {
            personToMovies.computeIfAbsent(p, k -> new ArrayList<>()).add(movie);
        }
        for (MovieFlyweight.Person p : movie.getCinematographers()) {
            personToMovies.computeIfAbsent(p, k -> new ArrayList<>()).add(movie);
        }
        for (MovieFlyweight.Person p : movie.getComposers()) {
            personToMovies.computeIfAbsent(p, k -> new ArrayList<>()).add(movie);
        }
    }

    /**
     * Check if two movies share an edge.
     */
    public boolean areConnected(Movie m1, Movie m2) {
        return getNeighbors(m1).contains(m2);
    }

    /**
     * Get all directly connected neighbors of a movie.
     */
    public List<Movie> getNeighbors(Movie movie) {
        Set<Movie> neighbors = new HashSet<>();

        for (MovieFlyweight.Person p : movie.getActors()) {
            List<Movie> list = personToMovies.getOrDefault(p, Collections.emptyList());
            for (Movie m : list) {
                if (!m.equals(movie)) neighbors.add(m);
            }
        }
        for (MovieFlyweight.Person p : movie.getDirectors()) {
            List<Movie> list = personToMovies.getOrDefault(p, Collections.emptyList());
            for (Movie m : list) {
                if (!m.equals(movie)) neighbors.add(m);
            }
        }
        for (MovieFlyweight.Person p : movie.getWriters()) {
            List<Movie> list = personToMovies.getOrDefault(p, Collections.emptyList());
            for (Movie m : list) {
                if (!m.equals(movie)) neighbors.add(m);
            }
        }
        for (MovieFlyweight.Person p : movie.getCinematographers()) {
            List<Movie> list = personToMovies.getOrDefault(p, Collections.emptyList());
            for (Movie m : list) {
                if (!m.equals(movie)) neighbors.add(m);
            }
        }
        for (MovieFlyweight.Person p : movie.getComposers()) {
            List<Movie> list = personToMovies.getOrDefault(p, Collections.emptyList());
            for (Movie m : list) {
                if (!m.equals(movie)) neighbors.add(m);
            }
        }

        return new ArrayList<>(neighbors);
    }

    /**
     * Look up a movie by its exact title (case-insensitive).
     */
    public Movie getMovieByTitle(String title) {
        return movieMap.get(title.toLowerCase());
    }

    /**
     * Pick and return a random movie from allMovies,
     * or null if we haven’t loaded anything.
     */
    public Movie getRandomMovie() {
        if (allMovies.isEmpty()) return null;
        int idx = new Random().nextInt(allMovies.size());
        return allMovies.get(idx);
    }

    /**
     * @return the complete list of loaded movies
     */
    public List<Movie> getAllMovies() {
        return allMovies;
    }
}
