import javax.swing.*;
import java.util.*;

public class MovieNameGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameInitializer().execute();
        });
    }
}

class GameInitializer extends SwingWorker<Void, Void> {
    @Override
    protected Void doInBackground() {
        // Load data
        MovieGraph movieGraph = DataLoader.loadMovieData("data/tmdb_movies.csv");

        // Randomly select two distinct genres from a list
        List<String> allGenres = Arrays.asList(
                "Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama",
                "Family", "Fantasy", "Foreign", "History", "Horror", "Music", "Mystery",
                "Romance", "Science Fiction", "TV Movie", "Thriller", "War", "Western"
        );
        Collections.shuffle(allGenres);
        String genre1 = allGenres.get(0);
        String genre2 = allGenres.get(1);

        // Create win conditions
        Map<String, Integer> player1WinCondition = new HashMap<>();
        player1WinCondition.put(genre1, 5);
        Map<String, Integer> player2WinCondition = new HashMap<>();
        player2WinCondition.put(genre2, 5);

        // Create players and controller
        Player player1 = new Player("Player 1", player1WinCondition);
        Player player2 = new Player("Player 2", player2WinCondition);
        GameController controller = new GameController(player1, player2, movieGraph);

        // Pick starting movie
        Movie startingMovie = movieGraph.getRandomMovie();
        controller.startGame(startingMovie);

        // Launch GUI
        SwingUtilities.invokeLater(() -> {
            GamePlayGUI gui = new GamePlayGUI(controller);
            gui.setVisible(true);
        });

        return null;
    }
}

