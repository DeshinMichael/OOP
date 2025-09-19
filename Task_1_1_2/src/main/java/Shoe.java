public class Shoe {
    private final Card[] cards;
    private int currCardIndex;

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

    public Card[] getCards() {
        return cards;
    }

    public void shuffle() {
        for (int i = cards.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            Card temp = cards[i];
            cards[i] = cards[j];
            cards[j] = temp;
        }
    }

    public Card dealCard() {
        if (currCardIndex < cards.length) {
            return cards[currCardIndex++];
        } else {
            throw new IllegalStateException();
        }
    }
}
