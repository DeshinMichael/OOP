import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ConsoleIO io = new ConsoleIO(in);
        ErrorHandler handler = new ErrorHandler(io);
        Game game = new Game(4, io, handler);
        game.startGame();
    }
}