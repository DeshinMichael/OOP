/**
 * Represents a shoe of multiple decks of cards used in Blackjack.
 * Manages card dealing and shuffling.
 */
public class Shoe {
    private final Card[] cards;
    private int currCardIndex;

    /**
     * Creates a new shoe with the specified number of decks.
     * Initializes all cards and shuffles the shoe.
     *
     * @param numDecks The number of decks to include in the shoe
     */
    public Shoe(int numDecks) {
        cards = new Card[numDecks * 52];
        currCardIndex = 0;

        String[] suits = {"Черви", "Буби", "Пики", "Крести"};
        String[] ranks = {"Двойка", "Тройка", "Четверка", "Пятерка", "Шестерка", "Семерка", "Восьмерка", "Девятка", "Десятка", "Валет", "Дама", "Король", "Туз"};

        int index = 0;
        for (int d = 0; d < numDecks; d++) {
            for (String suit : suits) {
                for (String rank : ranks) {
                    cards[index++] = new Card(rank, suit);
                }
            }
        }

        shuffle();
    }

    /**
     * Gets the array of cards in the shoe.
     *
     * @return The array of cards
     */
    public Card[] getCards() {
        return cards;
    }

    /**
     * Shuffles the cards in the shoe using the Fisher-Yates algorithm.
     */
    public void shuffle() {
        for (int i = cards.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            Card temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
    }

    /**
     * Deals a card from the shoe.
     *
     * @return The next card in the shoe
     * @throws IllegalStateException if there are no more cards in the shoe
     */
    public Card dealCard() {
        if (currCardIndex < cards.length) {
            return cards[currCardIndex++];
        } else {
            throw new IllegalStateException();
        }
    }
}
