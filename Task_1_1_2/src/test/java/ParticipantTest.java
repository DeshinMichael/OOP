import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// Test class for Participant functionality
public class ParticipantTest {

    // Tests that a Participant object can be created properly
    @Test
    public void testCreationParticipant() {
        Participant participant = new Participant();
        assertNotNull(participant);
        assertNotNull(participant.getHand());
    }

    // Tests that hand can be accessed
    @Test
    public void testGetHand() {
        Participant participant = new Participant();
        assertNotNull(participant.getHand());
    }

    // Tests that initial score is zero
    @Test
    public void testInitialScoreIsZero() {
        Participant participant = new Participant();
        assertEquals(0, participant.getScore());
    }

    // Tests that score can be incremented
    @Test
    public void testIncrementScore() {
        Participant participant = new Participant();
        participant.incScore();
        assertEquals(1, participant.getScore());
        participant.incScore();
        assertEquals(2, participant.getScore());
    }

    // Tests that hand value is calculated correctly
    @Test
    public void testGetHandValue() {
        Participant participant = new Participant();
        assertEquals(0, participant.getHandValue());
        participant.getHand().addCard(new Card("Пятерка", "Черви"));
        assertEquals(5, participant.getHandValue());
        participant.getHand().addCard(new Card("Король", "Буби"));
        assertEquals(15, participant.getHandValue());
    }

    // Tests that hand can be reset
    @Test
    public void testResetHand() {
        Participant participant = new Participant();
        participant.getHand().addCard(new Card("Пятерка", "Черви"));
        assertEquals(5, participant.getHandValue());
        participant.resetHand();
        assertEquals(0, participant.getHandValue());
    }
}