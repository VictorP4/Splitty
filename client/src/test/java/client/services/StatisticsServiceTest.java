package client.services;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Tag;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class StatisticsServiceTest {

    @Mock
    private ServerUtils serverUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTotalNumber() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense("Expense 1", 100.0, null, null, null, null, "USD"));
        expenses.add(new Expense("Expense 2", 50.0, null, null, null, null, "EUR"));
        Event event = new Event();
        for (Expense e: expenses) {
            event.getExpenses().add(e);
        }

        String expected = "150.00";
        String actual = StatisticsService.getTotalNumber(event);

        assertEquals(expected, actual);
    }

    @Test
    void testPopulateExpensesPerTag() {
        List<Expense> expenses = new ArrayList<>();
        Tag food = new Tag("Food", 255, 0,0);
        Tag drink = new Tag("Drink", 255, 0,0);
        expenses.add(new Expense("Expense 1", 100.0, null, null, null, food, "USD"));
        expenses.add(new Expense("Expense 2", 30.0, null, null, null, drink, "EUR"));
        expenses.add(new Expense("Expense 3", 100.0, null, null, null, food, "USD"));
        Event event1 = new Event();

        for (Expense e: expenses) {
            event1.getExpenses().add(e);
        }

        Map<String, Double> expected = Map.of("Food", 200.0, "Drink", 30.0);
        Map<String, Double> actual = StatisticsService.populateExpensesPerTag(event1);

        assertEquals(expected, actual);
    }

    @Test
    void testPopulateTagColors() {
        List<Expense> expenses = new ArrayList<>();
        expenses.add(new Expense("Expense 1", 100.0, null, null, null, new Tag("Food", 255, 0, 0), "USD"));
        expenses.add(new Expense("Expense 2", 50.0, null, null, null, new Tag("Travel", 0, 255, 0), "USD"));
        Event event1 = new Event();

        for (Expense e: expenses) {
            event1.getExpenses().add(e);
        }

        Map<String, Color> expected = Map.of("Food", Color.rgb(255, 0, 0), "Travel", Color.rgb(0, 255, 0));
        Map<String, Color> actual = StatisticsService.populateTagColors(event1);

        assertEquals(expected, actual);
    }
}
