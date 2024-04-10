package server.api.service.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Event;
import commons.Expense;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
        Event event = new Event();
        event.setTitle("Test");
        Participant participant1 = new Participant();
        participant1.setName("Test1");
        participant1.setId(1L);
        Participant participant2 = new Participant();
        participant2.setName("Test2");
        participant2.setId(2L);
        event.getParticipants().add(participant1);
        event.getParticipants().add(participant2);
        Expense expense = new Expense("test",1,participant1, new ArrayList<>(List.of(participant2)),new Date(),null, "EUR");
        event.getExpenses().add(expense);
        when(eventRepository.save(event)).thenReturn(event);
        Event e = eventService.importJSON(event);
        assertEquals(event,e);
        verify(eventRepository,times(1)).save(event);

    }
    @Test
    public void putTest(){
        Event original = new Event();
        original.setTitle("Test");
        original.setId(1L);
        Event update = new Event();
        update.setTitle("TestUpdate");
        update.setId(1L);
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findById(1l)).thenReturn(Optional.of(original));
        when(eventRepository.save(any(Event.class))).thenReturn(update);
        Event result = eventService.put(1L,update);
        assertEquals(update,result);
        verify(eventRepository,times(1)).findById(1L);
        verify(eventRepository,times(1)).save(any(Event.class));
    }
    @Test
    public void deleteTest(){
        Event event = new Event();
        event.setTitle("Test");
        event.setId(1L);
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        Event result = eventService.delete(1L);
        assertEquals(event,result);
        verify(eventRepository,times(1)).findById(1L);
        verify(eventRepository,times(1)).deleteById(1L);

    }
    @Test
    public void getByInviteCodeTest(){
        Event event = new Event();
        event.setTitle("Test");
        when(eventRepository.getByInviteCode(event.getInviteCode())).thenReturn(event);
        Event result = eventService.getByInviteCode(event.getInviteCode());
        assertEquals(event,result);
        verify(eventRepository,times(1)).getByInviteCode(event.getInviteCode());
    }
    @Test
    public void getEventByIdTest(){
        Event event = new Event();
        event.setTitle("test");
        event.setId(1L);
        String string = event.toString();
        ObjectMapper map = new ObjectMapper();
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        String result = eventService.getEventById(1L);
        try {
            assertEquals(map.writeValueAsString(event),result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        verify(eventRepository,times(1)).findById(1L);

    }
    @Test
    public void findAllTest(){
        Event event1 = new Event();
        event1.setTitle("Test1");
        Event event2 = new Event();
        event2.setTitle("Test2");
        when(eventRepository.findAll()).thenReturn(List.of(event1,event2));
        List<Event> result = eventService.findAll();
        assertEquals(List.of(event1,event2),result);
        verify(eventRepository,times(1)).findAll();
    }
}
