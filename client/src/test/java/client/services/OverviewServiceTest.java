package client.services;


import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.core.Response;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javafx.collections.FXCollections;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OverviewServiceTest {

    @InjectMocks
    private OverviewService overviewService;
    private ServerUtils serverUtils;

    @BeforeEach
    public void setUp() {
        serverUtils = mock(ServerUtils.class);
        overviewService = new OverviewService(serverUtils);
        MockitoAnnotations.openMocks(this);
    }

    /**
     * tests the formatted date method
     */
    @Test
    void formattedDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2024, Calendar.APRIL, 5);
        Date dummy = calendar.getTime();
        assertEquals("Apr 5, 2024", overviewService.formattedDate(dummy) );
    }

    /**
     * tests getAllExpenses from overviewService
     */
    @Test
    void getAllExpenses() {
        Event e = new Event();
        List<Participant> involved = new ArrayList<>();
        involved.add(new Participant("p2", "email"));
        Expense dummy = new Expense("title", 2.0, new Participant("p1", "email"), involved
                , new Date(2024, Calendar.APRIL, 5),
                null, "EUR" );
        Expense dummy2 = new Expense("debt repayment", 2.0, new Participant("p1", "email"), involved
                , new Date(2024, Calendar.APRIL, 5),
                null, "EUR" );
        e.addExpense(dummy);
        e.addExpense(dummy2);

        ObservableList<Expense> test = FXCollections.observableArrayList();
        test.add(dummy);

        assertEquals(test, overviewService.getAllExpenses(e));
    }

    /**
     * tests getAllExpenses from overviewService, case when event is null
     */
    @Test
    void getAllExpensesNull() {
        assertEquals(FXCollections.observableArrayList(), overviewService.getAllExpenses(null));
    }

    /**
     * tests getFromSelected from overviewService
     */
    @Test
    void getFromSelected() {
        Event e = new Event();
        Participant p1 = new Participant("p1", "");
        Participant p2 = new Participant("p2", "");
        Participant p3 = new Participant("p3", "");

        List<Participant> involved = new ArrayList<>();
        involved.add(p2);
        involved.add(p3);

        Expense e1 = new Expense("title", 2.0, p1, involved
                , new Date(2024, Calendar.APRIL, 5),
                null, "EUR" );

        Expense e2 = new Expense("title", 2.0, p2, involved
                , new Date(2024, Calendar.APRIL, 5),
                null, "EUR" );

        Expense e3 = new Expense("debt repayment", 2.0, p2, involved
                , new Date(2024, Calendar.APRIL, 5),
                null, "EUR" );

        e.addExpense(e1);
        e.addExpense(e2);
        e.addExpense(e3);

        ObservableList<Expense> test = FXCollections.observableArrayList();
        test.add(e2);

        assertEquals(test, overviewService.getFromSelected(e, p2));
    }

    /**
     * tests fetIncludingSelected, from overviewService
     */
    @Test
    void getIncludingSelected() {
        Event e = new Event();
        Participant p1 = new Participant("p1", "");
        Participant p2 = new Participant("p2", "");
        Participant p3 = new Participant("p3", "");

        List<Participant> involved = new ArrayList<>();
        involved.add(p2);
        involved.add(p3);

        Expense e1 = new Expense("title", 2.0, p1, involved
                , new Date(2024, Calendar.APRIL, 5),
                null, "EUR" );

        Expense e2 = new Expense("title", 2.0, p2, involved
                , new Date(2024, Calendar.APRIL, 5),
                null, "EUR" );

        Expense e3 = new Expense("debt repayment", 2.0, p2, involved
                , new Date(2024, Calendar.APRIL, 5),
                null, "EUR" );

        e.addExpense(e1);
        e.addExpense(e2);
        e.addExpense(e3);

        ObservableList<Expense> test = FXCollections.observableArrayList();
        test.add(e1);
        test.add(e2);

        assertEquals(test, overviewService.getIncludingSelected(e, p2));
    }

    /**
     * tests setParticipantString from overviewService
     */
    @Test
    void setParticipantsString() {
        Participant p2 = new Participant("p2", "");
        Participant p3 = new Participant("p3", "");

        List<Participant> involved = new ArrayList<>();
        involved.add(p2);
        involved.add(p3);

        assertEquals("p2, p3", overviewService.setParticipantsString(involved));
    }

    /**
     * tests covert currency from overviewService
     */
    @Test
    void convertCurrency() {
        Expense e1 = new Expense();
        e1.setId(1L);
        e1.setAmount(2.00);
        e1.setCurrency("EUR");
        e1.setDate(new Date());

        List<Expense> exp = new ArrayList<>();
        exp.add(e1);

        double usd = 2.13;

        when(serverUtils.convertCurrency(eq(2.00), eq("EUR"), eq("USD"), any(LocalDate.class))).thenReturn(usd);

        List<Expense> list = overviewService.convertCurrency(exp, "USD");

        assertEquals(list.getFirst().getAmount(), usd, 0.001);

    }

    @Test
    void deletePrevExp() {
        Map<Long, List<Expense>> m = new HashMap<>();
        Expense e = new Expense();
        e.setId(1L);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(e);
        m.put(e.getId(),expenses);
        overviewService.deletePrevExp(m, e);
        assertFalse(m.containsKey(1L));
    }

    @Test
    void addPrevExpNull() {
        Map<Long, List<Expense>> m = new HashMap<>();
        Expense e = new Expense();
        e.setId(1L);
        overviewService.addPrevExp(m, e);
        assertTrue(m.get(1L).contains(e));
    }

    @Test
    void addPrevExp() {
        Map<Long, List<Expense>> m = new HashMap<>();
        Expense e = new Expense();
        e.setId(1L);
        m.put(1L, new ArrayList<>());
        overviewService.addPrevExp(m, e);
        assertTrue(m.get(1L).contains(e));
    }

    @Test
    void getPrevExp() {
        Map<Long, List<Expense>> m = new HashMap<>();
        Expense e = new Expense();
        e.setId(1L);
        List<Expense> expenses = new ArrayList<>();
        expenses.add(e);
        m.put(e.getId(),expenses);

        assertEquals(e, overviewService.getPrevExp(m,1L));
    }

    @Test
    void getPrevExpNull(){
        Map<Long, List<Expense>> m = new HashMap<>();
        assertNull(overviewService.getPrevExp(m, 1L));
    }

    @Test
    void deleteExpense() {

    }

    @Test
    void removeParticipantFromEvent() {
    }

    /**
     * tests UpdateParticipant, case when being called with a new participant
     */
    @Test
    void updateParticipantWithNew() {

        Participant oldp = new Participant();
        Participant newp = new Participant();
        oldp.setId(1L);
        newp.setId(2L);

        Event e = mock(Event.class);

        List<Participant> originalPart = new ArrayList<>();
        originalPart.add(oldp);

        when(e.getParticipants()).thenReturn(originalPart);
        when(serverUtils.addParticipant(any(Participant.class))).thenReturn(newp);

        overviewService.updateParticipant(e, newp);

        verify(serverUtils).addParticipant(eq(newp));
        verify(serverUtils, never()).updateParticipant(any(Participant.class));

        assertTrue(e.getParticipants().contains(newp));
    }

    /**
     * tests updateParticipant, case when updating an existing participant
     */
    @Test
    void updateParticipant(){

        Participant p = new Participant("first", "");
        p.setId(1L);

        Event e = mock(Event.class);

        List<Participant> participants = new ArrayList<>();
        participants.add(p);

        when(e.getParticipants()).thenReturn(participants);
        when(serverUtils.updateParticipant(any(Participant.class))).thenReturn(p);

        overviewService.updateParticipant(e, p);

        verify(serverUtils).updateParticipant(eq(p));
        verify(serverUtils, never()).addParticipant(any(Participant.class));

        assertEquals(1, e.getParticipants().size());

    }
}