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

    /**
     * Gets the event by its id
     *
     * @param id of the event to get
     * @return event
     */
    public Event getById(long id){
        if (id < 0 || !repo.existsById(id)) {
            return null;
        }
        return repo.findById(id).get();
    }

    /**
     * saves the event to the repository
     * @param event event to add
     * @return saved event
     */
    public Event add(Event event){
        if (event == null || event.getTitle() == null) {
            return null;
        }
        Event saved = repo.save(event);
        return saved;
    }

    /**
     * adds an imported event to the repository
     * by first adding its tags, participants and expenses to their repositories
     * @param event event to add
     * @return saved event
     */
    public Event importJSON(Event event){

        if (event == null || event.getTitle() == null) {
            return null;
        }
        List<Participant> list = event.getParticipants();
        try{
            for(Expense expense : event.getExpenses()){
                for(int i=0;i< expense.getInvolvedParticipants().size();i++){
                    int k = i;
                    Participant p = list.stream().filter(x->x.getId().equals(expense.getInvolvedParticipants().get(k).getId())).findFirst().get();
                    expense.getInvolvedParticipants().remove(i);
                    expense.getInvolvedParticipants().add(i,p);
                }
                Participant p = list.stream().filter(x->x.getId().equals(expense.getPaidBy().getId())).findFirst().get();
                expense.setPaidBy(p);
                if(expense.getTag()!=null){
                    Tag tag = event.getTags().stream().filter(x->x.getId().equals(expense.getTag().getId())).findFirst().get();
                    expense.setTag(tag);
                }
            }
            participantRepository.saveAll(event.getParticipants());
            tagRepository.saveAll(event.getTags());
            expensesRepository.saveAll(event.getExpenses());
            return repo.save(event);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Updates the event
     *
     * @param id of the event to update
     * @param event updated version
     * @return updated event
     */
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

        Event finalUpdate = repo.save(update);
        return finalUpdate;
    }

    /**
     * Deletes the event
     *
     * @param id id of the event to delete
     * @return event
     */
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

    /**
     * Gets an event by its invite code
     *
     * @param inviteCode of the event
     * @return the requested event
     */
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
            return map.writeValueAsString(repo.findById(id).get());
        }
        catch (JsonProcessingException jpe){
            return "error" + jpe.getMessage();
        }
    }

    /**
     * find all events
     * @return
     */
    public List<Event> findAll() {
        return repo.findAll();
    }
}
