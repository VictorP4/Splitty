package server.api.controller.tests;

import commons.Expense;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.api.controllers.ExpensesController;
import server.api.services.ExpensesService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class ExpensesControllerTest {

    @Mock
    private ExpensesService expensesService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private ExpensesController expensesController;

    public ExpensesControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll() {
        long eventId = 1L;
        List<Expense> expectedExpenses = Collections.singletonList(new Expense());
        when(expensesService.getAll(eventId)).thenReturn(expectedExpenses);

        ResponseEntity<List<Expense>> response = expensesController.getAll(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedExpenses, response.getBody());
    }

    @Test
    void addNew() {
        long eventId = 1L;
        Expense expense = new Expense();
        when(expensesService.addNew(eq(eventId), any(Expense.class))).thenReturn(expense);

        ResponseEntity<Expense> response = expensesController.addNew(eventId, expense);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expense, response.getBody());
    }

    @Test
    void update() {
        long eventId = 1L;
        long expenseId = 1L;
        Expense updatedExpense = new Expense();
        when(expensesService.update(eq(eventId), eq(expenseId), any(Expense.class))).thenReturn(updatedExpense);

        ResponseEntity<Expense> response = expensesController.update(eventId, expenseId, new Expense());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedExpense, response.getBody());
    }

    @Test
    void delete() {
        long eventId = 1L;
        long expenseId = 1L;
        Expense deletedExpense = new Expense();
        when(expensesService.delete(eventId, expenseId)).thenReturn(deletedExpense);

        ResponseEntity<Expense> response = expensesController.delete(eventId, expenseId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(deletedExpense, response.getBody());
    }

    @Test
    void total() {
        long eventId = 1L;
        double totalExpense = 100.0;
        when(expensesService.total(eventId)).thenReturn(totalExpense);

        ResponseEntity<Double> response = expensesController.total(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(totalExpense, response.getBody());
    }

    @Test
    void debt() {
        long eventId = 1L;
        Map<String, List<Double>> debts = Collections.singletonMap("Ax", Collections.singletonList(50.0));
        when(expensesService.debt(eventId)).thenReturn(debts);

        ResponseEntity<Map<String, List<Double>>> response = expensesController.debt(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(debts, response.getBody());
    }

    @Test
    void share() {
        long eventId = 1L;
        Map<String, List<Double>> shares = Collections.singletonMap("Ax", Collections.singletonList(50.0));
        when(expensesService.share(eventId)).thenReturn(shares);

        ResponseEntity<Map<String, List<Double>>> response = expensesController.share(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(shares, response.getBody());
    }
}
