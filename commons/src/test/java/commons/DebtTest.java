package commons;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DebtTest {

    @Test
    public void testDefaultConstructor() {
        Debt debt = new Debt();
        assertNull(debt.getId());
        assertEquals(0, debt.getAmount());
        assertNull(debt.getPersonOwed());
        assertNull(debt.getPersonInDebt());
        assertFalse(debt.isSettled());
        assertNull(debt.getDebtFor());
    }

    @Test
    void getId() {
        Debt debt = new Debt();
        debt.setId(1L);
        assertEquals(1L, debt.getId());
    }

    @Test
    void getAmount() {
        Debt debt = new Debt();
        debt.setAmount(11);
        assertEquals(11, debt.getAmount());
    }

    @Test
    void getPersonOwed() {
        Debt debt = new Debt();
        Participant owedTo = new Participant();
        debt.setPersonInDebt(owedTo);
        assertEquals(owedTo, debt.getPersonInDebt());
    }

    @Test
    void getPersonInDebt() {
        Debt debt = new Debt();
        Participant inDebt = new Participant();
        debt.setPersonInDebt(inDebt);
        assertEquals(inDebt, debt.getPersonInDebt());
    }

    @Test
    void isPaidOff() {
        Debt debt = new Debt();
        debt.setSettled(true);
        assertTrue(debt.isSettled());
    }

    @Test
    void getDebtFor() {
        Event expense = new Event();
        Debt debt = new Debt();
        debt.setDebtFor(expense);
        assertEquals(expense, debt.getDebtFor());
    }

    @Test
    void testEqualsSame() {
        Event event = new Event();
        Debt debt1 = new Debt(15.00, new Participant(), new Participant(),
                event);
        Debt debt2 = new Debt(15.00, new Participant(), new Participant(),
                event);
        assertEquals(debt1, debt2);
    }

    @Test
    void testNotEqualsId() {
        Event event = new Event();
        Debt debt1 = new Debt(15.00, new Participant(), new Participant(),
                event);
        Debt debt2 = new Debt(15.00, new Participant(), new Participant(),
                event);

        debt1.setId(1L);
        debt1.setId(2L);

        assertNotEquals(debt1, debt2);
    }

    @Test
    void testNotEqualsAmount() {
        Event event = new Event();
        Debt debt1 = new Debt(1.00, new Participant(), new Participant(),
                event);
        Debt debt2 = new Debt(15.00, new Participant(), new Participant(),
                event);

        debt1.setId(1L);
        debt1.setId(2L);

        assertNotEquals(debt1, debt2);
    }

    @Test
    void testNotEqualsPersonOwed() {
        Participant p = new Participant();
        p.setName("Not Sane");
        Event event = new Event();
        Debt debt1 = new Debt(15.00, p, new Participant(),
                event);
        Debt debt2 = new Debt(15.00, new Participant(), new Participant(),
                event);

        debt1.setId(1L);
        debt1.setId(2L);

        assertNotEquals(debt1, debt2);
    }

    @Test
    void testNotEqualsPersonInDebt() {
        Participant p = new Participant();
        p.setName("Not Sane");
        Event event = new Event();
        Debt debt1 = new Debt(15.00, new Participant(), p,
                event);
        Debt debt2 = new Debt(15.00, new Participant(), new Participant(),
                event);

        debt1.setId(1L);
        debt1.setId(2L);

        assertNotEquals(debt1, debt2);
    }

    @Test
    void testNotEqualsEvent() {
        Event event1 = new Event();
        event1.setTitle("Not sane");
        Event event2 = new Event();
        event2.setTitle("Not sane");
        Debt debt1 = new Debt(15.00, new Participant(), new Participant(),
                event1);
        Debt debt2 = new Debt(15.00, new Participant(), new Participant(),
                event2);

        assertNotEquals(debt1, debt2);
    }

    @Test
    void testHashCodeEquals() {
        Event event = new Event();
        Debt debt1 = new Debt(15.00, new Participant(), new Participant(),
                event);
        Debt debt2 = new Debt(15.00, new Participant(), new Participant(),
                event);

        assertEquals(debt1, debt2);
        assertEquals(debt1.hashCode(), debt2.hashCode());
    }

    @Test
    void testHashCodeNotEquals() {
        Event event = new Event();
        Debt debt1 = new Debt(1.00, new Participant(), new Participant(),
                event);
        Debt debt2 = new Debt(15.00, new Participant(), new Participant(),
                event);

        assertNotEquals(debt1, debt2);
        assertNotEquals(debt1.hashCode(), debt2.hashCode());
    }
}