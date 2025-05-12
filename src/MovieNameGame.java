import javax.swing.*;
import java.util.*;

public class MovieNameGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Load data
            MovieGraph movieGraph = DataLoader.loadMovieData("data/tmdb_movies.csv");

            // Random win conditions
            List<String> allGenres = Arrays.asList(
                    "Action", "Adventure", "Animation", "Comedy", "Crime", "Documentary", "Drama",
                    "Family", "Fantasy", "Foreign", "History", "Horror", "Music", "Mystery",
                    "Romance", "Science Fiction", "TV Movie", "Thriller", "War", "Western"
            );
            Collections.shuffle(allGenres);
            Map<String, Integer> p1 = new HashMap<>();
            Map<String, Integer> p2 = new HashMap<>();
            p1.put(allGenres.get(0), 5);
            p2.put(allGenres.get(1), 5);

            // Players
            Player player1 = new Player("Player 1", p1);
            Player player2 = new Player("Player 2", p2);

            // Step 1: Create GUI without controller
            GamePlayGUI gui = new GamePlayGUI();

            // Step 2: Create controller, injecting the GUI as the view
            GameController controller = new GameController(player1, player2, movieGraph, gui);

            // Step 3: Hook controller back into GUI
            gui.setController(controller);

            // Start game
            Movie startingMovie = movieGraph.getRandomMovie();
            controller.startGame(startingMovie);

            // Show GUI
            gui.setVisible(true);
        });
    }
}
