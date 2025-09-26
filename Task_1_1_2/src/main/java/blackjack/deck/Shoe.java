package blackjack.deck;

import blackjack.model.Card;
import blackjack.model.Rank;
import blackjack.model.Suit;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a shoe (deck container) of cards for blackjack game.
 * Can contain multiple standard decks of 52 cards, supports shuffling and dealing cards.
 */
public class Shoe {
    private final Card[] cards;
    private int currentIndex;

    /**
     * Creates a new shoe with the specified number of decks.
     *
     * @param numDecks the number of standard decks (52 cards each)
     */
    public Shoe(int numDecks) {
        List<Card> cardList = new ArrayList<>();

        // Create the specified number of decks
        for (int deck = 0; deck < numDecks; deck++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cardList.add(new Card(rank, suit));
                }
            }
        }

        // Shuffle the cards
        Collections.shuffle(cardList);

        this.cards = cardList.toArray(new Card[0]);
        this.currentIndex = 0;
    }

    /**
     * Shuffles the cards in the shoe.
     */
    public void shuffle() {
        List<Card> cardList = new ArrayList<>();
        for (Card card : cards) {
            cardList.add(card);
        }
        Collections.shuffle(cardList);

        for (int i = 0; i < cards.length; i++) {
            cards[i] = cardList.get(i);
        }
        currentIndex = 0;
    }

    /**
     * Deals one card from the shoe.
     *
     * @return the next card from the shoe
     * @throws IllegalStateException if the shoe is out of cards
     */
    public Card dealCard() {
        if (currentIndex >= cards.length) {
            throw new IllegalStateException("Shoe is out of cards");
        }
        return cards[currentIndex++];
    }

    /**
     * Returns the array of all cards in the shoe.
     *
     * @return the card array
     */
    public Card[] getCards() {
        return cards;
    }
}
