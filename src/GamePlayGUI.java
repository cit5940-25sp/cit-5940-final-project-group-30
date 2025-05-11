import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Deque;
import java.util.List;

public class GamePlayGUI extends JFrame {
    private final GameController controller;
    private final JTextArea gameInfoArea;
    private final JTextField inputField;
    private final JButton submitButton;
    private final JButton exitButton;
    private final JList<String> suggestionList;
    private final DefaultListModel<String> suggestionModel;

    public GamePlayGUI(GameController controller) {
        this.controller = controller;
        this.setTitle("Movie Name Game");
        this.setSize(700, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        gameInfoArea = new JTextArea();
        gameInfoArea.setEditable(false);
        gameInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        this.add(new JScrollPane(gameInfoArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputPanel.add(inputField, BorderLayout.CENTER);

        submitButton = new JButton("Submit");
        exitButton = new JButton("Exit");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(exitButton);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        this.add(inputPanel, BorderLayout.SOUTH);

        suggestionModel = new DefaultListModel<>();
        suggestionList = new JList<>(suggestionModel);
        this.add(new JScrollPane(suggestionList), BorderLayout.EAST);

        updateGameInfo();

        // Autocomplete when typing
        inputField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateSuggestions(); }
            public void removeUpdate(DocumentEvent e) { updateSuggestions(); }
            public void changedUpdate(DocumentEvent e) { updateSuggestions(); }
        });

        // Select from suggestion list
        suggestionList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = suggestionList.getSelectedValue();
                if (selected != null) {
                    inputField.setText(selected);
                    submitButton.doClick();  // Optional: auto-submit when clicked
                }
            }
        });

        // Submit button (use background thread)
        submitButton.addActionListener((ActionEvent e) -> {
            String input = inputField.getText().trim();
            if (!input.isEmpty()) {
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() {
                        controller.processInput(input);
                        return null;
                    }

                    @Override
                    protected void done() {
                        updateGameInfo();
                        inputField.setText("");
                        suggestionModel.clear();
                    }
                }.execute();
            }
        });

        // Exit game
        exitButton.addActionListener(e -> System.exit(0));
    }

    private void updateGameInfo() {
        GameState state = controller.getGameState();
        StringBuilder sb = new StringBuilder();

        sb.append("Player 1: ").append(state.getPlayer1().getName())
                .append(" | Win Condition: ").append(state.getPlayer1().getWinCondition()).append("\n");
        sb.append("Player 2: ").append(state.getPlayer2().getName())
                .append(" | Win Condition: ").append(state.getPlayer2().getWinCondition()).append("\n");
        sb.append("Current Player: ").append(state.getCurrentPlayer().getName()).append("\n");
        sb.append("Round: ").append(state.getRoundCount()).append("\n\n");

        sb.append("Last 5 Movies:\n");
        Deque<Movie> history = state.getMovieHistory();
        Movie previous = null;
        for (Movie movie : history) {
            String connection = (previous != null) ? findConnection(previous, movie) : "Starting Movie";
            sb.append("- ").append(movie).append(" (").append(connection).append(")\n");
            previous = movie;
        }

        gameInfoArea.setText(sb.toString());
    }

    private void updateSuggestions() {
        String prefix = inputField.getText().trim();
        if (prefix.isEmpty()) {
            suggestionModel.clear();
            return;
        }

        List<Movie> suggestions = controller.getSuggestions(prefix);
        suggestionModel.clear();
        for (Movie m : suggestions) {
            suggestionModel.addElement(m.getTitle());
        }
    }

    private String findConnection(Movie m1, Movie m2) {
        for (MovieFlyweight.Person actor : m1.getActors()) {
            if (m2.getActors().contains(actor)) return "Actor: " + actor.getName();
        }
        for (MovieFlyweight.Person director : m1.getDirectors()) {
            if (m2.getDirectors().contains(director)) return "Director: " + director.getName();
        }
        for (MovieFlyweight.Person writer : m1.getWriters()) {
            if (m2.getWriters().contains(writer)) return "Writer: " + writer.getName();
        }
        for (MovieFlyweight.Person cinematographer : m1.getCinematographers()) {
            if (m2.getCinematographers().contains(cinematographer)) return "Cinematographer: " + cinematographer.getName();
        }
        for (MovieFlyweight.Person composer : m1.getComposers()) {
            if (m2.getComposers().contains(composer)) return "Composer: " + composer.getName();
        }
        return "Connected";
    }
}
