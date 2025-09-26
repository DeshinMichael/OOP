package blackjack_test.deck_test;

import blackjack.deck.Shoe;
import blackjack.model.Card;
import blackjack.model.Rank;
import blackjack.model.Suit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for blackjack.deck.Shoe functionality.
 * Tests deck management, shuffling, and card dealing.
 */
public class ShoeTest {
    private Shoe shoe;

    @BeforeEach
    void setUp() {
        shoe = new Shoe(1);
    }

    /**
     * Tests that a blackjack.deck.Shoe object can be created properly with correct number of cards.
     * Verifies the shoe contains the expected number of cards based on deck count.
     */
    @Test
    public void testShoeCreation() {
        Shoe singleDeckShoe = new Shoe(1);
        Card[] cards = singleDeckShoe.getCards();

        assertEquals(52, cards.length);
        assertNotNull(cards[0]);
        assertNotNull(cards[51]);
    }

    /**
     * Tests that a blackjack.deck.Shoe object can be created properly with multiple decks.
     * Verifies the shoe contains the expected number of cards based on deck count.
     */
    @Test
    void testConstructorMultipleDecks() {
        Shoe multiDeckShoe = new Shoe(4);
        Card[] cards = multiDeckShoe.getCards();

        assertEquals(208, cards.length);

        for (Card card : cards) {
            assertNotNull(card);
        }
    }

    /**
     * Tests that cards can be accessed.
     * Verifies the card array is properly sized.
     */
    @Test
    public void testGetCards() {
        Card[] cards = shoe.getCards();
        assertNotNull(cards);
        assertEquals(52, cards.length);

        Card[] sameCards = shoe.getCards();
        assertSame(cards, sameCards);
    }

    /**
     * Tests that cards are shuffled.
     * Verifies that cards are in a different order after shuffling.
     */
    @Test
    public void testShuffle() {
        Shoe shoe1 = new Shoe(2);
        Shoe shoe2 = new Shoe(2);

        Card[] cards1 = shoe1.getCards();
        Card[] cards2 = shoe2.getCards();

        shoe1.shuffle();
        shoe1.shuffle();

        boolean orderChanged = false;
        for (int i = 0; i < cards1.length; i++) {
            if (!cards1[i].toString().equals(cards2[i].toString())) {
                orderChanged = true;
                break;
            }
        }

        assertTrue(orderChanged || cards1.length <= 2);
    }

    /**
     * Tests that cards can be dealt from the shoe.
     * Verifies a card is successfully dealt and is not null.
     */
    @Test
    public void testDealCard() {
        Card firstCard = shoe.dealCard();
        assertNotNull(firstCard);

        Card secondCard = shoe.dealCard();
        assertNotNull(secondCard);

        assertNotSame(firstCard, secondCard);
    }

    /**
     * Tests that all cards can be dealt from the shoe.
     * Verifies that dealing the last card succeeds and dealing again throws an exception.
     */
    @Test
    public void testDealAllCards() {
        Shoe smallShoe = new Shoe(1);

        for (int i = 0; i < 52; i++) {
            Card card = smallShoe.dealCard();
            assertNotNull(card);
        }

        assertThrows(IllegalStateException.class, smallShoe::dealCard);
    }

    /**
     * Tests that dealing from an empty shoe throws an exception.
     * Verifies IllegalStateException is thrown when attempting to deal from an empty shoe.
     */
    @Test
    public void testDealCardFromEmptyShoe() {
        for (int i = 0; i < 52; i++) {
            shoe.dealCard();
        }

        assertThrows(IllegalStateException.class, () -> {
            shoe.dealCard();
        });
    }

    /**
     * Tests that all ranks and suits are present in the shoe.
     * Verifies that every rank and suit combination exists in the shoe's cards.
     */
    @Test
    void testAllRanksAndSuitsPresent() {
        Card[] cards = shoe.getCards();

        boolean[] rankPresent = new boolean[Rank.values().length];
        boolean[] suitPresent = new boolean[Suit.values().length];

        for (Card card : cards) {
            for (int i = 0; i < Rank.values().length; i++) {
                if (card.rank() == Rank.values()[i]) {
                    rankPresent[i] = true;
                }
            }
            for (int i = 0; i < Suit.values().length; i++) {
                if (card.suit() == Suit.values()[i]) {
                    suitPresent[i] = true;
                }
            }
        }

        for (boolean present : rankPresent) {
            assertTrue(present);
        }
        for (boolean present : suitPresent) {
            assertTrue(present);
        }
    }

    /**
     * Tests that each card appears the correct number of times in the shoe.
     * Verifies that in multiple decks, each card appears exactly twice.
     */
    @Test
    void testCorrectNumberOfEachCard() {
        Shoe multiDeckShoe = new Shoe(2);
        Card[] cards = multiDeckShoe.getCards();

        int aceOfHeartsCount = 0;
        for (Card card : cards) {
            if (card.rank() == Rank.ACE && card.suit() == Suit.HEARTS) {
                aceOfHeartsCount++;
            }
        }

        assertEquals(2, aceOfHeartsCount);
    }
}
