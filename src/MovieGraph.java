import java.util.*;

public class MovieGraph {
    // Map each Movie to the set of Movies it's connected to
    private final Map<Movie, Set<Movie>> movieEdges = new HashMap<>();
    // Quick lookup: title (lowercase) → Movie
    private final Map<String, Movie> movieMap = new HashMap<>();
    // Keep a list of all movies for random selection
    private final List<Movie> allMovies = new ArrayList<>();

    /**
     * Add a new movie node (no edges yet).
     */
    public void addMovie(Movie movie) {
        movieMap.put(movie.getTitle().toLowerCase(), movie);
        // ensure there's an entry in the edges map
        movieEdges.putIfAbsent(movie, new HashSet<>());
        allMovies.add(movie);
        
    }

    /**
     * After loading all movies, connect every pair that share a Person instance.
     */
    public void buildConnections() {
        // temporary map: Person → all movies they worked on
        Map<MovieFlyweight.Person, Set<Movie>> personToMovies = new HashMap<>();

        // collect movies per person
        for (Movie m : movieEdges.keySet()) {
            addPersonConnections(personToMovies, m.getActors(), m);
            addPersonConnections(personToMovies, m.getDirectors(), m);
            addPersonConnections(personToMovies, m.getWriters(), m);
            addPersonConnections(personToMovies, m.getCinematographers(), m);
            addPersonConnections(personToMovies, m.getComposers(), m);
        }
    
        // for each person, fully connect their movies
        for (Set<Movie> group : personToMovies.values()) {
            if (group.size() < 2) continue;  // no connections if only one movie
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

    /**
     * Helper: map each Person to the movies they appear in.
     * @param personToMovies map being built
     * @param people list of Person flyweights from one movie
     * @param movie the movie to associate
     */
    private void addPersonConnections(Map<MovieFlyweight.Person, Set<Movie>> personToMovies,
                                      List<MovieFlyweight.Person> people,
                                      Movie movie) {
        for (MovieFlyweight.Person p : people) {
            // create the set if absent, then add this movie
            personToMovies
                    .computeIfAbsent(p, k -> new HashSet<>())
                    .add(movie);
    }


    /**
     * Check if two movies share an edge.
     */
    public boolean areConnected(Movie m1, Movie m2) {
        return movieEdges
                .getOrDefault(m1, Collections.emptySet())
                .contains(m2);
    }

    /**
     * Get all directly connected neighbors of a movie.
     */
    public List<Movie> getNeighbors(Movie movie) {
        return new ArrayList<>(
                movieEdges.getOrDefault(movie, Collections.emptySet())
        );
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
