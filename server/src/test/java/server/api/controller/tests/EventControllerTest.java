package server.api.controller.tests;


import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.api.controllers.EventController;
import server.api.services.EventService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class EventControllerTest {
    @Mock
    private SimpMessagingTemplate smp;
    @Mock
    private EventService evServ;
    @InjectMocks
    private EventController sut;


    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);

    }
    @Test
    public void cannotAddNullEvent() {
        Event event = new Event();
        event.setTitle("Testing");
        when(evServ.add(event)).thenReturn(event);

        ResponseEntity<Event> response = sut.add(event);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(event, response.getBody());
        verify(evServ, times(1)).add(event);
    }
    @Test
    public void testGetById(){
        Event event = new Event();
        event.setTitle("testing");
        when(evServ.getById(1L)).thenReturn(event);
        ResponseEntity<Event> response = sut.getById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(event, response.getBody());
        verify(evServ, times(1)).getById(1L);
    }
    @Test
    public void testPut(){
        Event event = new Event();
        event.setId(1L);
        event.setTitle("Testing");


        try {
            when(evServ.put(eq(1L), any(Event.class))).thenReturn(Event.class.newInstance());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        ResponseEntity<Event> response = sut.put(event.getId(), event);

        verify(evServ, times(1)).put(eq(1L), eq(event));
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    public void testDelete(){

        try {
            when(evServ.delete(1L)).thenReturn(Event.class.newInstance());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        ResponseEntity<Event> response = sut.delete(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(evServ, times(1)).delete(1L);
    }
    @Test
    public void testGetByInviteCode(){
        Event e = new Event();
        e.setTitle("Testing");
        when(evServ.getByInviteCode(e.getInviteCode())).thenReturn(e);
        ResponseEntity<Event> response = sut.getByInviteCode(e.getInviteCode());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(e,response.getBody());
        verify(evServ,times(1)).getByInviteCode(e.getInviteCode());
    }
    @Test
    public void testGetEventByJson(){
        Event e = new Event();
        e.setTitle("Testing");
        e.setId(1L);
        when(evServ.getEventById(1L)).thenReturn(e.toString());
        ResponseEntity<Resource> response = sut.getEventJSON(1L);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(new ByteArrayResource(e.toString().getBytes()),response.getBody());
        verify(evServ,times(1)).getEventById(1L);
    }
    @Test
    public void testAddJson(){
        Event e = new Event();
        e.setTitle("Testing");
        when(evServ.importJSON(e)).thenReturn(e);
        ResponseEntity<Event> response = sut.addJSON(e);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(e, response.getBody());
        verify(evServ,times(1)).importJSON(e);
    }
    @Test
    public void testUpdate(){
        Event e = new Event();

        ResponseEntity<Event> response = (ResponseEntity<Event>) sut.getUpdates().getResult();
        assertEquals(null,response);
    }
}
