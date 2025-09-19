// Class representing a hand of cards with calculation of Blackjack values
public class Hand {
    private final Card[] cards;
    private int cardCount;

    // Constructor creating an empty hand with capacity for 12 cards
    public Hand() {
        cards = new Card[12];
        cardCount = 0;
    }

    // Adds a card to the hand, throws exception if hand is full
    public void addCard(Card card) {
        if (cardCount < cards.length) {
            cards[cardCount++] = card;
        } else {
            throw new IllegalStateException();
        }
    }

    // Gets card at specified index, throws exception if index is invalid
    public Card getCard(int index) {
        if (index >= 0 && index < cardCount) {
            return cards[index];
        } else {
            throw new IllegalArgumentException();
        }
    }

    // Returns the current number of cards in hand
    public int getCardCount() {
        return cardCount;
    }

    // Calculates the total Blackjack value of the hand, handling Aces appropriately
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

    // Clears all cards from the hand
    public void clear() {
        cardCount = 0;
    }

    // Converts hand to string representation listing all cards
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
