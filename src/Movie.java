import java.util.List;
import java.util.Objects;

public class Movie {
    private final String title;
    private final int year;
    private final List<String> genres;

    private final List<MovieFlyweight.Person> directors;
    private final List<MovieFlyweight.Person> actors;


    public Movie(String title,
                 int year,
                 List<String> genres,
                 List<MovieFlyweight.Person> directors,
                 List<MovieFlyweight.Person> actors) {
        this.title = title;
        this.year = year;
        this.genres = genres;
        this.directors = directors;
        this.actors = actors;
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
