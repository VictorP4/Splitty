package server.api.controllers;
import commons.Event;
import commons.Expense;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import server.api.services.ExpensesService;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/events")
public class ExpensesController {

    @Autowired
    private final ExpensesService expService;
    private final SimpMessagingTemplate smt;

    public ExpensesController(ExpensesService expService, SimpMessagingTemplate smt) {
        this.expService = expService;
        this.smt = smt;
    }

    /**
     * Gets all expenses for the event by id
     * @param id of the event
     * @return all expenses
     */
    @GetMapping(path = {"/{id}/expenses"})
    public ResponseEntity<List<Expense>> getAll(@PathVariable("id") long id) {
        List<Expense> expenses = expService.getAll(id);
        if (expenses == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(expenses);
    }

    /**
     * Create a new expense, apply it to the relevant event, add it to the database of expenses
     * @param id of the event
     * @param expense the expense to be created
     * @return ok response with the new expense
     */
    @PostMapping(path = {"/{id}/expenses"})
    public ResponseEntity<Expense> addNew(@PathVariable("id") long id, @RequestBody Expense expense){
        Expense newExp = expService.addNew(id,expense);
        if(newExp == null) return ResponseEntity.badRequest().build();
        Event modifiedEvent = (Event) Hibernate.unproxy(expService.getEvent(id));
        smt.convertAndSend("/topic/events",modifiedEvent);
        return ResponseEntity.ok(newExp);
    }

    /**
     * Changes an expense's data
     * @param id of the event
     * @param expId of the expense
     * @param expense to be updated
     * @return the updated expense
     */
    @PutMapping(path = "/{id}/expenses/{expId}")
    public ResponseEntity<Expense> update(@PathVariable("id") long id, @PathVariable("expId") long expId,
    @RequestBody Expense expense){
        Expense newExp = expService.update(id, expId, expense);
        if(newExp == null) return ResponseEntity.badRequest().build();
        smt.convertAndSend("/topic/events",Hibernate.unproxy(expService.getEvent(id)));
        return ResponseEntity.ok(newExp);
    }

    /**
     * Deletes an expense and removes it from the list of expenses of the corresponding event
     * @param id id of the event
     * @param expId the id of the expense
     * @return the deleted item
     */
    @DeleteMapping(path = "/{id}/expenses/{expId}")
    public ResponseEntity<Expense> delete(@PathVariable("id") long id, @PathVariable("expId") long expId){
        Expense exp = expService.delete(id, expId);
        if(exp == null) return ResponseEntity.badRequest().build();
        smt.convertAndSend("/topic/events",Hibernate.unproxy(expService.getEvent(id)));
        smt.convertAndSend("/topic/expenses",Hibernate.unproxy(exp));
        return ResponseEntity.ok(exp);
    }

    /**
     * Returns the total expenses for an event
     * @param id of the event
     * @return the total sum of the expenses
     */
    @GetMapping(path = "/{id}/expenses/total")
    public ResponseEntity<Double> total(@PathVariable("id") long id){
        Double total = expService.total(id);
        if(total == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(total);
    }

    /**
     * See how much you owe for the event, notably also can tell you you owe a debt to yourself,
     * would need access to the user's profile to add a check which would exclude yourself from
     * what you owe, it's also why I can't currently make the thing to see how much you're owed.
     * @param id of the event
     * @return the debts of the participants
     */
    @GetMapping(path = "/{id}/expenses/debts")
    public ResponseEntity<Map<String,List<Double>>> debt(@PathVariable("id") long id){
        Map<String,List<Double>> debts = expService.debt(id);
        if(debts == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(debts);
    }

    /**
     * More or less the same thing as above, lists each person and their respective share.
     * @param id of the event
     * @return the shares of the participants
     */
    @GetMapping(path = "/{id}/expenses/shares")
    public ResponseEntity<Map<String,List<Double>>> share(@PathVariable("id") long id) {
        Map<String, List<Double>> share = expService.share(id);
        if (share == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(share);
    }
}
