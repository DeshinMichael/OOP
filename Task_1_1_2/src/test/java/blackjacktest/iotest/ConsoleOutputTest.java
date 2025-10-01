package blackjacktest.iotest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import blackjack.i18n.I18nManager;
import blackjack.io.ConsoleOutput;
import blackjack.model.Card;
import blackjack.model.Hand;
import blackjack.model.Rank;
import blackjack.model.Suit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsoleOutputTest {
    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final I18nManager i18n = I18nManager.getInstance();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
        outputStreamCaptor.reset();
    }

    @Test
    void testPrintHelloMessage() {
        ConsoleOutput.printHelloMessage();
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("game.welcome")));
    }

    @Test
    void testPrintRoundHeader() {
        int roundNumber = 3;
        ConsoleOutput.printRoundHeader(roundNumber);
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("game.round.header", roundNumber)));
    }

    @Test
    void testPrintDealerDealtCards() {
        ConsoleOutput.printDealerDealtCards();
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("game.dealer.dealt")));
    }

    @Test
    void testPrintPlayerCards() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.ACE, Suit.HEARTS));
        hand.addCard(new Card(Rank.TEN, Suit.DIAMONDS));

        ConsoleOutput.printPlayerCards(hand);
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("hand.player", hand.toString(), hand.getValue())));
    }

    @Test
    void testPrintDealerCards() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.KING, Suit.CLUBS));
        hand.addCard(new Card(Rank.SEVEN, Suit.SPADES));

        ConsoleOutput.printDealerCards(hand);
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("hand.dealer", hand.toString(), hand.getValue())));
    }

    @Test
    void testPrintDealerClosedCards() {
        Hand hand = new Hand();
        hand.addCard(new Card(Rank.QUEEN, Suit.HEARTS));
        hand.addCard(new Card(Rank.FIVE, Suit.CLUBS));

        ConsoleOutput.printDealerClosedCards(hand);
        Card firstCard = hand.getCard(0);
        assertTrue(outputStreamCaptor.toString().contains(
            i18n.getString("hand.dealer.closed", firstCard.toString(), firstCard.getValue())));
    }

    @Test
    void testPrintPlayerHasBlackjack() {
        ConsoleOutput.printPlayerHasBlackjack();
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("blackjack.player")));
    }

    @Test
    void testPrintDealerHasBlackjack() {
        ConsoleOutput.printDealerHasBlackjack();
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("blackjack.dealer")));
    }

    @Test
    void testPrintPlayerTurn() {
        ConsoleOutput.printPlayerTurn();
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("turn.player")));
    }

    @Test
    void testPrintDealerTurn() {
        ConsoleOutput.printDealerTurn();
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("turn.dealer")));
    }

    @Test
    void testPrintHitOrStand() {
        ConsoleOutput.printHitOrStand();
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("game.hit.or.stand")));
    }

    @Test
    void testPrintPlayerDrewCard() {
        Card card = new Card(Rank.EIGHT, Suit.DIAMONDS);
        ConsoleOutput.printPlayerDrewCard(card);
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("player.drew.card", card.toString())));
    }

    @Test
    void testPrintDealerDrewCard() {
        Card card = new Card(Rank.THREE, Suit.CLUBS);
        ConsoleOutput.printDealerDrewCard(card);
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("dealer.drew.card", card.toString())));
    }

    @Test
    void testPrintDealerDrewClosedCard() {
        ConsoleOutput.printDealerDrewClosedCard();
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("dealer.drew.closed")));
    }

    @Test
    void testPrintPlayerBust() {
        ConsoleOutput.printPlayerBust();
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("result.player.busted")));
    }

    @Test
    void testPrintPlayer21() {
        ConsoleOutput.printPlayer21();
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("player.21")));
    }

    @Test
    void testPrintErrorHandFull() {
        ConsoleOutput.printErrorHandFull();
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("error.hand.full")));
    }

    @Test
    void testPrintErrorShoeOutOfCards() {
        ConsoleOutput.printErrorShoeOutOfCards();
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("error.shoe.empty")));
    }

    @Test
    void testPrintErrorInvalidCardIndex() {
        ConsoleOutput.printErrorInvalidCardIndex();
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("error.invalid.card.index")));
    }

    @ParameterizedTest
    @CsvSource({"3,2", "0,0", "10,5"})
    void testPrintScore(int playerScore, int dealerScore) {
        ConsoleOutput.printScore(playerScore, dealerScore);
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("score.current", playerScore, dealerScore)));
    }

    @Test
    void testPrintFinalResults_PlayerWins() {
        ConsoleOutput.printFinalResults(5, 3);
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains(i18n.getString("score.final", 5, 3)));
        assertTrue(output.contains(i18n.getString("final.player.wins")));
        assertTrue(output.contains(i18n.getString("final.thanks")));
    }

    @Test
    void testPrintFinalResults_DealerWins() {
        ConsoleOutput.printFinalResults(2, 4);
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains(i18n.getString("score.final", 2, 4)));
        assertTrue(output.contains(i18n.getString("final.dealer.wins")));
        assertTrue(output.contains(i18n.getString("final.thanks")));
    }

    @Test
    void testPrintFinalResults_Draw() {
        ConsoleOutput.printFinalResults(3, 3);
        String output = outputStreamCaptor.toString();
        assertTrue(output.contains(i18n.getString("score.final", 3, 3)));
        assertTrue(output.contains(i18n.getString("final.tie")));
        assertTrue(output.contains(i18n.getString("final.thanks")));
    }

    @Test
    void testPrintDealerBust() {
        ConsoleOutput.printDealerBust();
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("result.dealer.busted")));
    }

    @ParameterizedTest
    @CsvSource({"20,18", "21,17", "19,16"})
    void testPrintPlayerWins(int playerValue, int dealerValue) {
        ConsoleOutput.printPlayerWins(playerValue, dealerValue);
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("result.player.wins", playerValue, dealerValue)));
    }

    @ParameterizedTest
    @CsvSource({"19,17", "21,19", "20,15"})
    void testPrintDealerWins(int dealerValue, int playerValue) {
        ConsoleOutput.printDealerWins(dealerValue, playerValue);
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("result.dealer.wins", dealerValue, playerValue)));
    }

    @ParameterizedTest
    @CsvSource({"17", "18", "19", "20", "21"})
    void testPrintDraw(int value) {
        ConsoleOutput.printDraw(value);
        assertTrue(outputStreamCaptor.toString()
            .contains(i18n.getString("result.tie", value, value)));
    }
}
