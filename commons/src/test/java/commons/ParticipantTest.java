package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipantTest {

    /**
     * Tests the constructor of the Participant class.
     * It should construct a Participant with the specified name and email.
     */
    @Test
    public void testConstructor() {
        Participant participant = new Participant("John Doe", "john.doe@example.com");
        assertEquals("John Doe", participant.getName());
        assertEquals("john.doe@example.com", participant.getEmail());
    }

    /**
     * Tests the getId method of the Participant class.
     * It should return the ID of the participant.
     */
    @Test
    public void testGetId() {
        Participant participant = new Participant();
        participant.setId(1L);
        assertEquals(1L, participant.getId());
    }

    /**
     * Tests the getName method of the Participant class.
     * It should return the name of the participant.
     */
    @Test
    public void testGetName() {
        Participant participant = new Participant();
        participant.setName("John Doe");
        assertEquals("John Doe", participant.getName());
    }

    /**
     * Tests the getEmail method of the Participant class.
     * It should return the email of the participant.
     */
    @Test
    public void testGetEmail() {
        Participant participant = new Participant();
        participant.setEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", participant.getEmail());
    }

    /**
     * Tests the getBankAccount method of the Participant class.
     * It should return the bank account of the participant.
     */
    @Test
    public void testGetBankAccount() {
        Participant participant = new Participant();
        participant.setBankAccount("1234567890");
        assertEquals("1234567890", participant.getBankAccount());
    }

    /**
     * Tests the getEventFollowed method of the Participant class.
     * It should return the events followed by the participant.
     */
    @Test
    public void testGetEventFollowed() {
        Participant participant = new Participant();
        Event eventFollowed = new Event();
        participant.setEventFollowed(eventFollowed);
        assertEquals(eventFollowed, participant.getEventFollowed());
    }

    /**
     * Tests the equals method of the Participant class.
     * It should check if two participants are equal.
     */
    @Test
    public void testEquals() {
        Participant participant1 = new Participant();
        participant1.setId(1L);
        participant1.setName("John Doe");

        Participant participant2 = new Participant();
        participant2.setId(1L);
        participant2.setName("John Doe");

        assertEquals(participant1, participant2);
    }

    @Test
    public void testHashCodeEquals() {
        Participant participant1 = new Participant();
        participant1.setId(1L);
        participant1.setName("participat 1");
        Participant participant2 = new Participant();
        participant2.setId(1L);
        participant2.setName("participat 1");

        assertEquals(participant1.hashCode(), participant2.hashCode());
    }

    @Test
    public void testHashCodeNotEquals() {
        Participant participant1 = new Participant();
        participant1.setId(1L);
        participant1.setName("participat 1");
        Participant participant2 = new Participant();
        participant2.setId(1L);
        participant2.setName("Participant 2");

        assertNotEquals(participant1.hashCode(), participant2.hashCode());
    }

    /**
     * Tests the toString method of the Participant class.
     * It should return a string representation of the participant.
     */
    @Test
    public void testToString() {
        Participant participant = new Participant();
        participant.setId(1L);
        participant.setName("John Doe");
        participant.setEmail("john.doe@example.com");
        participant.setBankAccount("1234567890");

        String expected = "Participant{id=1, name='John Doe', email='john.doe@example.com', bankAccount='1234567890', debt=0.0, eventFollowed=null}";
        assertEquals(expected, participant.toString());
    }
}
