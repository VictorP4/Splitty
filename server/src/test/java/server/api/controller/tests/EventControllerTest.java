package server.api.controller.tests;


import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.controllers.EventController;
import server.api.repository.tests.TestEventRepository;

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
    private static Event getEvent(String q) {
        Event event = new Event();
        event.setTitle(q);
        return event;
    }
}
