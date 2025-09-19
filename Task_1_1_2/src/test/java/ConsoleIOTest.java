import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for ConsoleIO functionality.
 * Tests all input and output methods of the ConsoleIO class.
 */
public class ConsoleIOTest {
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private ConsoleIO consoleIo;

    /**
     * Setup method that runs before each test.
     * Redirects standard output and creates a ConsoleIO instance.
     */
    @BeforeEach
    public void setUp() {
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        consoleIo = new ConsoleIO(new Scanner(""));
    }

    /**
     * Cleanup method that runs after each test.
     * Restores the standard output stream.
     */
    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }

    /**
     * Helper method to create ConsoleIO with predefined input.
     *
     * @param input The input string to simulate user input
     * @return A ConsoleIO instance configured with the provided input
     */
    private ConsoleIO createConsoleIoWithInput(String input) {
        InputStream inputStream = new ByteArrayInputStream(input.getBytes());
        return new ConsoleIO(new Scanner(inputStream));
    }

    /**
     * Tests that welcome message is printed correctly.
     * Verifies the welcome message contains the expected text.
     */
    @Test
    public void testPrintHelloMessage() {
        consoleIo.printHelloMessage();
        String output = outputStream.toString();
        assertTrue(output.contains("Добро пожаловать в игру Блекджек!"));
    }

    /**
     * Tests that round header is printed correctly.
     * Verifies the round header contains the expected round number.
     */
    @Test
    public void testPrintRoundHeader() {
        consoleIo.printRoundHeader(3);
        String output = outputStream.toString();
        assertTrue(output.contains("-- Раунд 3 --"));
    }

    /**
     * Tests that dealer dealt cards message is printed correctly.
     * Verifies the message contains the expected text.
     */
    @Test
    public void testPrintDealerDealtCards() {
        consoleIo.printDealerDealtCards();
        String output = outputStream.toString();
        assertTrue(output.contains("Дилер раздал карты"));
    }

    /**
     * Tests that player and dealer cards are shown correctly with revealed hand.
     * Verifies all cards are visible when dealer's hand is revealed.
     */
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

        consoleIo.printPlayerAndDealerCards(player, dealer, true);
        String output = outputStream.toString();

        assertTrue(output.contains("Ваши карты:"));
        assertTrue(output.contains("Карты дилера:"));
        assertTrue(output.contains("Туз Черви"));
        assertTrue(output.contains("Король Пики"));
        assertTrue(output.contains("Дама Крести"));
        assertTrue(output.contains("Семерка Буби"));
    }

    /**
     * Tests that player and dealer cards are shown correctly with hidden dealer card.
     * Verifies dealer's second card is hidden when dealer's hand is not revealed.
     */
    @Test
    public void testPrintPlayerAndDealerCardsHiddenHand() {
        Participant player = new Participant();
        player.getHand().addCard(new Card("Туз", "Черви"));
        player.getHand().addCard(new Card("Король", "Пики"));

        Participant dealer = new Participant();
        dealer.getHand().addCard(new Card("Дама", "Крести"));
        dealer.getHand().addCard(new Card("Семерка", "Буби"));

        consoleIo.printPlayerAndDealerCards(player, dealer, false);
        String output = outputStream.toString();

        assertTrue(output.contains("Ваши карты:"));
        assertTrue(output.contains("Карты дилера:"));
        assertTrue(output.contains("Туз Черви"));
        assertTrue(output.contains("Король Пики"));
        assertTrue(output.contains("Дама Крести"));
        assertTrue(output.contains("<закрытая карта>"));
        assertFalse(output.contains("Семерка Буби"));
    }

    /**
     * Tests that player blackjack message is printed correctly.
     * Verifies the message contains the expected text.
     */
    @Test
    public void testPrintPlayerHasBlackjack() {
        consoleIo.printPlayerHasBlackjack();
        String output = outputStream.toString();
        assertTrue(output.contains("У вас Блекджек! Вы выиграли!"));
    }

    /**
     * Tests that dealer blackjack message is printed correctly.
     * Verifies the message contains the expected text.
     */
    @Test
    public void testPrintDealerHasBlackjack() {
        consoleIo.printDealerHasBlackjack();
        String output = outputStream.toString();
        assertTrue(output.contains("У дилера Блекджек! Вы проиграли."));
    }

    /**
     * Tests that player turn announcement is printed correctly.
     * Verifies the announcement contains the expected text.
     */
    @Test
    public void testPrintPlayerTurn() {
        consoleIo.printPlayerTurn();
        String output = outputStream.toString();
        assertTrue(output.contains("Ваш ход"));
        assertTrue(output.contains("-------"));
    }

    /**
     * Tests that dealer turn announcement is printed correctly.
     * Verifies the announcement contains the expected text.
     */
    @Test
    public void testPrintDealerTurn() {
        consoleIo.printDealerTurn();
        String output = outputStream.toString();
        assertTrue(output.contains("Ход дилера"));
        assertTrue(output.contains("-------"));
    }

    /**
     * Tests that hit or stand prompt is printed correctly.
     * Verifies the prompt contains the expected text.
     */
    @Test
    public void testPrintHitOrStand() {
        consoleIo.printHitOrStand();
        String output = outputStream.toString();
        assertTrue(output.contains("Введите '1', чтобы взять карту, и '0', чтобы остановиться..."));
    }

    /**
     * Tests that player's choice to hit is read correctly.
     * Verifies that input "1" returns true.
     */
    @Test
    public void testReadPlayerTurnChoiceHit() {
        ConsoleIO ioWithInput = createConsoleIoWithInput("1\n");
        boolean choice = ioWithInput.readPlayerTurnChoice();
        assertTrue(choice);
    }

    /**
     * Tests that player's choice to stand is read correctly.
     * Verifies that input "0" returns false.
     */
    @Test
    public void testReadPlayerTurnChoiceStand() {
        ConsoleIO ioWithInput = createConsoleIoWithInput("0\n");
        boolean choice = ioWithInput.readPlayerTurnChoice();
        assertFalse(choice);
    }

    /**
     * Tests that invalid input is handled correctly before accepting valid input.
     * Verifies that after invalid input, the method continues to prompt until valid input.
     */
    @Test
    public void testReadPlayerTurnChoiceInvalidThenValid() {
        ConsoleIO ioWithInput = createConsoleIoWithInput("invalid\n1\n");
        boolean choice = ioWithInput.readPlayerTurnChoice();
        assertTrue(choice);
        String output = outputStream.toString();
        assertTrue(output.contains("Некорректный ввод"));
    }

    /**
     * Tests that player's choice to play another round is read correctly.
     * Verifies that input "Да" returns true.
     */
    @Test
    public void testReadPlayerNextRoundChoiceYes() {
        ConsoleIO ioWithInput = createConsoleIoWithInput("Да\n");
        boolean choice = ioWithInput.readPlayerNexRoundChoice();
        assertTrue(choice);
    }

    /**
     * Tests that player's choice to end the game is read correctly.
     * Verifies that input "Нет" returns false and displays thank you message.
     */
    @Test
    public void testReadPlayerNextRoundChoiceNo() {
        ConsoleIO ioWithInput = createConsoleIoWithInput("Нет\n");
        boolean choice = ioWithInput.readPlayerNexRoundChoice();
        assertFalse(choice);
        String output = outputStream.toString();
        assertTrue(output.contains("Спасибо за игру!"));
    }

    /**
     * Tests that invalid input is handled correctly before accepting valid round choice.
     * Verifies that after invalid input, the method continues to prompt until valid input.
     */
    @Test
    public void testReadPlayerNextRoundChoiceInvalidThenValid() {
        ConsoleIO ioWithInput = createConsoleIoWithInput("invalid\nДа\n");
        boolean choice = ioWithInput.readPlayerNexRoundChoice();
        assertTrue(choice);
        String output = outputStream.toString();
        assertTrue(output.contains("Некорректный ввод"));
    }

    /**
     * Tests that player drawing card message is printed correctly.
     * Verifies the message contains the expected card information.
     */
    @Test
    public void testPrintPlayerDrewCard() {
        Card card = new Card("Туз", "Черви");
        consoleIo.printPlayerDrewCard(card);
        String output = outputStream.toString();
        assertTrue(output.contains("Вы открыли карту Туз Черви"));
    }

    /**
     * Tests that dealer drawing card message is printed correctly.
     * Verifies the message contains the expected card information.
     */
    @Test
    public void testPrintDealerDrewCard() {
        Card card = new Card("Король", "Буби");
        consoleIo.printDealerDrewCard(card);
        String output = outputStream.toString();
        assertTrue(output.contains("Дилер открывает карту Король Буби"));
    }

    /**
     * Tests that dealer revealing closed card message is printed correctly.
     * Verifies the message contains the expected text.
     */
    @Test
    public void testPrintDealerDrewClosedCard() {
        consoleIo.printDealerDrewClosedCard();
        String output = outputStream.toString();
        assertTrue(output.contains("Дилер открывает закрытую карту"));
    }

    /**
     * Tests that player bust message is printed correctly.
     * Verifies the message contains the expected text.
     */
    @Test
    public void testPrintPlayerBust() {
        consoleIo.printPlayerBust();
        String output = outputStream.toString();
        assertTrue(output.contains("Перебор! Вы проиграли."));
    }

    /**
     * Tests that dealer bust message is printed correctly.
     * Verifies the message contains the expected text.
     */
    @Test
    public void testPrintDealerBust() {
        consoleIo.printDealerBust();
        String output = outputStream.toString();
        assertTrue(output.contains("Дилер перебрал! Вы выиграли!"));
    }

    /**
     * Tests that player 21 message is printed correctly.
     * Verifies the message contains the expected text.
     */
    @Test
    public void testPrintPlayer21() {
        consoleIo.printPlayer21();
        String output = outputStream.toString();
        assertTrue(output.contains("У вас 21!"));
    }

    /**
     * Tests that dealer win message is printed correctly.
     * Verifies the message contains the expected text.
     */
    @Test
    public void testPrintDealerWon() {
        consoleIo.printDealerWon();
        String output = outputStream.toString();
        assertTrue(output.contains("Дилер выиграл."));
    }

    /**
     * Tests that player win message is printed correctly.
     * Verifies the message contains the expected text.
     */
    @Test
    public void testPrintPlayerWon() {
        consoleIo.printPlayerWon();
        String output = outputStream.toString();
        assertTrue(output.contains("Вы выиграли!"));
    }

    /**
     * Tests that push (tie) message is printed correctly.
     * Verifies the message contains the expected text.
     */
    @Test
    public void testPrintPush() {
        consoleIo.printPush();
        String output = outputStream.toString();
        assertTrue(output.contains("Ничья."));
    }

    /**
     * Tests that end of round message with scores is printed correctly.
     * Verifies the message contains the correct scores.
     */
    @Test
    public void testPrintRoundIsOver() {
        Participant player = new Participant();
        Participant dealer = new Participant();
        player.incScore();
        player.incScore();
        dealer.incScore();

        consoleIo.printRoundIsOver(player, dealer);
        String output = outputStream.toString();
        assertTrue(output.contains("Раунд окончен. Счёт 2:1"));
        assertTrue(output.contains("-------"));
    }

    /**
     * Tests that shoe out of cards error message is printed correctly.
     * Verifies the error message contains the expected text.
     */
    @Test
    public void testPrintErrorShoeOutOfCards() {
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errorStream));

        try {
            consoleIo.printErrorShoeOutOfCards();
            String errorOutput = errorStream.toString();
            assertTrue(errorOutput.contains("Ошибка: В колоде больше нет карт"));
        } finally {
            System.setErr(originalErr);
        }
    }

    /**
     * Tests that hand full error message is printed correctly.
     * Verifies the error message contains the expected text.
     */
    @Test
    public void testPrintErrorHandFull() {
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errorStream));

        try {
            consoleIo.printErrorHandFull();
            String errorOutput = errorStream.toString();
            assertTrue(errorOutput.contains("Ошибка: Рука переполнена"));
        } finally {
            System.setErr(originalErr);
        }
    }

    /**
     * Tests that invalid card index error message is printed correctly.
     * Verifies the error message contains the expected text.
     */
    @Test
    public void testPrintErrorInvalidCardIndex() {
        PrintStream originalErr = System.err;
        ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errorStream));

        try {
            consoleIo.printErrorInvalidCardIndex();
            String errorOutput = errorStream.toString();
            assertTrue(errorOutput.contains("Ошибка: Неверный индекс карты"));
        } finally {
            System.setErr(originalErr);
        }
    }
}