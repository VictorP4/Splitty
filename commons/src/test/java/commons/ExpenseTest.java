package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpenseTest {

    /**
     * Tests the constructor of the Expense class.
     * It should construct an Expense with the specified title, amount, payer, involved participants, and date.
     */
    @Test
    public void testConstructor() {
        Participant paidBy = new Participant("John Doe", "john.doe@example.com");
        List<Participant> involvedParticipants = new ArrayList<>();
        involvedParticipants.add(new Participant("Jane Doe", "jane.doe@example.com"));
        Date date = new Date();

        Expense expense = new Expense("Groceries", 50.0, paidBy, involvedParticipants, date);
        assertEquals("Groceries", expense.getTitle());
        assertEquals(50.0, expense.getAmount());
        assertEquals(paidBy, expense.getPaidBy());
        assertEquals(involvedParticipants, expense.getInvolvedParticipants());
        assertEquals(date, expense.getDate());
    }

    /**
     * Tests the getId method of the Expense class.
     * It should return the ID of the expense.
     */
    @Test
    public void testGetId() {
        Expense expense = new Expense();
        expense.setId(1L);
        assertEquals(1L, expense.getId());
    }

    /**
     * Tests the getTitle method of the Expense class.
     * It should return the title of the expense.
     */
    @Test
    public void testGetTitle() {
        Expense expense = new Expense();
        expense.setTitle("Groceries");
        assertEquals("Groceries", expense.getTitle());
    }

    /**
     * Tests the getAmount method of the Expense class.
     * It should return the amount of the expense.
     */
    @Test
    public void testGetAmount() {
        Expense expense = new Expense();
        expense.setAmount(50.0);
        assertEquals(50.0, expense.getAmount());
    }

    /**
     * Tests the getPaidBy method of the Expense class.
     * It should return the participant who paid for the expense.
     */
    @Test
    public void testGetPaidBy() {
        Participant paidBy = new Participant("John Doe", "john.doe@example.com");
        Expense expense = new Expense();
        expense.setPaidBy(paidBy);
        assertEquals(paidBy, expense.getPaidBy());
    }

    /**
     * Tests the getInvolvedParticipants method of the Expense class.
     * It should return the list of participants involved in the expense.
     */
    @Test
    public void testGetInvolvedParticipants() {
        List<Participant> involvedParticipants = new ArrayList<>();
        involvedParticipants.add(new Participant("Jane Doe", "jane.doe@example.com"));
        Expense expense = new Expense();
        expense.setInvolvedParticipants(involvedParticipants);
        assertEquals(involvedParticipants, expense.getInvolvedParticipants());
    }

    /**
     * Tests the getDate method of the Expense class.
     * It should return the date of the expense.
     */
    @Test
    public void testGetDate() {
        Date date = new Date();
        Expense expense = new Expense();
        expense.setDate(date);
        assertEquals(date, expense.getDate());
    }

    /**
     * Tests the equals method of the Expense class.
     * It should check if two expenses are equal.
     */
    @Test
    public void testEquals() {
        Participant paidBy = new Participant("John Doe", "john.doe@example.com");
        List<Participant> involvedParticipants = new ArrayList<>();
        involvedParticipants.add(new Participant("Jane Doe", "jane.doe@example.com"));

        Expense expense1 = new Expense("Groceries", 50.0, paidBy, involvedParticipants, new Date());
        expense1.setId(1L);
        Expense expense2 = new Expense("Groceries", 50.0, paidBy, involvedParticipants, new Date());
        expense2.setId(1L);

        assertEquals(expense1, expense2);
    }

//    /**
//     * Tests the hashCode method of the Expense class.
//     * It should generate a hash code for the expense.
//     */
//    @Test
//    public void testHashCode() {
//        Participant paidBy = new Participant("John Doe", "john.doe@example.com");
//        List<Participant> involvedParticipants = new ArrayList<>();
//        involvedParticipants.add(new Participant("Jane Doe", "jane.doe@example.com"));
//
//        Expense expense = new Expense("Groceries", 50.0, paidBy, involvedParticipants, new Date());
//        assertEquals(Objects.hash("Groceries", 50.0, paidBy, involvedParticipants, expense.getDate()), expense.hashCode());
//    }
}
