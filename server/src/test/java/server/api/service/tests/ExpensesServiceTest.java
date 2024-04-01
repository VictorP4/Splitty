package server.api.service.tests;

import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.api.controllers.EventController;
import server.api.controllers.ExpensesController;
import server.api.repository.tests.TestEventRepository;
import server.api.repository.tests.TestExpenseRepository;
import server.api.services.EventService;
import server.api.services.ExpensesService;
import server.database.ParticipantRepository;
import server.database.TagRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ExpensesServiceTest {
    @Mock
    private TestEventRepository evRepo;
    @Mock
    private ParticipantRepository partRepo;
    @Mock
    private TagRepository tagRepo;
    @Mock
    private TestExpenseRepository repo;
    private EventService evServ;
    private EventController evCont;
    @InjectMocks
    private ExpensesService servMock;
    private ExpensesController cont;
    @BeforeEach
    public void setUp()  {
        MockitoAnnotations.openMocks(this);
        cont = new ExpensesController(servMock,null);
        repo = new TestExpenseRepository();
        evRepo = new TestEventRepository();
        servMock = new ExpensesService(repo,evRepo,partRepo);
        cont = new ExpensesController(servMock,null);
        evServ  = new EventService(evRepo,partRepo, repo,tagRepo);
        evCont = new EventController(evServ,null);
    }

    @Test
    void getAll() {
        Event event = new Event();
        event.setTitle("test");
        evRepo.save(event);
        evCont.add(event);
        Expense exp = new Expense();
        repo.save(exp);
        event.addExpense(exp);
        evCont.add(event);
        List<Expense> expenses = new ArrayList<>(repo.findAll());
        assertFalse(expenses.isEmpty());
    }

    @Test
    void addNew() {
        Event event = new Event();
        event.setId(1L);
        event.setTitle("test");
        evRepo.save(event);
        evCont.add(event);
       Expense exp = new Expense();
       assertNotNull(exp);
    }

    @Test
    void update() {
        Event event = new Event();
        event.setTitle("test");
        evRepo.save(event);
        Expense exp = new Expense();
        repo.save(exp);
        exp.setAmount(4);
        repo.save(exp);
        assertTrue(repo.getById(exp.getId()).getAmount() == 4);
    }

    @Test
    void delete() {
        Event event = new Event();
        event.setTitle("test");
        Event res = evRepo.save(event);
        Participant p1 = new Participant();
        List<Participant> ps = new ArrayList<>();
        p1.setId(1L);
        ps.add(p1);
        Expense exp = new Expense();
        exp.setInvolvedParticipants(ps);
        exp.setPaidBy(p1);
        repo.save(exp);
        Expense exp2 = servMock.delete(res.getId(), exp.getId());
        assertEquals(exp,exp2);
    }

    @Test
    void debt() {
        Event event = new Event();
        Expense exp = new Expense();
        Participant p1 = new Participant();
        p1.setName("dude");
        List<Participant> ps = new ArrayList<>();
        p1.setId(1L);
        ps.add(p1);
        exp.setInvolvedParticipants(ps);
        exp.setPaidBy(p1);
        exp.setAmount(5);
        event.setTitle("test");
        List<Expense> exps = new ArrayList<>();
        exps.add(exp);
        event.setExpenses(exps);
        Event res = evRepo.save(event);

        repo.save(exp);
        Map<String,List<Double>> map = servMock.debt(res.getId());
        List<Double> res1 = new ArrayList<>();
        res1.add(5.0);
        assertEquals(map.get("dude"),res1);
    }

    @Test
    void share() {
        Event event = new Event();
        Expense exp = new Expense();
        Participant p1 = new Participant();
        p1.setName("dude");
        List<Participant> ps = new ArrayList<>();
        p1.setId(1L);
        ps.add(p1);
        exp.setInvolvedParticipants(ps);
        exp.setPaidBy(p1);
        exp.setAmount(5);
        event.setTitle("test");
        List<Expense> exps = new ArrayList<>();
        exps.add(exp);
        event.setExpenses(exps);
        Event res = evRepo.save(event);

        repo.save(exp);
        Map<String,List<Double>> map = servMock.share(res.getId());
        List<Double> res1 = new ArrayList<>();
        res1.add(5.0);
        assertEquals(map.get("dude"),res1);
    }

    @Test
    void total() {
        Event event = new Event();
        event.setTitle("test");
        Expense exp = new Expense();
        exp.setAmount(5);
        repo.save(exp);
        List<Expense> exps = new ArrayList<>();
        exps.add(exp);
        event.setExpenses(exps);
        Event res = evRepo.save(event);
        assertEquals(servMock.total(res.getId()), 5);
    }

    @Test
    void getEvent() {
        Event event = new Event();
        event.setTitle("test");
        Event res = evRepo.save(event);
        Event res2 = servMock.getEvent(res.getId());
        assertEquals(res,res2);
    }
}
