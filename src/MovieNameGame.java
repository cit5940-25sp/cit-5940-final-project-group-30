public class MovieNameGame {
    public static void main(String[] args) {
        GameController controller = new GameController();
        controller.startGame();

        while (true) {
            controller.processInput();
            controller.updateState();
            if (controller.checkWin()) {
                break;
            }
        }
    }
}