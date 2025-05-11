import java.util.ArrayList;
import java.util.List;

public class Autocomplete {
    private TrieNode root;

    public Autocomplete() {
        root = new TrieNode();
    }

    public void insert(Movie movie) {
        TrieNode current = root;
        String title = movie.getTitle().toLowerCase();

        for (char c : title.toCharArray()) {
            current.children.putIfAbsent(c, new TrieNode());
            current = current.children.get(c);
        }

        current.isEndOfWord = true;
        current.movies.add(movie);
    }

    public List<Movie> suggestMovies(String prefix) {
        List<Movie> suggestions = new ArrayList<>();
        TrieNode node = findNode(prefix.toLowerCase());

        if (node != null) {
            collectSuggestions(node, suggestions);
            suggestions.sort((m1, m2) -> Integer.compare(m2.getYear(), m1.getYear()));
        }

        return suggestions;
    }

    private TrieNode findNode(String prefix) {
        TrieNode current = root;

        for (char c : prefix.toCharArray()) {
            if (!current.children.containsKey(c)) {
                return null;
            }
            current = current.children.get(c);
        }

        return current;
    }

    private void collectSuggestions(TrieNode node, List<Movie> suggestions) {
        if (node.isEndOfWord) {
            suggestions.addAll(node.movies);
        }

        for (TrieNode child : node.children.values()) {
            collectSuggestions(child, suggestions);
        }
    }
}