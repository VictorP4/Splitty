package server.api.controllers;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Event;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.api.services.EventService;
import server.database.EventRepository;


@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventRepository repo;
    @Autowired
    private final EventService evServ;
    /**
     * @param repo   the event repository
     * @param evServ
     */
    public EventController(EventRepository repo, EventService evServ) {
        this.repo = repo;
        this.evServ = evServ;
    }

    /**
     *
     * @return all events
     */
    @GetMapping(path = { "", "/" })
    public ResponseEntity<?> getAll(HttpServletRequest request) {
        if(request.getSession().getAttribute("adminLogged") == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only viewable by admins");
        }
        else return ResponseEntity.ok(repo.findAll());
    }

    /**
     * Finds the event by id
     * @param id
     * @return the event requested
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") long id) {
        Event ev = evServ.getById(id);
        if(ev == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(ev);
    }

    /**
     * Creates a new event
     * @param event
     * @return
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Event> add(@RequestBody Event event) {
        Event newEvent = evServ.add(event);
        if(newEvent == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(newEvent);
    }

    /**
     * Updates an event
     * @param id
     * @param event
     * @return the updated event
     */
    @PutMapping(path = {"/{id}"})
    public ResponseEntity<Event> put(@PathVariable("id") long id, @RequestBody Event event){
        Event finalEv = evServ.put(id, event);
        if (finalEv == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(finalEv);
    }

    /**
     * Deletes a specified event
     * @param id
     * @return the deleted event
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Event> delete(@PathVariable("id") long id){
        Event event = evServ.delete(id);
        if(event == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(event);
    }

    /**
     * Find event by the invite code
     * @param inviteCode the invite code of the event
     * @return the requested event
     */
    @GetMapping(path={"/inviteCode/{inviteCode}"})
    public ResponseEntity<Event> getByInviteCode(@PathVariable("inviteCode") String inviteCode){
        Event e = evServ.getByInviteCode(inviteCode);
        if(e == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(e);
    }

    /**
     * Find event by id
     * @param id id of the event
     * @return Json representation of the requested event
     */
    @GetMapping(path = {"/id"}, consumes="application/json")
    public ResponseEntity<Resource> getEventJSON(@PathVariable("id") long id){

//        //Check if user is an admin somehow
//        if(false){ //!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
//            ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not an admin");        }

        try{
            ObjectMapper map = new ObjectMapper();
            map.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
