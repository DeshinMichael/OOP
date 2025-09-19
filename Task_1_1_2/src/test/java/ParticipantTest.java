import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ParticipantTest {

    @Test
    public void testCreationParticipant() {
        Participant participant = new Participant();
        assertNotNull(participant);
        assertNotNull(participant.getHand());
    }

    @Test
    public void testGetHand() {
        Participant participant = new Participant();
        assertNotNull(participant.getHand());
    }

    @Test
    public void testInitialScoreIsZero() {
        Participant participant = new Participant();
        assertEquals(0, participant.getScore());
    }

    @Test
    public void testIncrementScore() {
        Participant participant = new Participant();
        participant.incScore();
        assertEquals(1, participant.getScore());
        participant.incScore();
        assertEquals(2, participant.getScore());
    }

    @Test
    public void testGetHandValue() {
        Participant participant = new Participant();
        assertEquals(0, participant.getHandValue());
        participant.getHand().addCard(new Card("Пятерка", "Черви"));
        assertEquals(5, participant.getHandValue());
        participant.getHand().addCard(new Card("Король", "Буби"));
        assertEquals(15, participant.getHandValue());
    }

    @Test
    public void testResetHand() {
        Participant participant = new Participant();
        participant.getHand().addCard(new Card("Пятерка", "Черви"));
        assertEquals(5, participant.getHandValue());
        participant.resetHand();
        assertEquals(0, participant.getHandValue());
    }
}