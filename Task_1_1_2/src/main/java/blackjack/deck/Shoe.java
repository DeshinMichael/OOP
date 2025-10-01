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
    private final List<Card> cards;
    private int currentIndex;

    /**
     * Creates a new shoe with the specified number of decks.
     *
     * @param numDecks the number of standard decks (52 cards each)
     */
    public Shoe(int numDecks) {
        this.cards = new ArrayList<>();

        for (int deck = 0; deck < numDecks; deck++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(rank, suit));
                }
            }
        }

        Collections.shuffle(cards);

        this.currentIndex = 0;
    }

    /**
     * Deals one card from the shoe.
     *
     * @return the next card from the shoe
     * @throws IllegalStateException if the shoe is out of cards
     */
    public Card dealCard() {
        if (currentIndex >= cards.size()) {
            throw new IllegalStateException();
        }
        return cards.get(currentIndex++);
    }

    /**
     * Returns the list of all cards in the shoe.
     *
     * @return the card list
     */
    public List<Card> getCards() {
        return cards;
    }

    public int getRemainingCards() {
        return cards.size() - currentIndex;
    }
}
