package blackjack.io;

import blackjack.model.Card;
import blackjack.model.Hand;

/**
 * Class for output through console.
 * Provides output functionality for the blackjack game.
 */
public class ConsoleOutput {

    /**
     * Prints the game welcome message.
     */
    public static void printHelloMessage() {
        System.out.println("Добро пожаловать в игру Блекджек!");
    }

    /**
     * Prints the round header with its number.
     *
     * @param roundNumber the current round number
     */
    public static void printRoundHeader(int roundNumber) {
        System.out.println("\n-- Раунд " + roundNumber + " --");
    }

    /**
     * Prints a message that the dealer dealt cards.
     */
    public static void printDealerDealtCards() {
        System.out.println("Дилер раздал карты");
    }

    /**
     * Prints the player's cards and their total sum.
     *
     * @param hand the player's hand
     */
    public static void printPlayerCards(Hand hand) {
        System.out.println("Ваши карты: [" + hand.toString()
            + "] (сумма: " + hand.getValue() + ")");
    }

    /**
     * Prints the dealer's cards and their total sum.
     *
     * @param hand the dealer's hand
     */
    public static void printDealerCards(Hand hand) {
        System.out.println("Карты дилера: [" + hand.toString()
            + "] (сумма: " + hand.getValue() + ")");
    }

    /**
     * Prints the dealer's cards with one hidden card.
     *
     * @param hand the dealer's hand
     */
    public static void printDealerClosedCards(Hand hand) {
        System.out.println("Карты дилера: [" + hand.getCard(0).toString()
            + " (" + hand.getCard(0).getValue() + ")" + ", <закрытая карта>]");
    }

    /**
     * Prints a message about player's blackjack.
     */
    public static void printPlayerHasBlackjack() {
        System.out.println("У вас Блекджек! Вы выиграли!");
    }

    /**
     * Prints a message about dealer's blackjack.
     */
    public static void printDealerHasBlackjack() {
        System.out.println("У дилера Блекджек! Вы проигр��ли.");
    }

    /**
     * Prints the player's turn header.
     */
    public static void printPlayerTurn() {
        System.out.println("\nВаш ход");
        System.out.println("-------");
    }

    /**
     * Prints the dealer's turn header.
     */
    public static void printDealerTurn() {
        System.out.println("\nХод дилера");
        System.out.println("-------");
    }

    /**
     * Prints a prompt for the player's decision to hit or stand.
     */
    public static void printHitOrStand() {
        System.out.println("Введите '1', чтобы взять карту, и '0', чтобы остановиться...");
    }

    /**
     * Prints a message that the player drew a card.
     *
     * @param card the drawn card
     */
    public static void printPlayerDrewCard(Card card) {
        System.out.println("Вы открыли карту " + card.toString());
    }

    /**
     * Prints a message that the dealer drew a card.
     *
     * @param card the drawn card
     */
    public static void printDealerDrewCard(Card card) {
        System.out.println("Дилер открывает карту " + card.toString());
    }

    /**
     * Prints a message that the dealer reveals the hidden card.
     */
    public static void printDealerDrewClosedCard() {
        System.out.println("Дилер открывает закрытую карту");
    }

    /**
     * Prints a message about player's bust.
     */
    public static void printPlayerBust() {
        System.out.println("Перебор! Вы проиграли.");
    }

    /**
     * Prints a message that the player got 21.
     */
    public static void printPlayer21() {
        System.out.println("У вас 21! Отличный результат!");
    }

    /**
     * Prints an error message about hand overflow.
     */
    public static void printErrorHandFull() {
        System.out.println("Ошибка: рука переполнена!");
    }

    /**
     * Prints an error message about running out of cards in the shoe.
     */
    public static void printErrorShoeOutOfCards() {
        System.out.println("Ошибка: в колоде закончились карты!");
    }

    /**
     * Prints an error message about invalid card index.
     */
    public static void printErrorInvalidCardIndex() {
        System.out.println("Ошибка: неверный индекс карты!");
    }

    /**
     * Prints the current game score.
     *
     * @param playerScore the player's score
     * @param dealerScore the dealer's score
     */
    public static void printScore(int playerScore, int dealerScore) {
        System.out.println("\nТекущий счет:");
        System.out.println("Игрок: " + playerScore);
        System.out.println("Дилер: " + dealerScore);
    }

    /**
     * Prints the final game results.
     *
     * @param playerScore the player's final score
     * @param dealerScore the dealer's final score
     */
    public static void printFinalResults(int playerScore, int dealerScore) {
        System.out.println("\n=== ФИНАЛЬНЫЕ РЕЗУЛЬТАТЫ ===");
        System.out.println("Игрок: " + playerScore);
        System.out.println("Дилер: " + dealerScore);

        if (playerScore > dealerScore) {
            System.out.println("Поздравляем! Вы выиграли общий зачет!");
        } else if (dealerScore > playerScore) {
            System.out.println("Дилер выиграл общий зачет. Удачи в следующий раз!");
        } else {
            System.out.println("Общий зачет закончился вничью!");
        }

        System.out.println("Спасибо за игру!");
    }

    /**
     * Prints a message that dealer is busted.
     */
    public static void printDealerBust() {
        System.out.println("У дилера перебор! Вы выиграли!");
    }

    /**
     * Prints a message that player won with comparison of values.
     *
     * @param playerValue player's hand value
     * @param dealerValue dealer's hand value
     */
    public static void printPlayerWins(int playerValue, int dealerValue) {
        System.out.println("Вы выиграли! (" + playerValue + " против " + dealerValue + ")");
    }

    /**
     * Prints a message that dealer won with comparison of values.
     *
     * @param dealerValue dealer's hand value
     * @param playerValue player's hand value
     */
    public static void printDealerWins(int dealerValue, int playerValue) {
        System.out.println("Дилер выиграл! (" + dealerValue + " против " + playerValue + ")");
    }

    /**
     * Prints a message about draw game.
     *
     * @param value the value of both hands
     */
    public static void printDraw(int value) {
        System.out.println("Ничья! (" + value + " против " + value + ")");
    }
}
