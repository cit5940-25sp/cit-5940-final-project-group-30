import java.util.List;
import java.util.Objects;

public class Movie {
    private final String title;
    private final int year;
    private final List<String> genres;

    private final List<MovieFlyweight.Person> directors;
    private final List<MovieFlyweight.Person> actors;
    private final List<MovieFlyweight.Person> writers;
    private final List<MovieFlyweight.Person> cinematographers;
    private final List<MovieFlyweight.Person> composers;


    public Movie(String title,
                 int year,
                 List<String> genres,
                 List<MovieFlyweight.Person> directors,
                 List<MovieFlyweight.Person> actors,
                 List<MovieFlyweight.Person> writers,
                 List<MovieFlyweight.Person> cinematographers,
                 List<MovieFlyweight.Person> composers) {
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

    public List<MovieFlyweight.Person> getDirectors() {
        return directors;
    }

    public List<MovieFlyweight.Person> getActors() {
        return actors;
    }

    public List<MovieFlyweight.Person> getWriters() {
        return writers;
    }

    public List<MovieFlyweight.Person> getCinematographers() {
        return cinematographers;
    }

    public List<MovieFlyweight.Person> getComposers() {
        return composers;
    }

    @Override
    public String toString() {
        return title + " (" + year + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Movie other = (Movie) obj;
        return title.equalsIgnoreCase(other.title) && year == other.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title.toLowerCase(), year);
    }

}
