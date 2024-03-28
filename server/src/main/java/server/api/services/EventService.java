package server.api.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import server.database.EventRepository;


import java.util.Date;

import server.database.ExpensesRepository;
import server.database.ParticipantRepository;
import server.database.TagRepository;

import java.util.List;


@Service
public class EventService {
    private EventRepository repo;

    private ParticipantRepository participantRepository;
    private ExpensesRepository expensesRepository;
    private TagRepository tagRepository;

    private ObjectMapper map = new ObjectMapper();


    public EventService(EventRepository repo, ParticipantRepository participantRepository, ExpensesRepository expensesRepository, TagRepository tagRepository) {
        this.repo = repo;
        this.participantRepository = participantRepository;
        this.expensesRepository = expensesRepository;
        this.tagRepository = tagRepository;
    }

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
        update.setLastActivityDate(new Date());
        update.setInviteCode(event.getInviteCode());
        update.setTags(event.getTags());
        update.setPreferredCurrency(event.getPreferredCurrency());

        Event finalUpdate = repo.save(update);
        return finalUpdate;
    }

    public Event delete(long id){
        if (id < 0 || !repo.existsById(id)) {
            return null;
        }
        Event event = repo.findById(id).get();
        Event placeholder = (Event) Hibernate.unproxy(event);
        List<Participant> participantList = placeholder.getParticipants();
        Hibernate.initialize(participantList);
        List<Expense> expenseList = placeholder.getExpenses();
        Hibernate.initialize(expenseList);
        List<Tag> tagList = placeholder.getTags();
        Hibernate.initialize(tagList);
        repo.deleteById(id);
        for(Expense expense:expenseList){
            expensesRepository.deleteById(expense.getId());
        }
        for(Participant participant:participantList){
            participantRepository.deleteById(participant.getId());
        }
        for(Tag tag:tagList){
            tagRepository.deleteById(tag.getId());
        }
        return event;
    }

    public Event getByInviteCode(String inviteCode){
        Event e = repo.getByInviteCode(inviteCode);
        if(e==null) return null;
        else return e;
    }

    /**
     * Gets an event by its id
     * @param id id of the event to get
     * @return a string of the requested event
     */
    public String getEventById(Long id) {
        try{
            return map.writeValueAsString(repo.getById(id));
        }
        catch (JsonProcessingException jpe){
            return "error" + jpe.getMessage();
        }
    }
}
