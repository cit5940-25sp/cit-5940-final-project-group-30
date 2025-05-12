import java.util.*;

public class MovieGraph {
    private final Map<Movie, Set<Movie>> movieEdges = new HashMap<>();
    private final Map<String, Movie> movieMap    = new HashMap<>();
    private final List<Movie> allMovies          = new ArrayList<>();
    private final Map<String, Movie> moviesByLowercaseTitle = new HashMap<>();

    /** Add a node to the graph (edges come later). */
    public void addMovie(Movie movie) {
        String key = movie.getTitle().toLowerCase();
        movieMap.put(key, movie);
        moviesByLowercaseTitle.put(movie.getTitle().toLowerCase(), movie);
        movieEdges.putIfAbsent(movie, new HashSet<>());
        allMovies.add(movie);
    }

    /** After loading everything, connect movies sharing any Person flyweight. */
    public void buildConnections() {
        // person → list of movies they worked on
        Map<MovieFlyweight.Person, Set<Movie>> personToMovies = new HashMap<>();

        // collect: for each movie, for each person role, group them
        for (Movie m : allMovies) {
            for (MovieFlyweight.Person p : m.getActors()) {
                personToMovies.computeIfAbsent(p, k -> new HashSet<>()).add(m);
            }
            for (MovieFlyweight.Person p : m.getDirectors()) {
                personToMovies.computeIfAbsent(p, k -> new HashSet<>()).add(m);
            }

        }

        // for each person, fully connect their movies
        for (Set<Movie> group : personToMovies.values()) {
            if (group.size() < 2) {
                continue;
            }
            for (Movie m1 : group) {
                for (Movie m2 : group) {
                    if (!m1.equals(m2)) {
                        movieEdges.get(m1).add(m2);
                        movieEdges.get(m2).add(m1);
                    }
                }
            }
        }
    }

    /** True if m1 and m2 share an edge. */
    public boolean areConnected(Movie m1, Movie m2) {
        return movieEdges.getOrDefault(m1, Collections.emptySet()).contains(m2);
    }

    /** Return every neighbor of the given movie. */
    public List<Movie> getNeighbors(Movie movie) {
        return new ArrayList<>(movieEdges.getOrDefault(movie, Collections.emptySet()));
    }

    /** Lookup by title (case-insensitive). */
    public Movie getMovieByTitle(String title) {
        return moviesByLowercaseTitle.get(title.toLowerCase());
    }

    /** Pick a random movie as a starter. */
    public Movie getRandomMovie() {
        if (allMovies.isEmpty()) {
            return null;
        }
        return allMovies.get(new Random().nextInt(allMovies.size()));
    }

    /** Everything we loaded, in insertion order. */
    public List<Movie> getAllMovies() {
        return allMovies;
    }
}