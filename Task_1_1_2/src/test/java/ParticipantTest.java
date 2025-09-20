import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Test class for Participant functionality.
 * Tests the player/dealer representation and score tracking.
 */
public class ParticipantTest {

    /**
     * Tests that a Participant object can be created properly.
     * Verifies the participant and its hand are not null after creation.
     */
    @Test
    public void testCreationParticipant() {
        Participant participant = new Participant();
        assertNotNull(participant);
        assertNotNull(participant.getHand());
    }

    /**
     * Tests that hand can be accessed.
     * Verifies the hand is not null when retrieved.
     */
    @Test
    public void testGetHand() {
        Participant participant = new Participant();
        assertNotNull(participant.getHand());
    }

    /**
     * Tests that initial score is zero.
     * Verifies the score starts at zero for a new participant.
     */
    @Test
    public void testInitialScoreIsZero() {
        Participant participant = new Participant();
        assertEquals(0, participant.getScore());
    }

    /**
     * Tests that score can be incremented.
     * Verifies the score increases correctly when incremented.
     */
    @Test
    public void testIncrementScore() {
        Participant participant = new Participant();
        participant.incScore();
        assertEquals(1, participant.getScore());
        participant.incScore();
        assertEquals(2, participant.getScore());
    }

    /**
     * Tests that hand value is calculated correctly.
     * Verifies the hand value matches the sum of the card values.
     */
    @Test
    public void testGetHandValue() {
        Participant participant = new Participant();
        assertEquals(0, participant.getHandValue());
        participant.getHand().addCard(new Card("Пятерка", "Черви"));
        assertEquals(5, participant.getHandValue());
        participant.getHand().addCard(new Card("Король", "Буби"));
        assertEquals(15, participant.getHandValue());
    }

    /**
     * Tests that hand can be reset.
     * Verifies the hand is empty after resetting.
     */
    @Test
    public void testResetHand() {
        Participant participant = new Participant();
        participant.getHand().addCard(new Card("Пятерка", "Черви"));
        assertEquals(5, participant.getHandValue());
        participant.resetHand();
        assertEquals(0, participant.getHandValue());
    }
}