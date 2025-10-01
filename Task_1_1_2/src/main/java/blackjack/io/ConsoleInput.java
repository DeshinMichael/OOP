package blackjack.io;

import blackjack.i18n.I18nManager;
import java.util.Scanner;

/**
 * Class for user input through console.
 * Provides input functionality for the blackjack game.
 */
public class ConsoleInput {
    private static final I18nManager i18n = I18nManager.getInstance();
    private static Scanner scanner;

    /**
     * Reads the player's choice about hitting or standing.
     *
     * @return true if the player chooses to hit, false if stand
     */
    public static boolean readPlayerTurnChoice() {
        while (true) {
            System.out.print(i18n.getString("input.turn.prompt"));
            String input = getScanner().nextLine().trim();
            if ("1".equals(input)) {
                return true;
            } else if ("0".equals(input)) {
                return false;
            } else {
                System.out.println(i18n.getString("input.invalid"));
            }
        }
    }

    /**
     * Reads the player's choice about continuing the game.
     *
     * @return true if the player wants to play another round, false if quit
     */
    public static boolean readPlayerNextRoundChoice() {
        while (true) {
            System.out.print(i18n.getString("input.round.prompt"));
            String input = getScanner().nextLine().trim();
            if ("1".equals(input)) {
                return true;
            } else if ("0".equals(input)) {
                return false;
            } else {
                System.out.println(i18n.getString("input.invalid"));
            }
        }
    }

    /**
     * Closes the scanner. Should be called when the application is about to exit.
     */
    public static void closeScanner() {
        if (scanner != null) {
            scanner.close();
            scanner = null;
        }
    }

    private static Scanner getScanner() {
        if (scanner == null) {
            scanner = new Scanner(System.in);
        }
        return scanner;
    }
}
