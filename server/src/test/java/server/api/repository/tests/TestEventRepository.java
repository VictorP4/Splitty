package server.api.repository.tests;
import java.util.*;
import java.util.function.Function;



import commons.Participant;
import commons.Tag;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;

import commons.Event;
import server.database.EventRepository;

public class TestEventRepository implements EventRepository {
    public final List<Event> events = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();
    public void call(String name){
        calledMethods.add(name);
    }

    /**
     * auto-generated
     */
    @Override
    public void flush() {

    }
    /**
     * auto-generated
     */
    @Override
    public <S extends Event> S saveAndFlush(S entity) {
        return null;
    }
    /**
     * auto-generated
     */
    @Override
    public <S extends Event> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }
    /**
     * auto-generated
     */
    @Override
    public void deleteAllInBatch(Iterable<Event> entities) {

    }
    /**
     * auto-generated
     */
    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }
    /**
     * auto-generated
     */
    @Override
    public void deleteAllInBatch() {

    }
    /**
     * auto-generated
     */
    @Override
    public Event getOne(Long aLong) {
        return null;
    }
    /**
     * @param aLong the id
     * @return the event requested
     */
    @Override
    public Event getById(Long aLong) {
        call("getById");
        return find(aLong).get();
    }

    /**
     *
     * @param id
     * @return the requested event
     */
    private Optional<Event> find(Long id) {
        return events.stream().filter(q -> q.getId() == id).findFirst();
    }

    /**
     *
     * @param aLong the id.
     * @return the requested event
     */
    @Override
    public Event getReferenceById(Long aLong) {
        call("getReferenceById");
        return find(aLong).get();
    }
    /**
     * auto-generated
     */
    @Override
    public <S extends Event> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }
    /**
     * auto-generated
     */
    @Override
    public <S extends Event> List<S> findAll(Example<S> example) {
        return null;
    }
    /**
     * auto-generated
     */
    @Override
    public <S extends Event> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }
    /**
     * auto-generated
     */
    @Override
    public <S extends Event> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }
    /**
     * auto-generated
     */
    @Override
    public <S extends Event> long count(Example<S> example) {
        return 0;
    }
    /**
     * auto-generated
     */
    @Override
    public <S extends Event> boolean exists(Example<S> example) {
        return false;
    }
    /**
     * auto-generated
     */
    @Override
    public <S extends Event, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    /**
     * Saves the entity
     * @param entity must not be {@literal null}.
     * @return the entity saved
     *
     */
    @Override
    public <S extends Event> S save(S entity) {
        call("save");
        entity.setId((long) events.size());
        events.add(entity);
        return entity;
    }
    /**
     * auto-generated
     */
    @Override
    public <S extends Event> List<S> saveAll(Iterable<S> entities) {
        return null;
    }
    /**
     * auto-generated
     */
    @Override
    public Optional<Event> findById(Long aLong) {
        return Optional.empty();
    }

    /**
     * Checks if the requested event exists
     * @param aLong the id.
     * @return true if the event exists, false otherwise
     */
    @Override
    public boolean existsById(Long aLong) {
        call("existsById");
        return find(aLong).isPresent();
    }

    /**
     *
     * @return all events
     */
    @Override
    public List<Event> findAll() {
        calledMethods.add("findAll");
        return events;
    }

    @Override
    public List<Event> findAllById(Iterable<Long> longs) {
        return null;
    }

    /**
     *
     * @return the nubmer of current events
     */
    @Override
    public long count() {
        return events.size();
    }
    /**
     * auto-generated
     */
    @Override
    public void deleteById(Long aLong) {

    }
    /**
     * auto-generated
     */
    @Override
    public void delete(Event entity) {

    }
    /**
     * auto-generated
     */
    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }
    /**
     * auto-generated
     */
    @Override
    public void deleteAll(Iterable<? extends Event> entities) {

    }
    /**
     * auto-generated
     */
    @Override
    public void deleteAll() {

    }
    /**
     * auto-generated
     */
    @Override
    public List<Event> findAll(Sort sort) {
        return null;
    }
    /**
     * auto-generated
     */
    @Override
    public Page<Event> findAll(Pageable pageable) {
        return null;
    }

    /**
     * Modifies the specified event

     * @param id
     * @param title
     * @param lastActivityDate

     * @param inviteCode
     *
     */
    @Override
    public void modifyEvent(long id, String title, Date lastActivityDate, String inviteCode, List<Participant> participants, List<Tag> tags) {
        Event event = findById(id).get();
        event.setTitle(title);
        event.setLastActivityDate(lastActivityDate);
        event.setInviteCode(inviteCode);
        event.setParticipants(participants);
        event.setTags(tags);
    }

    /**
     * Get the event corresponding to the given invite code
     * @param inviteCode
     * @return the requested event
     */
    @Override
    public Event getByInviteCode(String inviteCode) {
        return events.stream().filter(x->x.getInviteCode().equals(inviteCode)).findFirst().get();
    }

}
