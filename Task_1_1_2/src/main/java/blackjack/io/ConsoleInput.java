package blackjack.io;

import java.util.Scanner;

/**
 * Class for user input through console.
 * Provides input functionality for the blackjack game.
 */
public class ConsoleInput {

    /**
     * Reads the player's choice about hitting or standing.
     *
     * @return true if the player chooses to hit, false if stand
     */
    public static boolean readPlayerTurnChoice() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Ваш выбор: ");
            String input = scanner.nextLine().trim();
            if ("1".equals(input)) {
                return true;
            } else if ("0".equals(input)) {
                return false;
            } else {
                System.out.println("Пожалуйста, введите '1' или '0'.");
            }
        }
    }

    /**
     * Reads the player's choice about continuing the game.
     *
     * @return true if the player wants to play another round, false if quit
     */
    public static boolean readPlayerNextRoundChoice() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Хотите сыграть еще раунд? (1 - да, 0 - нет): ");
            String input = scanner.nextLine().trim();
            if ("1".equals(input)) {
                return true;
            } else if ("0".equals(input)) {
                return false;
            } else {
                System.out.println("Пожалуйста, введите '1' или '0'.");
            }
        }
    }
}
