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
import server.database.EventRepository;
import server.database.ExpensesRepository;
import server.database.ParticipantRepository;
import server.database.TagRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

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
        evCont = new EventController(evRepo,evServ,null);
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
       when(repo.save(exp)).thenReturn(exp);
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
    }

    @Test
    void debt() {
    }

    @Test
    void share() {
    }

    @Test
    void total() {
    }

    @Test
    void getEvent() {
    }
}
