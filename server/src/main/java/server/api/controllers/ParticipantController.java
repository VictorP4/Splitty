package server.api.controllers;


import commons.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import server.database.ParticipantRepository;
@RestController
@RequestMapping("/api/participants")
public class ParticipantController {
    private final ParticipantRepository repo;

    /**Constructor for controller
     *
     * @param repo Participant repository
     */
    public ParticipantController(ParticipantRepository repo) {
        this.repo = repo;
    }

    /**
     * Creates new participant
     * @param participant
     * @return saved participant
     */
    @PostMapping(path = {"","/"})
    public ResponseEntity<Participant> add(@RequestBody Participant participant){
        if(participant.getName()==null) return ResponseEntity.badRequest().build();
        Participant saved = repo.save(participant);
        return ResponseEntity.ok(saved);
    }
    @PutMapping(path = {"/{id}"})
    public ResponseEntity<Participant> put(@PathVariable("id") long id, @RequestBody Participant participant){
        if(id<0|| !repo.existsById(id)) return ResponseEntity.badRequest().build();
        Participant update = repo.save(participant);
        return ResponseEntity.ok(update);
    }
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Participant> delete(@PathVariable("id") long id){
        if(id<0|| !repo.existsById(id)) return ResponseEntity.badRequest().build();
        Participant deleted = repo.findById(id).get();
        repo.deleteById(id);
        return ResponseEntity.ok(deleted);
    }
}
