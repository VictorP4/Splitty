package server.api.service.tests;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.api.services.CurrencyService;
import server.api.services.ExpensesService;
import server.database.EventRepository;
import server.database.ExpensesRepository;
import server.database.ParticipantRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class ExpensesServiceTest {
    @Mock
    private CurrencyService currencyService;
    @Mock
    private EventRepository evRepo;
    @Mock
    private ParticipantRepository partRepo;
    @Mock
    private ExpensesRepository repo;
    @InjectMocks
    private ExpensesService servMock;
    private static Expense exp;
    private static Event event;
    private static Participant p;
    @BeforeEach
    public void setUp()  {
        MockitoAnnotations.openMocks(this);
        p = new Participant("robin", "test");
        p.setId(3L);
        List<Participant> participants = new ArrayList<>();
        when(partRepo.save(p)).thenReturn(p);
        when(partRepo.getById(p.getId())).thenReturn(p);
        participants.add(p);

        // used expense for every test & repo methods
        exp = new Expense("please", 600.0, p, participants, new Date(), new Tag(), "EUR");
        exp.setId(1L);
        when(repo.existsById(1L)).thenReturn(true);
        when(repo.findById(exp.getId())).thenReturn(Optional.of(exp));
        when(repo.save(exp)).thenReturn(exp);

        // used event for every test & repo methods
        event = new Event();
        event.setId(1L);
        event.setTitle("test");
        when(evRepo.save(event)).thenReturn(event);
        when(evRepo.existsById(1L)).thenReturn(true);
        when(evRepo.getReferenceById(1L)).thenReturn(event);
        when(evRepo.findById(1L)).thenReturn(Optional.of(event));
    }

    @Test
    void getAll() {
        event.addExpense(exp);
        evRepo.save(event);

        assertEquals(servMock.getAll(1L).size(), 1);
        assertEquals(servMock.getAll(1L).getFirst(), exp);
    }

    @Test
    void addNewNull() {
        evRepo.save(event);

        Expense exp = servMock.addNew(event.getId(), new Expense());
        assertNull(exp);
    }

    @Test
    void addNew() {
        evRepo.save(event);
        Expense expRes = servMock.addNew(event.getId(), exp);
        assertEquals(exp, expRes);
    }

    @Test
    void update() {
        partRepo.save(p);
        List<Participant> participants = new ArrayList<>();
        participants.add(p);
        event.setParticipants(participants);

        repo.save(exp);
        evRepo.save(event);

        Expense updExp = new Expense("new", 666.0, p, participants, new Date(), null, "EUR");
        updExp.setId(exp.getId());

        Expense updated = servMock.update(event.getId(), exp.getId(), updExp);
        assertEquals(updExp, updated);
    }

    @Test
    void delete() {
        event.addExpense(exp);
        evRepo.save(event);
        Expense exp3 = servMock.delete(event.getId(), exp.getId());
        assertEquals(exp,exp3);
    }

    @Test
    void debt() {
        Participant p2 = new Participant("test", "test");
        p2.setId(2L);

        List<Participant> participants = new ArrayList<>();
        participants.add(p);
        participants.add(p2);
        event.setParticipants(participants);

        Expense exp2 = new Expense("new", 666.0, p2, participants, new Date(), null, "EUR");
        event.addExpense(exp);
        event.addExpense(exp2);

        Map<String, List<Double>> expected = new HashMap<>();
        List<Double> pAmounts = new ArrayList<>();
        pAmounts.add(600.0);
        expected.put(p.getName(), pAmounts);

        List<Double> p2Amounts = new ArrayList<>();
        p2Amounts.add(333.0);
        expected.put(p2.getName(), p2Amounts);

        repo.save(exp);
        evRepo.save(event);

        Map<String,List<Double>> map = servMock.debt(event.getId());
        assertEquals(expected, map);
    }

    @Test
    void share() {
        Participant p2 = new Participant("test", "test");
        p2.setId(2L);

        List<Participant> participants = new ArrayList<>();
        participants.add(p);
        participants.add(p2);
        event.setParticipants(participants);

        Expense exp2 = new Expense("new", 666.0, p2, participants, new Date(), null, "EUR");
        event.addExpense(exp);
        event.addExpense(exp2);

        Map<String, List<Double>> expected = new HashMap<>();
        List<Double> pAmounts = new ArrayList<>();
        pAmounts.add(600.0);
        pAmounts.add(333.0);
        expected.put(p.getName(), pAmounts);

        List<Double> p2Amounts = new ArrayList<>();
        p2Amounts.add(333.0);
        expected.put(p2.getName(), p2Amounts);

        repo.save(exp);
        evRepo.save(event);

        Map<String,List<Double>> map = servMock.share(event.getId());
        assertEquals(expected, map);
    }

    @Test
    void total() {
        Expense exp2 = new Expense("new", 666.0, null, null, new Date(), null, "EUR");
        List<Expense> exps = new ArrayList<>();
        exps.add(exp);
        exps.add(exp2);
        int sum = 0;
        for (Expense exp : exps) {
            sum += exp.getAmount();
        }

        event.addExpense(exp);
        event.addExpense(exp2);
        evRepo.save(event);

        assertEquals(sum, servMock.total(event.getId()));
    }

    @Test
    void getEvent() {
        evRepo.save(event);
        assertEquals(event, servMock.getEvent(event.getId()));
    }
}
