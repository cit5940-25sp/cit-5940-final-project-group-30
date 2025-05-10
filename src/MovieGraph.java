import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MovieGraph {
    private final Map<Movie, Set<Movie>> movieEdges = new HashMap<>();
    private final Map<String, Movie> movieMap = new HashMap<>();
    private List<Movie> allMovies = new ArrayList<>();

    public void addMovie(Movie movie) {
        movieMap.put(movie.getTitle().toLowerCase(), movie);
        movieEdges.putIfAbsent(movie, new HashSet<>());
        allMovies.add(movie);
    }

    public void buildConnections() {
        Map<String, Set<Movie>> personToMovies = new HashMap<>();

        for (Movie movie : movieEdges.keySet()) {
            addPersonConnections(personToMovies, movie.getActors(), movie);
            addPersonConnections(personToMovies, movie.getDirectors(), movie);
            addPersonConnections(personToMovies, movie.getWriters(), movie);
            addPersonConnections(personToMovies, movie.getCinematographers(), movie);
            addPersonConnections(personToMovies, movie.getComposers(), movie);
        }

        for (Set<Movie> movies : personToMovies.values()) {
            if (movies.size() > 1) {
                for (Movie m1 : movies) {
                    for (Movie m2 : movies) {
                        if (!m1.equals(m2)) {
                            movieEdges.get(m1).add(m2);
                            movieEdges.get(m2).add(m1);
                        }
                    }
                }
            }
        }
    }

    private void addPersonConnections(Map<String, Set<Movie>> personToMovies,
                                      List<String> people, Movie movie) {
        for (String person : people) {
            personToMovies.computeIfAbsent(person.toLowerCase(), k -> new HashSet<>()).add(movie);
        }
    }

    public boolean areConnected(Movie m1, Movie m2) {
        return movieEdges.getOrDefault(m1, new HashSet<>()).contains(m2);
    }

    public List<Movie> getNeighbors(Movie movie) {
        return new ArrayList<>(movieEdges.getOrDefault(movie, new HashSet<>()));
    }

    public Movie getMovieByTitle(String title) {
        return movieMap.get(title.toLowerCase());
    }

    public Movie getRandomMovie() {
        if (allMovies.isEmpty()) return null;
        return allMovies.get((int)(Math.random() * allMovies.size()));
    }

    public List<Movie> getAllMovies() {
        return allMovies;
    }
}