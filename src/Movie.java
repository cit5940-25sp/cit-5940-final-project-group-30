import java.util.List;

public class Movie {
    private final String title;
    private final int year;
    private final List<String> genres;
    private final List<String> directors;
    private final List<String> actors;
    private final List<String> writers;
    private final List<String> cinematographers;
    private final List<String> composers;

    public Movie(String title, int year, List<String> genres, List<String> directors,
                 List<String> actors, List<String> writers,
                 List<String> cinematographers, List<String> composers) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.directors = directors;
        this.actors = actors;
        this.writers = writers;
        this.cinematographers = cinematographers;
        this.composers = composers;
    }

    // Getters
    public String getTitle() {
        return title;
    }
    public int getYear() {
        return year;
    }
    public List<String> getGenres() {
        return genres;
    }
    public List<String> getDirectors() {
        return directors;
    }
    public List<String> getActors() {
        return actors;
    }
    public List<String> getWriters() {
        return writers;
    }
    public List<String> getCinematographers() {
        return cinematographers;
    }
    public List<String> getComposers() {
        return composers;
    }

    @Override
    public String toString() {
        return title + " (" + year + ")";
    }
}