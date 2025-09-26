package blackjack_test.io_test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import blackjack.io.ConsoleOutput;
import blackjack.model.Card;
import blackjack.model.Hand;
import blackjack.model.Rank;
import blackjack.model.Suit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleOutputTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void testPrintHelloMessage() {
        ConsoleOutput.printHelloMessage();
        assertTrue(outputStreamCaptor.toString()
            .contains("Добро пожаловать в игру Блекджек!"));
    }

    @Test
    void testPrintRoundHeader() {
        ConsoleOutput.printRoundHeader(3);
        assertTrue(outputStreamCaptor.toString().contains("Раунд 3"));
    }

    @Test
    void testPrintDealerDealtCards() {
        ConsoleOutput.printDealerDealtCards();
        assertTrue(outputStreamCaptor.toString().contains("Дилер раздал карты"));
    }

    @Test
    void testPrintPlayerCards() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE, Suit.HEARTS));
        hand.addCard(new Card(Rank.TEN, Suit.DIAMONDS));

        ConsoleOutput.printPlayerCards(hand);
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Ваши карты:"));
        assertTrue(output.contains("(сумма: 21)"));
    }

    @Test
    void testPrintDealerCards() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.KING, Suit.CLUBS));
        hand.addCard(new Card(Rank.SEVEN, Suit.SPADES));

        ConsoleOutput.printDealerCards(hand);
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Карты дилера:"));
        assertTrue(output.contains("(сумма: 17)"));
    }

    @Test
    void testPrintDealerClosedCards() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.QUEEN, Suit.HEARTS));
        hand.addCard(new Card(Rank.FIVE, Suit.CLUBS));

        ConsoleOutput.printDealerClosedCards(hand);
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Карты дилера:"));
        assertTrue(output.contains("(10)"));
        assertTrue(output.contains("<закрытая карта>"));
    }

    @Test
    void testPrintPlayerHasBlackjack() {
        ConsoleOutput.printPlayerHasBlackjack();
        assertTrue(outputStreamCaptor.toString()
            .contains("У вас Блекджек! Вы выиграли!"));
    }

    @Test
    void testPrintDealerHasBlackjack() {
        ConsoleOutput.printDealerHasBlackjack();
        assertTrue(outputStreamCaptor.toString()
            .contains("У дилера Блекджек! Вы проигр��ли."));
    }

    @Test
    void testPrintPlayerTurn() {
        ConsoleOutput.printPlayerTurn();
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Ваш ход"));
        assertTrue(output.contains("-------"));
    }

    @Test
    void testPrintDealerTurn() {
        ConsoleOutput.printDealerTurn();
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Ход дилера"));
        assertTrue(output.contains("-------"));
    }

    @Test
    void testPrintHitOrStand() {
        ConsoleOutput.printHitOrStand();
        assertTrue(outputStreamCaptor.toString()
            .contains("Введите '1', чтобы взять карту, и '0', чтобы остановиться..."));
    }

    @Test
    void testPrintPlayerDrewCard() {
        Card card = new Card(Rank.EIGHT, Suit.DIAMONDS);

        ConsoleOutput.printPlayerDrewCard(card);
        assertTrue(outputStreamCaptor.toString().contains("Вы открыли карту"));
    }

    @Test
    void testPrintDealerDrewCard() {
        Card card = new Card(Rank.THREE, Suit.CLUBS);

        ConsoleOutput.printDealerDrewCard(card);
        assertTrue(outputStreamCaptor.toString().contains("Дилер открывает карту"));
    }

    @Test
    void testPrintDealerDrewClosedCard() {
        ConsoleOutput.printDealerDrewClosedCard();
        assertTrue(outputStreamCaptor.toString()
            .contains("Дилер открывает закрытую карту"));
    }

    @Test
    void testPrintPlayerBust() {
        ConsoleOutput.printPlayerBust();
        assertTrue(outputStreamCaptor.toString().contains("Перебор! Вы проиграли."));
    }

    @Test
    void testPrintPlayer21() {
        ConsoleOutput.printPlayer21();
        assertTrue(outputStreamCaptor.toString()
            .contains("У вас 21! Отличный результат!"));
    }

    @Test
    void testPrintErrorHandFull() {
        ConsoleOutput.printErrorHandFull();
        assertTrue(outputStreamCaptor.toString().contains("Ошибка: рука переполнена!"));
    }

    @Test
    void testPrintErrorShoeOutOfCards() {
        ConsoleOutput.printErrorShoeOutOfCards();
        assertTrue(outputStreamCaptor.toString()
            .contains("Ошибка: в колоде закончились карты!"));
    }

    @Test
    void testPrintErrorInvalidCardIndex() {
        ConsoleOutput.printErrorInvalidCardIndex();
        assertTrue(outputStreamCaptor.toString()
            .contains("Ошибка: неверный индекс карты!"));
    }

    @Test
    void testPrintScore() {
        ConsoleOutput.printScore(3, 2);
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("Текущий счет:"));
        assertTrue(output.contains("Игрок: 3"));
        assertTrue(output.contains("Дилер: 2"));
    }

    @Test
    void testPrintFinalResults_PlayerWins() {
        ConsoleOutput.printFinalResults(5, 3);
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("=== ФИНАЛЬНЫЕ РЕЗУЛЬТАТЫ ==="));
        assertTrue(output.contains("Игрок: 5"));
        assertTrue(output.contains("Дилер: 3"));
        assertTrue(output.contains("Поздравляем! Вы выиграли общий зачет!"));
        assertTrue(output.contains("Спасибо за игру!"));
    }

    @Test
    void testPrintFinalResults_DealerWins() {
        ConsoleOutput.printFinalResults(2, 4);
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("=== ФИНАЛЬНЫЕ РЕЗУЛЬТАТЫ ==="));
        assertTrue(output.contains("Игрок: 2"));
        assertTrue(output.contains("Дилер: 4"));
        assertTrue(output.contains("Дилер выиграл общий зачет"));
        assertTrue(output.contains("Спасибо за игру!"));
    }

    @Test
    void testPrintFinalResults_Draw() {
        ConsoleOutput.printFinalResults(3, 3);
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains("=== ФИНАЛЬНЫЕ РЕЗУЛЬТАТЫ ==="));
        assertTrue(output.contains("Игрок: 3"));
        assertTrue(output.contains("Дилер: 3"));
        assertTrue(output.contains("Общий зачет закончился вничью!"));
        assertTrue(output.contains("Спасибо за игру!"));
    }

    @Test
    void testPrintDealerBust() {
        ConsoleOutput.printDealerBust();
        assertTrue(outputStreamCaptor.toString()
            .contains("У дилера перебор! Вы выиграли!"));
    }

    @Test
    void testPrintPlayerWins() {
        ConsoleOutput.printPlayerWins(20, 18);
        assertTrue(outputStreamCaptor.toString()
            .contains("Вы выиграли! (20 против 18)"));
    }

    @Test
    void testPrintDealerWins() {
        ConsoleOutput.printDealerWins(19, 17);
        assertTrue(outputStreamCaptor.toString()
            .contains("Дилер выиграл! (19 против 17)"));
    }

    @Test
    void testPrintDraw() {
        ConsoleOutput.printDraw(18);
        assertTrue(outputStreamCaptor.toString().contains("Ничья! (18 против 18)"));
    }
}
