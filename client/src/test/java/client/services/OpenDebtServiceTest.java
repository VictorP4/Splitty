package client.services;

import client.models.Debt;
import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenDebtServiceTest {
    private OpenDebtService openDebtService;
    @BeforeEach
    public void setup(){
        openDebtService = new OpenDebtService();
    }
    @Test
    public void getDebts(){
        Event e = new Event();
        Participant p1 = new Participant();
        p1.setName("p1");
        p1.setDebt(20);
        Participant p2 = new Participant();
        p2.setName("p2");
        p2.setDebt(-20);
        e.setParticipants(new ArrayList<>(List.of(p1,p2)));
        List<Debt> result = openDebtService.getDebts(e);
        List<Debt> expected = List.of(new Debt(20,p1,p2));
        assertEquals(expected,result);
    }
}
