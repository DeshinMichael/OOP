import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleIOTest {
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private ConsoleIO consoleIO;

    @BeforeEach
    public void setUp() {
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        consoleIO = new ConsoleIO(new Scanner(""));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    private ConsoleIO createConsoleIOWithInput(String input) {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        return new ConsoleIO(new Scanner(inputStream));
    }

    @Test
    public void testPrintHelloMessage() {
        consoleIO.printHelloMessage();
        String output = outputStream.toString();
        assertTrue(output.contains("Добро пожаловать в игру Блекджек!"));
    }

    @Test
    public void testPrintRoundHeader() {
        consoleIO.printRoundHeader(3);
        String output = outputStream.toString();
        assertTrue(output.contains("-- Раунд 3 --"));
    }

    @Test
    public void testPrintDealerDealtCards() {
        consoleIO.printDealerDealtCards();
        String output = outputStream.toString();
        assertTrue(output.contains("Дилер раздал карты"));
    }

    @Test
    public void testPrintPlayerAndDealerCardsRevealHand() {
        Hand playerHand = new Hand();
        playerHand.addCard(new Card("Туз", "Черви"));
        playerHand.addCard(new Card("Король", "Пики"));

        Hand dealerHand = new Hand();
        dealerHand.addCard(new Card("Дама", "Крести"));
        dealerHand.addCard(new Card("Семерка", "Буби"));

        Participant player = new Participant();
        player.getHand().addCard(new Card("Туз", "Черви"));
        player.getHand().addCard(new Card("Король", "Пики"));

        Participant dealer = new Participant();
        dealer.getHand().addCard(new Card("Дама", "Крести"));
        dealer.getHand().addCard(new Card("Семерка", "Буби"));

        consoleIO.printPlayerAndDealerCards(player, dealer, true);
        String output = outputStream.toString();

        assertTrue(output.contains("Ваши карты:"));
        assertTrue(output.contains("Карты дилера:"));
        assertTrue(output.contains("Туз Черви"));
        assertTrue(output.contains("Король Пики"));
        assertTrue(output.contains("Дама Крести"));
        assertTrue(output.contains("Семерка Буби"));
    }

    @Test
    public void testPrintPlayerAndDealerCardsHiddenHand() {
        Participant player = new Participant();
        player.getHand().addCard(new Card("Туз", "Черви"));
        player.getHand().addCard(new Card("Король", "Пики"));

        Participant dealer = new Participant();
        dealer.getHand().addCard(new Card("Дама", "Крести"));
        dealer.getHand().addCard(new Card("Семерка", "Буби"));

        consoleIO.printPlayerAndDealerCards(player, dealer, false);
        String output = outputStream.toString();

        assertTrue(output.contains("Ваши карты:"));
        assertTrue(output.contains("Карты дилера:"));
        assertTrue(output.contains("Туз Черви"));
        assertTrue(output.contains("Король Пики"));
        assertTrue(output.contains("Дама Крести"));
        assertTrue(output.contains("<закрытая карта>"));
        assertFalse(output.contains("Семерка Буби"));
    }

    @Test
    public void testPrintPlayerHasBlackjack() {
        consoleIO.printPlayerHasBlackjack();
        String output = outputStream.toString();
        assertTrue(output.contains("У вас Блекджек! Вы выиграли!"));
    }

    @Test
    public void testPrintDealerHasBlackjack() {
        consoleIO.printDealerHasBlackjack();
        String output = outputStream.toString();
        assertTrue(output.contains("У дилера Блекджек! Вы проиграли."));
    }

    @Test
    public void testPrintPlayerTurn() {
        consoleIO.printPlayerTurn();
        String output = outputStream.toString();
        assertTrue(output.contains("Ваш ход"));
        assertTrue(output.contains("-------"));
    }

    @Test
    public void testPrintDealerTurn() {
        consoleIO.printDealerTurn();
        String output = outputStream.toString();
        assertTrue(output.contains("Ход дилера"));
        assertTrue(output.contains("-------"));
    }

    @Test
    public void testPrintHitOrStand() {
        consoleIO.printHitOrStand();
        String output = outputStream.toString();
        assertTrue(output.contains("Введите '1', чтобы взять карту, и '0', чтобы остановиться..."));
    }

    @Test
    public void testReadPlayerTurnChoiceHit() {
        ConsoleIO ioWithInput = createConsoleIOWithInput("1\n");
        boolean choice = ioWithInput.readPlayerTurnChoice();
        assertTrue(choice);
    }

    @Test
    public void testReadPlayerTurnChoiceStand() {
        ConsoleIO ioWithInput = createConsoleIOWithInput("0\n");
        boolean choice = ioWithInput.readPlayerTurnChoice();
        assertFalse(choice);
    }

    @Test
    public void testReadPlayerTurnChoiceInvalidThenValid() {
        ConsoleIO ioWithInput = createConsoleIOWithInput("invalid\n1\n");
        boolean choice = ioWithInput.readPlayerTurnChoice();
        assertTrue(choice);
        String output = outputStream.toString();
        assertTrue(output.contains("Некорректный ввод"));
    }

    @Test
    public void testReadPlayerNextRoundChoiceYes() {
        ConsoleIO ioWithInput = createConsoleIOWithInput("Да\n");
        boolean choice = ioWithInput.readPlayerNexRoundChoice();
        assertTrue(choice);
    }

    @Test
    public void testReadPlayerNextRoundChoiceNo() {
        ConsoleIO ioWithInput = createConsoleIOWithInput("Нет\n");
        boolean choice = ioWithInput.readPlayerNexRoundChoice();
        assertFalse(choice);
        String output = outputStream.toString();
        assertTrue(output.contains("Спасибо за игру!"));
    }

    @Test
    public void testReadPlayerNextRoundChoiceInvalidThenValid() {
        ConsoleIO ioWithInput = createConsoleIOWithInput("invalid\nДа\n");
        boolean choice = ioWithInput.readPlayerNexRoundChoice();
        assertTrue(choice);
        String output = outputStream.toString();
        assertTrue(output.contains("Некорректный ввод"));
    }

    @Test
    public void testPrintPlayerDrewCard() {
        Card card = new Card("Туз", "Черви");
        consoleIO.printPlayerDrewCard(card);
        String output = outputStream.toString();
        assertTrue(output.contains("Вы открыли карту Туз Черви"));
    }

    @Test
    public void testPrintDealerDrewCard() {
        Card card = new Card("Король", "Буби");
        consoleIO.printDealerDrewCard(card);
        String output = outputStream.toString();
        assertTrue(output.contains("Дилер открывает карту Король Буби"));
    }

    @Test
    public void testPrintDealerDrewClosedCard() {
        consoleIO.printDealerDrewClosedCard();
        String output = outputStream.toString();
        assertTrue(output.contains("Дилер открывает закрытую карту"));
    }

    @Test
    public void testPrintPlayerBust() {
        consoleIO.printPlayerBust();
        String output = outputStream.toString();
        assertTrue(output.contains("Перебор! Вы проиграли."));
    }

    @Test
    public void testPrintDealerBust() {
        consoleIO.printDealerBust();
        String output = outputStream.toString();
        assertTrue(output.contains("Дилер перебрал! Вы выиграли!"));
    }

    @Test
    public void testPrintPlayer21() {
        consoleIO.printPlayer21();
        String output = outputStream.toString();
        assertTrue(output.contains("У вас 21!"));
    }

    @Test
    public void testPrintDealerWon() {
        consoleIO.printDealerWon();
        String output = outputStream.toString();
        assertTrue(output.contains("Дилер выиграл."));
    }

    @Test
    public void testPrintPlayerWon() {
        consoleIO.printPlayerWon();
        String output = outputStream.toString();
        assertTrue(output.contains("Вы выиграли!"));
    }

    @Test
    public void testPrintPush() {
        consoleIO.printPush();
        String output = outputStream.toString();
        assertTrue(output.contains("Ничья."));
    }

    @Test
    public void testPrintRoundIsOver() {
        Participant player = new Participant();
        Participant dealer = new Participant();
        player.incScore();
        player.incScore();
        dealer.incScore();

        consoleIO.printRoundIsOver(player, dealer);
        String output = outputStream.toString();
        assertTrue(output.contains("Раунд окончен. Счёт 2:1"));
        assertTrue(output.contains("-------"));
    }

    @Test
    public void testPrintErrorShoeOutOfCards() {
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errorStream));

        try {
            consoleIO.printErrorShoeOutOfCards();
            String errorOutput = errorStream.toString();
            assertTrue(errorOutput.contains("Ошибка: В колоде больше нет карт"));
        } finally {
            System.setErr(originalErr);
        }
    }

    @Test
    public void testPrintErrorHandFull() {
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errorStream));

        try {
            consoleIO.printErrorHandFull();
            String errorOutput = errorStream.toString();
            assertTrue(errorOutput.contains("Ошибка: Рука переполнена"));
        } finally {
            System.setErr(originalErr);
        }
    }

    @Test
    public void testPrintErrorInvalidCardIndex() {
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errorStream));

        try {
            consoleIO.printErrorInvalidCardIndex();
            String errorOutput = errorStream.toString();
            assertTrue(errorOutput.contains("Ошибка: Неверный индекс карты"));
        } finally {
            System.setErr(originalErr);
        }
    }
}