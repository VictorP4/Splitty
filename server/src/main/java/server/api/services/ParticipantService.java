package server.api.services;

import commons.Participant;
import org.springframework.stereotype.Service;
import server.database.ParticipantRepository;

@Service
public class ParticipantService {
    private final ParticipantRepository repo;

    /**
     * constructor for participant service
     * @param repo the participant repository
     */
    public ParticipantService(ParticipantRepository repo) {
        this.repo = repo;
    }

    /**
     * Creates new participant
     * @param participant new participant
     * @return saved participant
     */
    public Participant add(Participant participant){
        return repo.save(participant);
    }

    /**
     * Updates existing participant
     * @param id id of participant to modify
     * @param participant updated version
     * @return the modified participant
     */
    public Participant put(long id, Participant participant){
        if(id<0|| !repo.existsById(id)) return null;
        return repo.save(participant);
    }

    /**
     * Deleting given participant
     * @param id of participant
     * @return the deleted participant
     */
    public Participant delete(long id){
        if(id<0|| !repo.existsById(id)) return null;
        Participant deleted = repo.findById(id).get();
        repo.deleteById(id);
        return deleted;
    }


}
