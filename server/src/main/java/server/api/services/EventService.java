package server.api.services;

import commons.Event;
import server.database.EventRepository;

public class EventService {
    private EventRepository repo;

    public Event getById(long id){
        if (id < 0 || !repo.existsById(id)) {
            return null;
        }
        return repo.findById(id).get();
    }

    public Event add(Event event){
        if (event == null || event.getTitle() == null) {
            return null;
        }
        Event saved = repo.save(event);
        return saved;
    }

    public Event put(long id, Event event){
        if (id < 0 || !repo.existsById(id)) {
            return null;
        }
        Event update = repo.findById(id).get();
        update.setParticipants(event.getParticipants());
        update.setTitle(event.getTitle());
        update.setLastActivityDate(event.getLastActivityDate());
        update.setInviteCode(event.getInviteCode());
        update.setTags(event.getTags());

        Event finalUpdate = repo.save(update);
        return finalUpdate;
    }

    public Event delete(long id){
        if (id < 0 || !repo.existsById(id)) {
            return null;
        }
        Event event = repo.findById(id).get();
        repo.deleteById(id);
        return event;
    }

    public Event getByInviteCode(String inviteCode){
        Event e = repo.getByInviteCode(inviteCode);
        if(e==null) return null;
        else return e;
    }
}
