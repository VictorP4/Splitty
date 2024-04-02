package server.api.service.tests;

import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.api.services.EventService;
import server.database.EventRepository;
import server.database.ExpensesRepository;
import server.database.ParticipantRepository;
import server.database.TagRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EventServiceTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private ExpensesRepository expensesRepository;
    @InjectMocks
    private EventService eventService;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void getByIdTest(){
        Event event = new Event();
        event.setId(1L);
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        Event e = eventService.getById(1l);

        assertEquals(event,e);
        verify(eventRepository,times(1 )).existsById(1L);
    }
    @Test
    public void addTest(){
        Event event = new Event();
        event.setTitle("Testing");
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle(event.getTitle());
        event1.setCreationDate(event.getCreationDate());
        event1.setLastActivityDate(event.getLastActivityDate());
        event1.setInviteCode(event.getInviteCode());

        when(eventRepository.save(event)).thenReturn(event1);
        Event e = eventService.add(event);

        assertEquals(event1,e);
        verify(eventRepository,times(1 )).save(event);

    }
    @Test
    public void addTestNullTitle(){
        Event event = new Event();
        Event event1 = new Event();
        event1.setId(1L);
        event1.setTitle(event.getTitle());
        event1.setCreationDate(event.getCreationDate());
        event1.setLastActivityDate(event.getLastActivityDate());
        event1.setInviteCode(event.getInviteCode());

        when(eventRepository.save(event)).thenReturn(event1);
        Event e = eventService.add(event);

        assertEquals(null,e);
        verify(eventRepository,times(0 )).save(event);

    }
    @Test
    public void importJSONTest(){

    }
}
