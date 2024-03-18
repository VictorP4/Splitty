package server.api.controller.tests;


import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.api.controllers.EventController;
import server.api.repository.tests.TestEventRepository;
import server.api.services.EventService;

public class EventControllerTest {
    public TestEventRepository repo;
    private EventController sut;
    private EventService evServ;


    @BeforeEach
    public void setup(){
        evServ = new EventService(repo);
       repo = new TestEventRepository();
       sut = new EventController(repo, evServ);

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
