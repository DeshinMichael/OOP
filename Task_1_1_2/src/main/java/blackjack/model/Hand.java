package blackjack.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a hand of cards in blackjack.
 * Manages a collection of cards and calculates the total value
 * of the hand according to blackjack rules.
 */
public class Hand {
    private static final int MAX_CARDS = 12;
    private final List<Card> cards;
    private Integer cachedValue;

    /**
     * Creates a new empty hand.
     */
    public Hand() {
        this.cards = new ArrayList<>();
        this.cachedValue = null;
    }

    /**
     * Adds a card to the hand.
     *
     * @param card the card to add
     * @throws IllegalStateException if the hand already contains the maximum number of cards
     */
    public void addCard(Card card) {
        if (cards.size() >= MAX_CARDS) {
            throw new IllegalStateException("Hand is full");
        }
        cards.add(card);
        invalidateCache();
    }

    /**
     * Returns the card at the specified index.
     *
     * @param index the index of the card
     * @return the card at the specified index
     * @throws IllegalArgumentException if the index is invalid
     */
    public Card getCard(int index) {
        if (index < 0 || index >= cards.size()) {
            throw new IllegalArgumentException("Invalid card index");
        }
        return cards.get(index);
    }

    /**
     * Returns the last added card.
     *
     * @return the last card in the hand
     * @throws IllegalArgumentException if the hand is empty
     */
    public Card getLastCard() {
        if (cards.isEmpty()) {
            throw new IllegalArgumentException("Hand is empty");
        }
        return cards.get(cards.size() - 1);
    }

    /**
     * Returns the number of cards in the hand.
     *
     * @return the number of cards
     */
    public int getCardCount() {
        return cards.size();
    }

    private void invalidateCache() {
        cachedValue = null;
    }

    /**
     * Calculates the total value of the hand according to blackjack rules.
     * Aces are counted as 11, but can be reduced to 1 to avoid busting.
     *
     * @return the total value of the hand
     */
    public int getValue() {
        if (cachedValue != null) {
            return cachedValue;
        }

        int value = 0;
        int aces = 0;

        for (Card card : cards) {
            value += card.getValue();
            if (card.rank() == Rank.ACE) {
                aces++;
            }
        }

        while (value > 21 && aces > 0) {
            value -= 10;
            aces--;
        }

        cachedValue = value;
        return value;
    }

    /**
     * Returns the actual value of a specific card in the context of this hand.
     * For aces, calculates whether the card should count as 1 or 11 points.
     *
     * @param index the index of the card in the hand
     * @return the actual value of the card in this hand's context
     */
    public int getActualCardValue(int index) {
        Card card = getCard(index);

        if (card.rank() != Rank.ACE) {
            return card.getValue();
        }

        int baseSum = 0;
        int acesCount = 0;
        for (Card c : cards) {
            if (c.rank() == Rank.ACE) {
                baseSum += 1;
                acesCount++;
            } else {
                baseSum += c.getValue();
            }
        }

        int acesAs11 = 0;
        while (acesCount > acesAs11 && baseSum + 10 <= 21) {
            baseSum += 10;
            acesAs11++;
        }

        int currentAcePosition = 0;
        for (int i = 0; i <= index; i++) {
            if (cards.get(i).rank() == Rank.ACE) {
                currentAcePosition++;
            }
        }

        return (currentAcePosition <= acesAs11) ? 11 : 1;
    }

    /**
     * Clears the hand, removing all cards.
     */
    public void clear() {
        cards.clear();
    }

    /**
     * Returns the string representation of the hand.
     * Cards are separated by commas.
     *
     * @return string representation of the hand
     */
    @Override
    public String toString() {
        if (cards.isEmpty()) {
            return "Empty hand";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(cards.get(i).toString());
            sb.append(" (").append(getActualCardValue(i)).append(")");
        }
        return sb.toString();
    }
}
