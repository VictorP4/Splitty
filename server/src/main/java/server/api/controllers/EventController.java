package server.api.controllers;

import commons.Event;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.api.services.EventService;
import server.database.EventRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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

    private Map<Object, Consumer<Event>> listeners = new HashMap<>();

    /**
     * long polling for new or updated events(used for admin event overview)
     * @return the new or updated event, or http status 204 no content
     */
    @GetMapping("/updates")
    public DeferredResult<ResponseEntity<Event>> getUpdates(){
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var res = new DeferredResult<ResponseEntity<Event>>(5000L,noContent);
        var key = new Object();
        listeners.put(key, e -> {
            res.setResult(ResponseEntity.ok(e));
        });
        res.onCompletion(()->listeners.remove(key));
        return res;
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
        listeners.forEach((k,l)->{
            l.accept(newEvent);
        });
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
        listeners.forEach((k,l) -> {
            l.accept(finalEv);
        });
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

    /**
     * Find event by id
     * @param id id of the event
     * @return Json representation of the requested event
     */
    @GetMapping(path = {"/id"}, consumes="application/json")
    public ResponseEntity<Resource> getEventJSON(@PathVariable("id") long id){

//        HttpServletRequest req;
//        if(req.getSession().getAttribut() != "adminLogged"){
//            return  ResponseEntity.status(HttpStatus.FORBIDDEN).build(); //"not an admin"
//        }
        try{
            String event = evServ.getEventById(id);
            byte[] eventBytes = event.getBytes();
            ByteArrayResource resource = new ByteArrayResource(eventBytes);

            return ResponseEntity.ok().body(resource);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
