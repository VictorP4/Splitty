package client.services;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AddExpenseServiceTests {

    @InjectMocks
    private AddExpenseService expenseService;

    @BeforeEach
    public void setUp() {
        expenseService = new AddExpenseService();
        MockitoAnnotations.openMocks(this);
    }

    /**
     * tests the updateExp method in AddExpenseService
     */
    @Test
    public void testUpdateExpense() {
        Event event = new Event();
        Expense dummy = new Expense();
        dummy.setId(3L);

        Expense old = new Expense();
        old.setId(1L);
        old.setAmount(50.0);

        Expense updatedExpense = new Expense();
        updatedExpense.setId(1L);
        updatedExpense.setAmount(20.0);

        event.getExpenses().add(dummy);
        event.getExpenses().add(old);

        assertEquals(updatedExpense, expenseService.updateExp(event, updatedExpense));
        assertEquals(dummy, event.getExpenses().getFirst());
        assertEquals(updatedExpense, event.getExpenses().get(1));
    }

    /**
     * tests the updateExp method in AddExpenseService. This time the updated expense was not in the event.
     * This methods should just return it and not edit anything.
     */
    @Test
    public void testUpdateExpenseNotInEvent() {
        Event event = new Event();
        Expense dummy = new Expense();
        dummy.setId(3L);

        Expense old = new Expense();
        old.setId(2L);
        old.setAmount(50.0);

        Expense updatedExpense = new Expense();
        updatedExpense.setId(1L);
        updatedExpense.setAmount(20.0);

        event.getExpenses().add(dummy);
        event.getExpenses().add(old);

        assertEquals(updatedExpense, expenseService.updateExp(event, updatedExpense));
        for (Expense expense : event.getExpenses()) {
            assertNotEquals(updatedExpense, expense);
        }
    }

    /**
     * tests the getAllExpenses methods in AddExpenseService
     */
    @Test
    public void testGetAllExpenses() {
        Event event = new Event();
        Expense expense1 = new Expense();
        expense1.setId(1L);
        Expense expense2 = new Expense();
        expense2.setId(2L);
        Expense expense3 = new Expense();
        expense3.setId(3L);
        Expense expense4 = new Expense();
        expense4.setId(4L);

        event.getExpenses().add(expense1);
        event.getExpenses().add(expense2);
        event.getExpenses().add(expense3);
        event.getExpenses().add(expense4);

        for (int i = 0; i < event.getExpenses().size(); i++) {
            assertEquals(event.getExpenses().get(i).getId(), expenseService.getAllExpenses(event).get(i));
        }
    }

    /**
     * tests the checkParticipants method in AddExpenseService
     */
    @Test
    public void testCheckParticipants() {
        Event event = new Event();
        Expense prev = new Expense();
        prev.setId(1L);
        Participant p1 = new Participant("Robin", "random@gmail.com");
        p1.setId(1L);
        Participant p2 = new Participant("Lara", "random@hotmail.com");
        p2.setId(2L);
        Participant p3 = new Participant("Stoyan", "random2@gmail.com");
        p3.setId(3L);
        Participant p4 = new Participant("Egemen", "random3@gmail.com");
        p4.setId(4L);
        Participant p5 = new Participant("Bogdan", "random4@gmail.com");
        p5.setId(5L);
        Participant p6 = new Participant("Victor", "random5@gmail.com");
        p6.setId(6L);

        event.getParticipants().add(p1);
        event.getParticipants().add(p2);
        event.getParticipants().add(p3);
        event.getParticipants().add(p4);
        event.getParticipants().add(p5);
        event.getParticipants().add(p6);
        List<Participant> ps = new ArrayList<Participant>();
        ps.addAll(event.getParticipants());
        prev.setInvolvedParticipants(ps);
        prev.setPaidBy(p5);
        prev.setTag(new Tag());

        assertTrue(expenseService.checkExpenseParticipants(event, prev));
    }

    /**
     * tests the checkParticipants method in AddExpenseService
     */
    @Test
    public void False() {
        Event event = new Event();
        Expense prev = new Expense();
        prev.setId(1L);
        Participant p1 = new Participant("Robin", "random@gmail.com");
        p1.setId(1L);
        Participant p2 = new Participant("Lara", "random@hotmail.com");
        p2.setId(2L);
        Participant p3 = new Participant("Stoyan", "random2@gmail.com");
        p3.setId(3L);
        Participant p4 = new Participant("Egemen", "random3@gmail.com");
        p4.setId(4L);

        event.getParticipants().add(p1);
        event.getParticipants().add(p2);
        event.getParticipants().add(p3);

        List<Participant> ps = new ArrayList<Participant>();
        ps.addAll(event.getParticipants());
        ps.add(p4);

        prev.setInvolvedParticipants(ps);
        prev.setPaidBy(p2);
        prev.setTag(new Tag());

        assertFalse(expenseService.checkExpenseParticipants(event, prev));
    }
}
