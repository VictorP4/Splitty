package server.api.controllers;

import commons.Event;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.api.services.EventService;
import server.database.EventRepository;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventRepository repo;
    private final EventService evServ;
    private final SimpMessagingTemplate smt;

    /**
     * @param repo   the event repository
     * @param evServ
     * @param smt
     */
    public EventController(EventRepository repo, EventService evServ, SimpMessagingTemplate smt) {
        this.repo = repo;
        this.evServ = evServ;
        this.smt = smt;
    }

    /**
     *
     * @return all events
     */
    @GetMapping(path = { "", "/" })
    public ResponseEntity<List<Event>> getAll(HttpServletRequest request) {
//        if (request.getSession().getAttribute("adminLogged") == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
//        }
        return ResponseEntity.ok(repo.findAll());
    }

    /**
     * Finds the event by id
     * 
     * @param id
     * @return the event requested
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") long id) {
        Event ev = evServ.getById(id);
        if (ev == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(ev);
    }

    /**
     * Creates a new event
     * 
     * @param event
     * @return
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Event> add(@RequestBody Event event) {
        Event newEvent = evServ.add(event);
        if (newEvent == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(newEvent);
    }

    /**
     * Updates an event
     * 
     * @param id
     * @param event
     * @return the updated event
     */
    @PutMapping(path = { "/{id}" })
    public ResponseEntity<Event> put(@PathVariable("id") long id, @RequestBody Event event) {
        Event finalEv = evServ.put(id, event);
        if (finalEv == null)
            return ResponseEntity.badRequest().build();
        smt.convertAndSend("/topic/events", finalEv);
        return ResponseEntity.ok(finalEv);
    }

    /**
     * Deletes a specified event
     * 
     * @param id
     * @return the deleted event
     */
    @DeleteMapping(path = { "/{id}" })
    public ResponseEntity<Event> delete(@PathVariable("id") long id) {
        Event event = evServ.delete(id);
        if (event == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(event);
    }

    /**
     * Find event by the invite code
     * 
     * @param inviteCode the invite code of the event
     * @return the requested event
     */
    @GetMapping(path = { "/inviteCode/{inviteCode}" })
    public ResponseEntity<Event> getByInviteCode(@PathVariable("inviteCode") String inviteCode) {
        Event e = evServ.getByInviteCode(inviteCode);
        if (e == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(e);
    }
}
