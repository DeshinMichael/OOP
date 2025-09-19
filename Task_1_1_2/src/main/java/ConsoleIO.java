import java.util.Scanner;

/**
 * Handles all console input/output operations for the Blackjack game.
 * Responsible for displaying game information and reading user input.
 */
public class ConsoleIO {
    private final Scanner in;

    /**
     * Creates a new ConsoleIO instance with the specified input scanner.
     *
     * @param in The scanner to use for reading user input
     */
    public ConsoleIO(Scanner in) {
        this.in = in;
    }

    /**
     * Prints welcome message at the start of the game.
     */
    public void printHelloMessage() {
        System.out.println("Добро пожаловать в игру Блекджек!");
        System.out.println();
    }

    /**
     * Displays the round number header.
     *
     * @param roundCount The current round number
     */
    public void printRoundHeader(int roundCount) {
        System.out.println("-- Раунд " + roundCount + " --");
    }

    /**
     * Indicates that the dealer has dealt cards.
     */
    public void printDealerDealtCards() {
        System.out.println("Дилер раздал карты");
    }

    /**
     * Displays the player's and dealer's cards.
     *
     * @param player The player participant
     * @param dealer The dealer participant
     * @param revealDealerHand Whether to reveal the dealer's full hand or hide one card
     */
    public void printPlayerAndDealerCards(Participant player, Participant dealer, boolean revealDealerHand) {
        System.out.println();
        System.out.println("Ваши карты: [" + player.getHand() + "] => " + player.getHandValue());
        if (revealDealerHand) {
            System.out.println("Карты дилера: [" + dealer.getHand() + "] => " + dealer.getHandValue());
        } else {
            System.out.println("Карты дилера: [" + dealer.getHand().getCard(0) + ", <закрытая карта>]");
        }
        System.out.println();
    }

    /**
     * Announces that the player has Blackjack.
     */
    public void printPlayerHasBlackjack() {
        System.out.println("У вас Блекджек! Вы выиграли!");
    }

    /**
     * Announces that the dealer has Blackjack.
     */
    public void printDealerHasBlackjack() {
        System.out.println("У дилера Блекджек! Вы проиграли.");
    }

    /**
     * Announces the start of the player's turn.
     */
    public void printPlayerTurn() {
        System.out.println("Ваш ход");
        System.out.println("-------");
    }

    /**
     * Announces the start of the dealer's turn.
     */
    public void printDealerTurn() {
        System.out.println("Ход дилера");
        System.out.println("-------");
    }

    /**
     * Prompts the player to hit or stand.
     */
    public void printHitOrStand() {
        System.out.println("Введите '1', чтобы взять карту, и '0', чтобы остановиться...");
    }

    /**
     * Reads the player's choice to hit or stand.
     *
     * @return true if the player chose to hit, false if the player chose to stand
     */
    public boolean readPlayerTurnChoice() {
        while (true) {
            String input = in.nextLine();
            if (input.equals("1")) {
                return true;
            } else if (input.equals("0")) {
                return false;
            } else {
                System.out.println("Некорректный ввод. Пожалуйста, введите '1' или '0'.");
            }
        }
    }

    /**
     * Asks if the player wants to play another round.
     *
     * @return true if the player wants to play again, false otherwise
     */
    public boolean readPlayerNexRoundChoice() {
        System.out.println("Хотите сыграть еще раз? Введите 'Да' для следующего раунда или 'Нет' для выхода.");
        while (true) {
            String input = in.nextLine();
            if (input.equals("Да")) {
                return true;
            } else if (input.equals("Нет")) {
                System.out.println("Спасибо за игру!");
                return false;
            } else {
                System.out.println("Некорректный ввод. Пожалуйста, введите 'Да' или 'Нет'.");
            }
        }
    }

    /**
     * Announces a card drawn by the player.
     *
     * @param card The card drawn
     */
    public void printPlayerDrewCard(Card card) {
        System.out.println("Вы открыли карту " + card);
    }

    /**
     * Announces a card drawn by the dealer.
     *
     * @param card The card drawn
     */
    public void printDealerDrewCard(Card card) {
        System.out.println("Дилер открывает карту " + card);
    }

    /**
     * Announces that the dealer revealed their closed card.
     */
    public void printDealerDrewClosedCard() {
        System.out.println("Дилер открывает закрытую карту");
    }

    /**
     * Announces that the player has busted.
     */
    public void printPlayerBust() {
        System.out.println("Перебор! Вы проиграли.");
    }

    /**
     * Announces that the dealer has busted.
     */
    public void printDealerBust() {
        System.out.println("Дилер перебрал! Вы выиграли!");
    }

    /**
     * Announces that the player has reached 21.
     */
    public void printPlayer21() {
        System.out.println("У вас 21!");
    }

    /**
     * Announces that the dealer has won.
     */
    public void printDealerWon() {
        System.out.println("Дилер выиграл.");
    }

    /**
     * Announces that the player has won.
     */
    public void printPlayerWon() {
        System.out.println("Вы выиграли!");
    }

    /**
     * Announces a tie game (push).
     */
    public void printPush() {
        System.out.println("Ничья.");
    }

    /**
     * Displays the end of round message with scores.
     *
     * @param player The player participant
     * @param dealer The dealer participant
     */
    public void printRoundIsOver(Participant player, Participant dealer) {
        System.out.println("Раунд окончен. " + "Счёт " + player.getScore() + ":" + dealer.getScore());
        System.out.println("-------");
    }

    /**
     * Prints an error message for an empty shoe.
     */
    public void printErrorShoeOutOfCards() {
        System.err.println("Ошибка: В колоде больше нет карт");
    }

    /**
     * Prints an error message for a full hand.
     */
    public void printErrorHandFull() {
        System.err.println("Ошибка: Рука переполнена");
    }

    /**
     * Prints an error message for an invalid card index.
     */
    public void printErrorInvalidCardIndex() {
        System.err.println("Ошибка: Неверный индекс карты");
    }
}