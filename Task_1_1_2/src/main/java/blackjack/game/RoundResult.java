package blackjack.game;

/**
 * Enumeration of possible round results in blackjack.
 * Defines all possible outcomes of a player's turn or round state.
 */
public enum RoundResult {
    /**
     * Blackjack - natural 21 points with two cards.
     */
    BLACKJACK,

    /**
     * Bust - card sum exceeded 21.
     */
    BUST,

    /**
     * Game continues - intermediate state.
     */
    CONTINUE
}
