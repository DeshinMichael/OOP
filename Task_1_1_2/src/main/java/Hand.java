/**
 * Represents a hand of cards in the Blackjack game.
 * Manages card storage and calculates the hand value.
 */
public class Hand {
    private final Card[] cards;
    private int cardCount;

    /**
     * Creates a new empty hand with capacity for 12 cards.
     */
    public Hand() {
        cards = new Card[12];
        cardCount = 0;
    }

    /**
     * Adds a card to the hand.
     *
     * @param card The card to add
     * @throws IllegalStateException if the hand is already full
     */
    public void addCard(Card card) {
        if (cardCount < cards.length) {
            cards[cardCount++] = card;
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * Gets a card from the hand at the specified index.
     *
     * @param index The index of the card to retrieve
     * @return The card at the specified index
     * @throws IllegalArgumentException if the index is invalid
     */
    public Card getCard(int index) {
        if (index >= 0 && index < cardCount) {
            return cards[index];
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Gets the number of cards in the hand.
     *
     * @return The number of cards
     */
    public int getCardCount() {
        return cardCount;
    }

    /**
     * Calculates the Blackjack value of the hand.
     * Accounts for Aces being worth 1 or 11 points.
     *
     * @return The total value of the hand
     */
    public int getValue() {
        int val = 0;
        int aceCount = 0;

        for (int i = 0; i < cardCount; i++) {
            String rank = cards[i].getRank();
            switch (rank) {
                case "Туз":
                    val += 11;
                    aceCount++;
                    break;
                case "Король":
                case "Дама":
                case "Валет":
                case "Десятка":
                    val += 10;
                    break;
                case "Двойка":
                    val += 2;
                    break;
                case "Тройка":
                    val += 3;
                    break;
                case "Четверка":
                    val += 4;
                    break;
                case "Пятерка":
                    val += 5;
                    break;
                case "Шестерка":
                    val += 6;
                    break;
                case "Семерка":
                    val += 7;
                    break;
                case "Восьмерка":
                    val += 8;
                    break;
                case "Девятка":
                    val += 9;
                    break;
            }
        }

        if (val > 21 && aceCount > 0) {
            val -= 10 * aceCount;
        }

        return val;
    }

    /**
     * Removes all cards from the hand.
     */
    public void clear() {
        cardCount = 0;
    }

    /**
     * Returns a string representation of the hand.
     *
     * @return A string listing all the cards in the hand
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cardCount; i++) {
            sb.append(cards[i].toString());
            if (i < cardCount - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }
}
