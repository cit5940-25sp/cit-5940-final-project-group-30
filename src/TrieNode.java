import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TrieNode {
    Map<Character, TrieNode> children;
    boolean isEndOfWord;
    List<Movie> movies;

    public TrieNode() {
        children = new HashMap<>();
        isEndOfWord = false;
        movies = new ArrayList<>();
    }
}