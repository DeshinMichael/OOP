import java.util.Scanner;

// Class for handling all console input/output operations in the game
public class ConsoleIO {
    private final Scanner in;

    // Constructor that initializes with input scanner
    public ConsoleIO(Scanner in) {
        this.in = in;
    }

    // Displays welcome message when game starts
    public void printHelloMessage() {
        System.out.println("Добро пожаловать в игру Блекджек!");
        System.out.println();
    }

    // Shows the current round number as a header
    public void printRoundHeader(int roundCount) {
        System.out.println("-- Раунд " + roundCount + " --");
    }

    // Indicates that the dealer has dealt cards to players
    public void printDealerDealtCards() {
        System.out.println("Дилер раздал карты");
    }

    // Shows cards for both player and dealer, with option to hide dealer's second card
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

    // Announces player has blackjack and won the round
    public void printPlayerHasBlackjack() {
        System.out.println("У вас Блекджек! Вы выиграли!");
    }

    // Announces dealer has blackjack and player lost the round
    public void printDealerHasBlackjack() {
        System.out.println("У дилера Блекджек! Вы проиграли.");
    }

    // Indicates the beginning of player's turn
    public void printPlayerTurn() {
        System.out.println("Ваш ход");
        System.out.println("-------");
    }

    // Indicates the beginning of dealer's turn
    public void printDealerTurn() {
        System.out.println("Ход дилера");
        System.out.println("-------");
    }

    // Asks player to decide between hitting or standing
    public void printHitOrStand() {
        System.out.println("Введите '1', чтобы взять карту, и '0', чтобы остановиться...");
    }

    // Gets player's decision to hit (true) or stand (false)
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

    // Asks if player wants to play another round
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

    // Announces which card the player drew
    public void printPlayerDrewCard(Card card) {
        System.out.println("Вы открыли карту " + card);
    }

    // Announces which card the dealer drew
    public void printDealerDrewCard(Card card) {
        System.out.println("Дилер открывает карту " + card);
    }

    // Announces that dealer is revealing their hidden card
    public void printDealerDrewClosedCard() {
        System.out.println("Дилер открывает закрытую карту");
    }

    // Announces player busted (exceeded 21) and lost
    public void printPlayerBust() {
        System.out.println("Перебор! Вы проиграли.");
    }

    // Announces dealer busted (exceeded 21) and player won
    public void printDealerBust() {
        System.out.println("Дилер перебрал! Вы выиграли!");
    }

    // Announces player has exactly 21 points
    public void printPlayer21() {
        System.out.println("У вас 21!");
    }

    // Announces dealer won the round
    public void printDealerWon() {
        System.out.println("Дилер выиграл.");
    }

    // Announces player won the round
    public void printPlayerWon() {
        System.out.println("Вы выиграли!");
    }

    // Announces the round ended in a tie
    public void printPush() {
        System.out.println("Ничья.");
    }

    // Shows end of round message with current score
    public void printRoundIsOver(Participant player, Participant dealer) {
        System.out.println("Раунд окончен. " + "Счёт " + player.getScore() + ":" + dealer.getScore());
        System.out.println("-------");
    }

    // Displays error when shoe has no more cards
    public void printErrorShoeOutOfCards() {
        System.err.println("Ошибка: В колоде больше нет карт");
    }

    // Displays error when hand cannot hold more cards
    public void printErrorHandFull() {
        System.err.println("Ошибка: Рука переполнена");
    }

    // Displays error when attempting to access card with invalid index
    public void printErrorInvalidCardIndex() {
        System.err.println("Ошибка: Неверный индекс карты");
    }
}