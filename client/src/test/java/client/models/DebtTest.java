package client.models;


import client.models.Debt;
import commons.Participant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



class DebtTest {

    @Test
    public void testConstructor() {
        Participant p1 = new Participant();
        p1.setName("p1");
        Participant p2 = new Participant();
        p2.setName("p2");
        Debt debt = new Debt(1,p1,p2);
        assertEquals(p1, debt.getPersonOwed());
        assertEquals(p2, debt.getPersonInDebt());
        assertEquals(1, debt.getAmount());

    }

    @Test
    void getAmount() {
        Participant p1 = new Participant();
        p1.setName("p1");
        Participant p2 = new Participant();
        p2.setName("p2");
        Debt debt = new Debt(1,p1,p2);
        debt.setAmount(11);
        assertEquals(11, debt.getAmount());
    }

    @Test
    void getPersonOwed() {
        Participant p1 = new Participant();
        p1.setName("p1");
        Participant p2 = new Participant();
        p2.setName("p2");
        Debt debt = new Debt(1,p1,p2);
        assertEquals(p1,debt.getPersonOwed());
    }

    @Test
    void getPersonInDebt() {
        Participant p1 = new Participant();
        p1.setName("p1");
        Participant p2 = new Participant();
        p2.setName("p2");
        Debt debt = new Debt(1,p1,p2);
        assertEquals(p2, debt.getPersonInDebt());
    }

    @Test
    void testEqualsSame() {
        Participant p1 = new Participant();
        p1.setName("p1");
        Participant p2 = new Participant();
        p2.setName("p2");
        Debt debt1 = new Debt(1,p1,p2);
        Debt debt2 = new Debt(1,p1,p2);
        assertEquals(debt1, debt2);
    }

    @Test
    void testNotEqualsAmount() {
        Participant p1 = new Participant();
        p1.setName("p1");
        Participant p2 = new Participant();
        p2.setName("p2");
        Debt debt1 = new Debt(1,p1,p2);
        Debt debt2 = new Debt(10,p1,p2);

        assertNotEquals(debt1, debt2);
    }

    @Test
    void testNotEqualsPersonOwed() {
        Participant p1 = new Participant();
        p1.setName("p1");
        Participant p2 = new Participant();
        p2.setName("p2");
        Participant p3 = new Participant();
        p3.setName("p3");
        Debt debt1 = new Debt(1,p1,p2);
        Debt debt2 = new Debt(1,p1,p2);
        debt2.setPersonOwed(p3);

        assertNotEquals(debt1, debt2);
    }

    @Test
    void testNotEqualsPersonInDebt() {
        Participant p1 = new Participant();
        p1.setName("p1");
        Participant p2 = new Participant();
        p2.setName("p2");
        Participant p3 = new Participant();
        p3.setName("p3");
        Debt debt1 = new Debt(1,p1,p2);
        Debt debt2 = new Debt(1,p1,p2);
        debt2.setPersonInDebt(p3);

        assertNotEquals(debt1, debt2);
    }

    @Test
    void testHashCodeEquals() {
        Participant p1 = new Participant();
        p1.setName("p1");
        Participant p2 = new Participant();
        p2.setName("p2");
        Debt debt1 = new Debt(1,p1,p2);
        Debt debt2 = new Debt(1,p1,p2);

        assertEquals(debt1, debt2);
        assertEquals(debt1.hashCode(), debt2.hashCode());
    }

    @Test
    void testHashCodeNotEquals() {
        Participant p1 = new Participant();
        p1.setName("p1");
        Participant p2 = new Participant();
        p2.setName("p2");
        Participant p3 = new Participant();
        p3.setName("p3");
        Debt debt1 = new Debt(1,p1,p2);
        Debt debt2 = new Debt(10,p3,p2);

        assertNotEquals(debt1, debt2);
        assertNotEquals(debt1.hashCode(), debt2.hashCode());
    }
}
