// Class representing a playing card with rank and suit
public class Card {
    private final String rank;
    private final String suit;

    // Constructor for creating a new card with specific rank and suit
    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    // Returns the rank of this card
    public String getRank() {
        return rank;
    }

    // Converts card to string format as "rank suit"
    @Override
    public String toString() {
        return rank + " " + suit;
    }
}
