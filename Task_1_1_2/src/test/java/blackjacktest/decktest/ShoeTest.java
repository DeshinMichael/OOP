package blackjacktest.decktest;

import blackjack.deck.Shoe;
import blackjack.model.Card;
import blackjack.model.Rank;
import blackjack.model.Suit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
     */
    @Test
    public void testShoeCreation() {
        Shoe singleDeckShoe = new Shoe(1);
        List<Card> cards = singleDeckShoe.getCards();

        assertEquals(52, cards.size());
        assertNotNull(cards.getFirst());
        assertNotNull(cards.get(51));
    }

    /**
     * Tests that a blackjack.deck.Shoe object can be created properly with multiple decks.
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 4, 6, 8})
    void testConstructorMultipleDecks(int numDecks) {
        Shoe multiDeckShoe = new Shoe(numDecks);
        List<Card> cards = multiDeckShoe.getCards();

        assertEquals(52 * numDecks, cards.size());

        for (Card card : cards) {
            assertNotNull(card);
        }
    }

    /**
     * Tests that cards can be accessed.
     */
    @Test
    public void testGetCards() {
        List<Card> cards = shoe.getCards();
        assertNotNull(cards);
        assertEquals(52, cards.size());

        List<Card> sameCards = shoe.getCards();
        assertSame(cards, sameCards);
    }

    /**
     * Tests that cards can be dealt from the shoe.
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
     */
    @Test
    public void testDealAllCards() {
        Shoe smallShoe = new Shoe(1);

        Set<Card> dealtCards = new HashSet<>();
        for (int i = 0; i < 52; i++) {
            Card card = smallShoe.dealCard();
            assertNotNull(card);
            dealtCards.add(card);
        }

        assertEquals(52, dealtCards.size(), "Все карты должны быть уникальными");
        assertThrows(IllegalStateException.class, smallShoe::dealCard);
    }

    /**
     * Tests that dealing from an empty shoe throws an exception.
     */
    @Test
    public void testDealCardFromEmptyShoe() {
        for (int i = 0; i < 52; i++) {
            shoe.dealCard();
        }

        assertThrows(IllegalStateException.class, shoe::dealCard);
    }

    /**
     * Tests that all ranks and suits are present in the shoe.
     */
    @Test
    void testAllRanksAndSuitsPresent() {
        List<Card> cards = shoe.getCards();

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
     */
    @Test
    void testCorrectNumberOfEachCard() {
        Shoe multiDeckShoe = new Shoe(2);
        List<Card> cards = multiDeckShoe.getCards();

        int aceOfHeartsCount = 0;
        int kingOfSpadesCount = 0;
        for (Card card : cards) {
            if (card.rank() == Rank.ACE && card.suit() == Suit.HEARTS) {
                aceOfHeartsCount++;
            }
            if (card.rank() == Rank.KING && card.suit() == Suit.SPADES) {
                kingOfSpadesCount++;
            }
        }

        assertEquals(2, aceOfHeartsCount, "Должно быть ровно 2 туза червей в двух колодах");
        assertEquals(2, kingOfSpadesCount, "Должно быть ровно 2 короля пик в двух колодах");
    }

    /**
     * Tests dealing multiple cards and checking uniqueness
     */
    @Test
    void testDealingMultipleCards() {
        Set<Card> dealtCards = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            Card card = shoe.dealCard();
            assertNotNull(card);
            dealtCards.add(card);
        }

        assertEquals(10, dealtCards.size(), "Все 10 карт должны быть уникальными");
    }
}
