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
     * Tests the getIban method of the Participant class.
     * It should return the iban of the participant.
     */
    @Test
    public void getIban() {
        Participant p = new Participant();
        p.setIBAN("This is my IBAN");
        assertEquals("This is my IBAN", p.getIban());
    }

    /**
     * Tests the getBIC method of the Participant class.
     * It should return the BIC of the participant.
     */
    @Test
    public void getBic() {
        Participant p = new Participant();
        p.setBIC("This is my BIC");
        assertEquals("This is my BIC", p.getBic());
    }

    /**
     * Tests the setDebt and getDebt method of the Participant class.
     * It should set and return the debt of the participant.
     */
    @Test
    public void setDebt() {
        Participant p = new Participant();
        p.setDebt(66.6);
        assertEquals(66.6, p.getDebt());
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

    /**
     * Tests the hashcode method of the Participant class.
     * It should create a new unique hashcode for different participants.
     */
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

    /**
     * Tests the hashcode method of the Participant class.
     * It should create a new unique hashcode for different participants.
     */
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
        participant.setIBAN("1234567890");
        participant.setBIC("123456");

        String expected = "John Doe";
        assertEquals(expected, participant.toString());
    }
}
