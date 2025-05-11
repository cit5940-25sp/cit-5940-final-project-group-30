import java.util.Map;
import java.util.HashMap;

public class MovieNameGame {
    public static void main(String[] args) {
        // Load data
        MovieGraph movieGraph = DataLoader.loadMovieData("data/tmdb_movies.csv");

        // Create players with win conditions
        Map<String, Integer> player1WinCondition = new HashMap<>();
        player1WinCondition.put("Horror", 5);

        Map<String, Integer> player2WinCondition = new HashMap<>();
        player2WinCondition.put("Action", 5);

        Player player1 = new Player("Player 1", player1WinCondition);
        Player player2 = new Player("Player 2", player2WinCondition);

        // Create controller
        GameController controller = new GameController(player1, player2, movieGraph);

        // Start with a random movie
        Movie startingMovie = movieGraph.getRandomMovie();
        controller.startGame(startingMovie);

        // Main game loop
        InputHandler inputHandler = new InputHandler(controller, movieGraph);
        while (true) {
            String input = controller.getView().getInput();
            inputHandler.handleInput(input);
        }
    }
}