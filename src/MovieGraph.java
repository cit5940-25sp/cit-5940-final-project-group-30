import java.util.*;

public class MovieGraph {
    private final Map<String, Movie> movieMap = new HashMap<>();
    private final Map<String, List<Movie>> personToMovies = new HashMap<>();
    private final List<Movie> allMovies = new ArrayList<>();

    public void addMovie(Movie movie) {
        movieMap.put(movie.getTitle().toLowerCase(), movie);
        allMovies.add(movie);
        indexPeople(movie.getActors(), movie);
        indexPeople(movie.getDirectors(), movie);
        indexPeople(movie.getWriters(), movie);
        indexPeople(movie.getCinematographers(), movie);
        indexPeople(movie.getComposers(), movie);
    }

    private void indexPeople(List<String> people, Movie movie) {
        for (String person : people) {
            personToMovies
                    .computeIfAbsent(person.toLowerCase(), k -> new ArrayList<>())
                    .add(movie);
        }
    }

    public List<Movie> getNeighbors(Movie movie) {
        Set<Movie> neighbors = new HashSet<>();
        addConnectionsFromList(movie.getActors(), movie, neighbors);
        addConnectionsFromList(movie.getDirectors(), movie, neighbors);
        addConnectionsFromList(movie.getWriters(), movie, neighbors);
        addConnectionsFromList(movie.getCinematographers(), movie, neighbors);
        addConnectionsFromList(movie.getComposers(), movie, neighbors);
        neighbors.remove(movie);
        return new ArrayList<>(neighbors);
    }

    private void addConnectionsFromList(List<String> people, Movie source, Set<Movie> neighbors) {
        for (String person : people) {
            List<Movie> connected = personToMovies.getOrDefault(person.toLowerCase(),
                    Collections.emptyList());
            for (Movie candidate : connected) {
                if (!candidate.equals(source)) {
                    neighbors.add(candidate);
                }
            }
        }
    }

    public boolean areConnected(Movie m1, Movie m2) {
        return getNeighbors(m1).contains(m2);
    }

    public Movie getMovieByTitle(String title) {
        return movieMap.get(title.toLowerCase());
    }

    public Movie getRandomMovie() {
        if (allMovies.isEmpty()) {
            return null;
        }
        return allMovies.get((int)(Math.random() * allMovies.size()));
    }

    public List<Movie> getAllMovies() {
        return allMovies;
    }
}
