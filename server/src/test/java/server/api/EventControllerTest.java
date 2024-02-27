package server.api;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import commons.Event;
import server.api.controllers.EventController;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventControllerTest {
    public TestEventRepository repo;
    private EventController sut;
    @BeforeEach
    public void setup(){
       repo = new TestEventRepository();
       sut = new EventController(repo);

    }
    @Test
    public void cannotAddNullEvent() {
        var actual = sut.add(getEvent(null));
    }
    @Test
    public void getAllTest() {
        Event event1 = getEvent("e1");
        Event event2 = getEvent("e2");
        Event event3 = getEvent("e3");
        sut.add(event1);
        sut.add(event2);
        sut.add(event3);
        List<Event> events = sut.getAll();
        List<Event> expected  = new ArrayList<>();
        expected.add(event1);
        expected.add(event2);
        expected.add(event3);
        assertEquals(expected, events);

    }

    private static Event getEvent(String q) {
        Event event = new Event();
        event.setTitle(q);
        return event;
    }
}
