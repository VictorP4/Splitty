package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {

    /**
     * Tests the default constructor of the Event class.
     * It should initialize the participants and expenses lists as empty ArrayLists,
     * and set the creationDate and lastActivityDate to the current date and time.
     */
    @Test
    public void testDefaultConstructor() {
        Event event = new Event();
        assertNotNull(event.getParticipants());
        assertNotNull(event.getExpenses());
        assertEquals(0, event.getParticipants().size());
        assertEquals(0, event.getExpenses().size());
        assertNotNull(event.getCreationDate());
        assertNotNull(event.getLastActivityDate());
    }

    /**
     * Tests the getId method of the Event class.
     * It should return the ID of the event.
     */
    @Test
    public void testGetId() {
        Event event = new Event();
        event.setId(1L);
        assertEquals(1L, event.getId());
    }

    /**
     * Tests the getTitle method of the Event class.
     * It should return the title of the event.
     */
    @Test
    public void testGetTitle() {
        Event event = new Event();
        event.setTitle("Birthday Party");
        assertEquals("Birthday Party", event.getTitle());
    }

    /**
     * Tests the getCreationDate method of the Event class.
     * It should return the creation date of the event.
     */
    @Test
    public void testGetCreationDate() {
        Date now = new Date();
        Event event = new Event();
        assertEquals(now, event.getCreationDate());
    }

    /**
     * Tests the getLastActivityDate method of the Event class.
     * It should return the last activity date of the event.
     */
    @Test
    public void testGetLastActivityDate() {
        Date now = new Date();
        Event event = new Event();
        assertEquals(now, event.getLastActivityDate());
    }

    /**
     * Tests the getInviteCode method of the Event class.
     * It should return the invite code of the event.
     */
    @Test
    public void testGetInviteCode() {
        Event event = new Event();
        event.setInviteCode("ABCD1234");
        assertEquals("ABCD1234", event.getInviteCode());
    }

    /**
     * Tests the getParticipants method of the Event class.
     * It should return the list of participants of the event.
     */
    @Test
    public void testGetParticipants() {
        Event event = new Event();
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant("John", "john@example.com"));
        event.setParticipants(participants);
        assertEquals(participants, event.getParticipants());
    }

    /**
     * Tests the getExpenses method of the Event class.
     * It should return the list of expenses of the event.
     */
    @Test
    public void testGetExpenses() {
        Event event = new Event();
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense("Food", 50.0, new Participant("John", "john@example.com"), new ArrayList<>(),
                new Date(), new Tag(), "EUR"));
        event.setExpenses(expenses);
        assertEquals(expenses, event.getExpenses());
    }

    /**
     * Tests the addParticipant method of the Event class.
     * It should add a participant to the list of participants of the event.
     */
    @Test
    public void testAddParticipant() {
        Event event = new Event();
        Participant participant = new Participant("John", "john@example.com");
        event.addParticipant(participant);
        assertTrue(event.getParticipants().contains(participant));
    }

    /**
     * Tests the removeParticipant method of the Event class.
     * It should remove a participant from the list of participants of the event.
     */
    @Test
    public void testRemoveParticipant() {
        Event event = new Event();
        Participant participant = new Participant("John", "john@example.com");
        event.addParticipant(participant);
        event.removeParticipant(participant);
        assertFalse(event.getParticipants().contains(participant));
    }

    /**
     * Tests the addExpense method of the Event class.
     * It should add an expense to the list of expenses of the event.
     */
    @Test
    public void testAddExpense() {
        Event event = new Event();
        Expense expense = new Expense("Food", 50.0, new Participant("John", "john@example.com"), new ArrayList<>(),
                new Date(), new Tag(), "EUR");
        event.addExpense(expense);
        assertTrue(event.getExpenses().contains(expense));
    }

    /**
     * Tests the removeExpense method of the Event class.
     * It should remove an expense from the list of expenses of the event.
     */
    @Test
    public void testRemoveExpense() {
        Event event = new Event();
        Expense expense = new Expense("Food", 50.0, new Participant("John", "john@example.com"), new ArrayList<>(),
                new Date(), new Tag(), "EUR");
        event.addExpense(expense);
        event.removeExpense(expense);
        assertFalse(event.getExpenses().contains(expense));
    }

    /**
     * Tests the equals method of the Event class.
     * It should check if two events are equal.
     */
    @Test
    public void testDiffEquals() {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");

        Event event2 = new Event();
        event2.setId(1L);
        event2.setTitle("Event 1");
        assertNotEquals(event1, event2);
    }

    /**
     * Tests the hashCode method of the Event class.
     * It should generate a hash code for the event.
     */
    @Test
    public void testDiffHashCodeEquals() {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");
        Event event2 = new Event();
        event2.setId(1L);
        event2.setTitle("Event 1");

        assertNotEquals(event1, event2);
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    public void testGetSetTags() {
        Event event = new Event();
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("a", 0, 255, 0));
        tags.add(new Tag("b", 0, 0, 255));
        event.setTags(tags);
        assertEquals(tags, event.getTags());
    }

    @Test
    public void testHashCodeNotEquals() {
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle("Event 1");
        Event event2 = new Event();
        event2.setId(1L);
        event2.setTitle("Event 2");

        assertNotEquals(event1, event2);
        assertNotEquals(event1.hashCode(), event2.hashCode());
    }
}
