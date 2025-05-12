import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class GamePlayGUI extends JFrame implements GameView {
    private GameController controller;
    private final JTextArea gameInfoArea;
    private final JTextField inputField;
    private final JButton submitButton;
    private final JButton exitButton;
    private final JList<String> suggestionList;
    private final DefaultListModel<String> suggestionModel;
    private final JLabel errorLabel = new JLabel();
    private final JLabel winnerLabel = new JLabel();

    public GamePlayGUI() {
        this.setTitle("Movie Name Game");
        this.setSize(700, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Colors
        Color headerColor = Color.decode("#7373fe");
        Color backgroundColor = Color.decode("#d6d6f6");

        this.getContentPane().setBackground(backgroundColor);

        gameInfoArea = new JTextArea();
        gameInfoArea.setEditable(false);
        gameInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        gameInfoArea.setBackground(backgroundColor);
        this.add(new JScrollPane(gameInfoArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(backgroundColor);
        inputField = new JTextField();
        inputField.setBackground(Color.WHITE);
        inputField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        inputPanel.add(inputField, BorderLayout.CENTER);

        submitButton = new JButton("Submit");
        exitButton = new JButton("Exit");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(backgroundColor);
        buttonPanel.add(submitButton);
        buttonPanel.add(exitButton);
        inputPanel.add(buttonPanel, BorderLayout.EAST);
        this.add(inputPanel, BorderLayout.SOUTH);

        suggestionModel = new DefaultListModel<>();
        suggestionList = new JList<>(suggestionModel);
        suggestionList.setBackground(backgroundColor);
        suggestionList.setSelectionBackground(headerColor);
        this.add(new JScrollPane(suggestionList), BorderLayout.EAST);

        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setOpaque(true);
        errorLabel.setBackground(backgroundColor);
        errorLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        winnerLabel.setForeground(new Color(0, 128, 0));
        winnerLabel.setOpaque(true);
        winnerLabel.setBackground(backgroundColor);
        winnerLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        winnerLabel.setVisible(false);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(backgroundColor);
        messagePanel.add(errorLabel);
        messagePanel.add(winnerLabel);
        this.add(messagePanel, BorderLayout.NORTH);

        inputField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateSuggestions();
            }
            public void removeUpdate(DocumentEvent e) {
                updateSuggestions();
            }
            public void changedUpdate(DocumentEvent e) {
                updateSuggestions();
            }
        });

        suggestionList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = suggestionList.getSelectedValue();
                if (selected != null) {
                    inputField.setText(selected);
                    submitButton.doClick();
                }
            }
        });

        submitButton.addActionListener((ActionEvent e) -> {
            String input = inputField.getText().trim();
            if (!input.isEmpty()) {
                new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() {
                        return controller.processInput(input);
                    }

                    @Override
                    protected void done() {
                        try {
                            boolean success = get();
                            if (success) {
                                errorLabel.setText("");
                                errorLabel.setVisible(false);
                                inputField.setText("");
                                suggestionModel.clear();
                                updateGameInfo();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }.execute();
            }
        });

        exitButton.addActionListener(e -> System.exit(0));
    }

    public void setController(GameController controller) {
        this.controller = controller;
        updateGameInfo();
    }

    @Override
    public void displayBoard(GameState state) {
        updateGameInfo();
    }

    @Override
    public void showError(String error) {
        errorLabel.setText(error);
        errorLabel.setVisible(true);
    }

    @Override
    public void announceWinner(Player winner) {
        winnerLabel.setText("Game Over! Winner: " + winner.getName() + " — Press Exit to close.");
        winnerLabel.setVisible(true);
        errorLabel.setVisible(false);
    }

    private void updateGameInfo() {
        GameState state = controller.getGameState();
        StringBuilder sb = new StringBuilder();

        Player p1 = state.getPlayer1();
        Player p2 = state.getPlayer2();

        for (Map.Entry<String, Integer> entry : p1.getWinCondition().entrySet()) {
            String genre = entry.getKey();
            int goal = entry.getValue();
            int progress = p1.getProgressForGenre(genre);
            sb.append("Player 1: ").append(p1.getName())
                    .append(" | Goal: ").append(genre).append(" ").append(progress).append("/").append(goal).append("\n");
        }

        for (Map.Entry<String, Integer> entry : p2.getWinCondition().entrySet()) {
            String genre = entry.getKey();
            int goal = entry.getValue();
            int progress = p2.getProgressForGenre(genre);
            sb.append("Player 2: ").append(p2.getName())
                    .append(" | Goal: ").append(genre).append(" ").append(progress).append("/").append(goal).append("\n");
        }

        sb.append("Current Player: ").append(state.getCurrentPlayer().getName()).append("\n");
        sb.append("Round: ").append(state.getRoundCount()).append("\n\n");

        sb.append("Last 5 Movies:\n");
        Deque<Movie> history = state.getMovieHistory();
        Movie[] historyArray = history.toArray(new Movie[0]);

        for (int i = historyArray.length - 1; i >= 0; i--) {
            Movie movie = historyArray[i];
            String connection = (i < historyArray.length - 1)
                    ? findConnection(movie, historyArray[i + 1])
                    : "Starting Movie";
            sb.append("- ").append(movie).append(" (").append(connection).append(")\n");
            sb.append("  Genres: ").append(String.join(", ", movie.getGenres())).append("\n");
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
        if (m1 == null || m2 == null) {
            return "Starting Movie";
        }
        for (MovieFlyweight.Person person : m1.getActors()) {
            if (m2.getActors().contains(person)) {
                return "Actor: " + person.getName();
            }
        }
        for (MovieFlyweight.Person person : m1.getDirectors()) {
            if (m2.getDirectors().contains(person)) {
                return "Director: " + person.getName();
            }
        }

        return "No direct connection found";
    }
}
