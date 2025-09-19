public class Hand {
    private final Card[] cards;
    private int cardCount;

    public Hand() {
        cards = new Card[12];
        cardCount = 0;
    }

    public void addCard(Card card) {
        if (cardCount < cards.length) {
            cards[cardCount++] = card;
        } else {
            throw new IllegalStateException();
        }
    }

    public Card getCard(int index) {
        if (index >= 0 && index < cardCount) {
            return cards[index];
        } else {
            throw new IllegalArgumentException();
        }
    }

    public int getCardCount() {
        return cardCount;
    }

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

    public void clear() {
        cardCount = 0;
    }

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
