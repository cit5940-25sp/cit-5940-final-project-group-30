public interface GameView {
    void displayBoard(GameState state);
    void showError(String error);
    void announceWinner(Player winner);
}
