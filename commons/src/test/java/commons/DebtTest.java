package commons;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class DebtTest {

    @Test
    public void testDefaultConstructor() {
        Debt debt = new Debt();
        assertNull(debt.getId());
        assertEquals(0, debt.getAmount());
        assertNull(debt.getPersonOwed());
        assertNull(debt.getPersonInDebt());
        assertFalse(debt.isSettled());
        assertNull(debt.getDateCreation());
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
        List<Participant> owedTo = new ArrayList<>();
        owedTo.add(new Participant());
        debt.setPersonInDebt(owedTo);
        assertEquals(owedTo, debt.getPersonInDebt());
    }

    @Test
    void getPersonInDebt() {
        Debt debt = new Debt();
        List<Participant> inDebt = new ArrayList<>();
        inDebt.add(new Participant());
        inDebt.add(new Participant());
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
    void getDateCreation() {
        Date created = new Date(1568, Calendar.DECEMBER,12);
        Debt debt = new Debt();
        debt.setDateCreation(created);
        assertEquals(created, debt.getDateCreation());
    }

    @Test
    void getDebtFor() {
        Expense expense = new Expense();
        Debt debt = new Debt();
        debt.setDebtFor(expense);
        assertEquals(expense, debt.getDebtFor());
    }

    @Test
    void testEqualsSame() {
        Date created = new Date(1568, Calendar.DECEMBER,12);
        Expense expense = new Expense();
        Debt debt1 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, expense);
        Debt debt2 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, expense);
        assertEquals(debt1, debt2);
    }

    @Test
    void testNotEqualsId() {
        Date created = new Date(1568, Calendar.DECEMBER,12);
        Expense expense = new Expense();

        Debt debt1 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, expense);
        debt1.setId(1L);
        Debt debt2 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, expense);
        debt1.setId(2L);

        assertNotEquals(debt1, debt2);
    }

    @Test
    void testNotEqualsAmount() {
        Date created = new Date(1568, Calendar.DECEMBER,12);
        Expense expense = new Expense();

        Debt debt1 = new Debt(20, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, expense);
        debt1.setId(1L);
        Debt debt2 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, expense);
        debt1.setId(1L);

        assertNotEquals(debt1, debt2);
    }

    @Test
    void testNotEqualsPersonOwed() {
        Date created = new Date(1568, Calendar.DECEMBER,12);
        Expense expense = new Expense();
        List<Participant> owed = new ArrayList<>();
        owed.add(new Participant());

        Debt debt1 = new Debt(15, owed, new ArrayList<Participant>(),
                true, created, expense);
        debt1.setId(1L);
        Debt debt2 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, expense);
        debt1.setId(1L);

        assertNotEquals(debt1, debt2);
    }

    @Test
    void testNotEqualsPersonInDebt() {
        Date created = new Date(1568, Calendar.DECEMBER,12);
        Expense expense = new Expense();
        List<Participant> inDebt = new ArrayList<>();
        inDebt.add(new Participant());

        Debt debt1 = new Debt(15, new ArrayList<Participant>(), inDebt,
                true, created, expense);
        debt1.setId(1L);
        Debt debt2 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, expense);
        debt1.setId(1L);

        assertNotEquals(debt1, debt2);
    }

    @Test
    void testNotEqualsPaidOff() {
        Date created = new Date(1568, Calendar.DECEMBER,12);
        Expense expense = new Expense();

        Debt debt1 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                false, created, expense);
        debt1.setId(1L);
        Debt debt2 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, expense);
        debt1.setId(1L);

        assertNotEquals(debt1, debt2);
    }

    @Test
    void testNotEqualsDate() {
        Date created = new Date(1568, Calendar.DECEMBER,12);
        Expense expense = new Expense();

        Debt debt1 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, new Date(), expense);
        debt1.setId(1L);
        Debt debt2 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, expense);
        debt1.setId(1L);

        assertNotEquals(debt1, debt2);
    }

    @Test
    void testNotEqualsExpense() {
        Date created = new Date(1568, Calendar.DECEMBER,12);
        Expense expense = new Expense();
        expense.setAmount(1200);

        Debt debt1 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, new Expense());
        debt1.setId(1L);
        Debt debt2 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, expense);
        debt1.setId(1L);

        assertNotEquals(debt1, debt2);
    }

    @Test
    void testHashCodeEquals() {
        Date created = new Date(1568, Calendar.DECEMBER,12);
        Expense expense = new Expense();
        Debt debt1 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, expense);
        Debt debt2 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, expense);

        assertEquals(debt1, debt2);
        assertEquals(debt1.hashCode(), debt2.hashCode());
    }

    @Test
    void testHashCodeNotEquals() {
        Date created = new Date(1568, Calendar.DECEMBER,12);
        Expense expense = new Expense();
        Debt debt1 = new Debt(20, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, expense);
        Debt debt2 = new Debt(15, new ArrayList<Participant>(), new ArrayList<Participant>(),
                true, created, expense);

        assertNotEquals(debt1, debt2);
        assertNotEquals(debt1.hashCode(), debt2.hashCode());
    }
}