package server.api.controllers;


import commons.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import server.database.ParticipantRepository;
@RestController
@RequestMapping("/api/participants")
public class ParticipantController {
    private final ParticipantRepository repo;
    private final SimpMessagingTemplate smt;

    /**
     * Constructor for controller
     *
     * @param repo Participant repository
     * @param smt
     */
    public ParticipantController(ParticipantRepository repo, SimpMessagingTemplate smt) {
        this.repo = repo;
        this.smt = smt;
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

    /**
     *
     * @param id id of participant to modify
     * @param participant participant to change
     * @return updated participant
     */
    @PutMapping(path = {"/{id}"})
    public ResponseEntity<Participant> put(@PathVariable("id") long id, @RequestBody Participant participant){
        if(id<0|| !repo.existsById(id)) return ResponseEntity.badRequest().build();
        Participant update = repo.save(participant);
        return ResponseEntity.ok(update);
    }

    /**
     *
     * @param id participant to delete
     * @return the deleted participant
     */
    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Participant> delete(@PathVariable("id") long id){
        if(id<0|| !repo.existsById(id)) return ResponseEntity.badRequest().build();
        Participant deleted = repo.findById(id).get();
        repo.deleteById(id);
        return ResponseEntity.ok(deleted);
    }
}
