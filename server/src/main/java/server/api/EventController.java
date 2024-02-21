package server.api;


import java.util.List;


import commons.Participant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commons.Event;
import server.database.EventRepository;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventRepository repo;

    /**
     *
     * @param repo the event repository
     */
    public EventController(EventRepository repo) {
        this.repo = repo;
    }
    @GetMapping(path = { "", "/" })
    public List<Event> getAll() {
        return repo.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Event> getById(@PathVariable("id") long id) {
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(repo.findById(id).get());
    }
    @PostMapping(path = { "", "/" })
    public ResponseEntity<Event> add(@RequestBody Event event) {

        if (event.getTitle() == null) {
            return ResponseEntity.badRequest().build();
        }

        Event saved = repo.save(event);
        return ResponseEntity.ok(saved);
    }
    @PutMapping(path = {"/{id}"})
    public ResponseEntity<Event> addParticipant(@PathVariable("id") long id, @RequestBody Participant participant){
        if (id < 0 || !repo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Event update = repo.findById(id).get();
        List<Participant> participantList = update.getParticipants();
        participantList.add(participant);
        repo.addParticipant(participantList,id);
        update = repo.findById(id).get();
        return ResponseEntity.ok(update);
    }


}
