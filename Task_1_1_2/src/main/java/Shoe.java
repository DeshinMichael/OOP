// Class representing a shoe containing multiple decks of cards
public class Shoe {
    private final Card[] cards;
    private int currCardIndex;

    // Constructor initializing shoe with specified number of decks and shuffling them
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

    // Returns the array of all cards in the shoe
    public Card[] getCards() {
        return cards;
    }

    // Shuffles the cards in the shoe using Fisher-Yates algorithm
    public void shuffle() {
        for (int i = cards.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            Card temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
    }

    // Deals the next card from the shoe, throws exception if no cards left
    public Card dealCard() {
        if (currCardIndex < cards.length) {
            return cards[currCardIndex++];
        } else {
            throw new IllegalStateException();
        }
    }
}
