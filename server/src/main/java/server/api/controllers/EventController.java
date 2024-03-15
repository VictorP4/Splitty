package server.api.controllers;


import commons.Event;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;


@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventRepository repo;
    /**
     * @param repo            the event repository
     */
    public EventController(EventRepository repo) {
        this.repo = repo;

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
     * @param id The id if the event
     * @return the event requested
     */
    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }

    /**
     * Creates a new event
     * @param event The event to be added
     * @return The event that has been adding
     */
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Event> add(@RequestBody Event event) {
        if (event.getTitle() == null) {
            return ResponseEntity.badRequest().build();
        }

        Event saved = repo.save(event);
        return ResponseEntity.ok(saved);
    }

    /**
     * Updates an event
     * @param id
     * @param event
     * @return the updated event
     */
    @PutMapping(path = {"/{id}"})
    public ResponseEntity<Event> put(@PathVariable("id") long id, @RequestBody Event event){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Event update = repo.findById(id).get();
        update.setParticipants(event.getParticipants());
        update.setTitle(event.getTitle());
        update.setLastActivityDate(event.getLastActivityDate());
        update.setInviteCode(event.getInviteCode());
        update.setTags(event.getTags());

        Event finalUpdate = repo.save(update);
        return ResponseEntity.ok(finalUpdate);
    }

    /**
     * Deletes a specified event
     * @param id
     * @return the deleted event
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Event> delete(@PathVariable("id") long id){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Event event = repo.findById(id).get();
        repo.deleteById(id);
        return ResponseEntity.ok(event);
    }

    /**
     * Find event by the invite code
     *
     * @param inviteCode the invite code of the event
     * @return the requested event
     */
    @GetMapping(path = "/inviteCode/{inviteCode}")
    public ResponseEntity<Event> getByInviteCode(@PathVariable("inviteCode") String inviteCode){
        Event e = repo.getByInviteCode(inviteCode);

        if(e==null) return ResponseEntity.badRequest().build();
        else return ResponseEntity.ok(e);
    }

}
