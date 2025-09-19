import java.util.Scanner;

public class ConsoleIO {
    private final Scanner in;

    public ConsoleIO(Scanner in) {
        this.in = in;
    }

    public void printHelloMessage() {
        System.out.println("Добро пожаловать в игру Блекджек!");
        System.out.println();
    }

    public void printRoundHeader(int roundCount) {
        System.out.println("-- Раунд " + roundCount + " --");
    }

    public void printDealerDealtCards() {
        System.out.println("Дилер раздал карты");
    }

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

    public void printPlayerHasBlackjack() {
        System.out.println("У вас Блекджек! Вы выиграли!");
    }

    public void printDealerHasBlackjack() {
        System.out.println("У дилера Блекджек! Вы проиграли.");
    }

    public void printPlayerTurn() {
        System.out.println("Ваш ход");
        System.out.println("-------");
    }

    public void printDealerTurn() {
        System.out.println("Ход дилера");
        System.out.println("-------");
    }

    public void printHitOrStand() {
        System.out.println("Введите '1', чтобы взять карту, и '0', чтобы остановиться...");
    }

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

    public void printPlayerDrewCard(Card card) {
        System.out.println("Вы открыли карту " + card);
    }

    public void printDealerDrewCard(Card card) {
        System.out.println("Дилер открывает карту " + card);
    }

    public void printDealerDrewClosedCard() {
        System.out.println("Дилер открывает закрытую карту");
    }

    public void printPlayerBust() {
        System.out.println("Перебор! Вы проиграли.");
    }

    public void printDealerBust() {
        System.out.println("Дилер перебрал! Вы выиграли!");
    }

    public void printPlayer21() {
        System.out.println("У вас 21!");
    }

    public void printDealerWon() {
        System.out.println("Дилер выиграл.");
    }

    public void printPlayerWon() {
        System.out.println("Вы выиграли!");
    }

    public void printPush() {
        System.out.println("Ничья.");
    }

    public void printRoundIsOver(Participant player, Participant dealer) {
        System.out.println("Раунд окончен. " + "Счёт " + player.getScore() + ":" + dealer.getScore());
        System.out.println("-------");
    }

    public void printErrorShoeOutOfCards() {
        System.err.println("Ошибка: В колоде больше нет карт");
    }

    public void printErrorHandFull() {
        System.err.println("Ошибка: Рука переполнена");
    }

    public void printErrorInvalidCardIndex() {
        System.err.println("Ошибка: Неверный индекс карты");
    }
}