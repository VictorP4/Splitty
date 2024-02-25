package server.api;
import commons.Event;
import commons.Expense;
import commons.Participant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;
import server.database.ExpensesRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/events")
public class ExpensesController {
    private final ExpensesRepository expRepo;
    private final EventRepository eventRepo;

    /**
     * Constructor with both repositories
     * @param expRepo expenses
     * @param eventRepo events
     */
    public ExpensesController(ExpensesRepository expRepo, EventRepository eventRepo) {
        this.expRepo = expRepo;
        this.eventRepo = eventRepo;
    }

    /**
     * Gets all expenses for the event by id
     * @param id of the event
     * @return all expenses
     */
    @GetMapping(path = {"/{id}/expenses"})
    public ResponseEntity<List<Expense>> getAll(@PathVariable("id") long id) {
        if(id<0 || !eventRepo.existsById(id)){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(eventRepo.findById(id).get().getExpenses());
    }

    /**
     * Create a new expense, apply it to the relevant event, add it to the database of expenses
     * @param id
     * @param expense
     * @return
     */
    @PostMapping(path = {"/{id}/expenses"})
    public ResponseEntity<Expense> addNew(@PathVariable("id") long id, @RequestBody Expense expense){
        Event event = eventRepo.getReferenceById((id));
        if (event == null || event.getTitle() == null || expense == null || expense.getTitle() == null) {
            return ResponseEntity.badRequest().build();
        }
        Expense newExp = expRepo.save(expense);

        List<Expense> oldExpenses = event.getExpenses();
        oldExpenses.add((newExp));
        event.setExpenses(oldExpenses);

        eventRepo.modifyEvent(event.getId(),event.getTitle(),event.getLastActivityDate(),event.getInviteCode());
        return ResponseEntity.ok(newExp);
    }

    /**
     * Changes an expense's data
     * @param id
     * @param expId
     * @param expense
     * @return
     */
    @PutMapping(path = "/{id}/expenses/{expId}")
    public ResponseEntity<Expense> update(@PathVariable("id") long id, @PathVariable("expId") long expId,
    @RequestBody Expense expense){
        if (id < 0 || !eventRepo.existsById(id) || expId < 0 || !expRepo.existsById(expId)) {
            return ResponseEntity.badRequest().build();
        }
        expRepo.modifyExpense(expense.getId(), expense.getTitle(), expense.getAmount());

        Expense newExp = expRepo.findById(expId).get();
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
        if (id < 0 || !eventRepo.existsById(id) || expId < 0 || !expRepo.existsById(expId)) {
            return ResponseEntity.badRequest().build();
        }
        Expense expense = expRepo.findById(expId).get();
        expRepo.deleteById(id);

        Event event = eventRepo.findById(id).get();
        List<Expense> expenses = event.getExpenses();
        expenses.remove(expense);
        event.setExpenses(expenses);

        eventRepo.modifyEvent(event.getId(),event.getTitle(),event.getLastActivityDate(),
            event.getInviteCode());
        return ResponseEntity.ok(expense);
    }

    /**
     * Returns the total expenses for an event
     * @param id
     * @return
     */
    @GetMapping(path = "/{id}/expenses/total")
    public ResponseEntity<Object> total(@PathVariable("id") long id){
        if (id < 0 || !eventRepo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Event event = eventRepo.getReferenceById(id);
        double sum = 0;
        for(Expense exp : event.getExpenses()){
            sum+=exp.getAmount();
        }
        return ResponseEntity.ok(sum);
    }

    /**
     * See how much you owe for the event, notably also can tell you you owe a debt to yourself,
     * would need access to the user's profile to add a check which would exclude yourself from
     * what you owe, it's also why I can't currently make the thing to see how much you're owed.
     * @param id
     * @return
     */
    @GetMapping(path = "/{id}/expenses/debts")
    public ResponseEntity<Map<String,Double>> debt(@PathVariable("id") long id){
        if (id < 0 || !eventRepo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Event event = eventRepo.getReferenceById(id);
        List<Expense> expenses = event.getExpenses();
        Map<String, List<Double>> debts = new HashMap<>();
        for(Expense expense : expenses){
            String buyer = expense.getPaidBy().getName();
            debts.putIfAbsent(buyer,new ArrayList<>());
            List<Double> toPay = debts.get(buyer);
            double owed = expense.getAmount()/(long) expense.getInvolvedParticipants().size();
            toPay.add(owed);
            debts.put(buyer,toPay);
        }
        return ResponseEntity.ok(debts);
    }

    /**
     * More or less the same thing as above, lists each person and their respective share.
     * @param id
     * @return
     */
    @GetMapping(path = "/{id}/expenses/shares")
    public ResponseEntity<Map<String,Double>> share(@PathVariable("id") long id){
        if (id < 0 || !eventRepo.existsById(id)) {
            return ResponseEntity.badRequest().build();
        }
        Event event = eventRepo.getReferenceById(id);
        List<Expense> expenses = event.getExpenses();
        Map<String, List<Double>> share = new HashMap<>();
        for(Expense expense : expenses){
            double part = expense.getAmount()/(long) expense.getInvolvedParticipants().size();
            for(Participant person : expense.getInvolvedParticipants()){
                share.putIfAbsent(person.getName(),new ArrayList<>());
                List<Double> toPay = share.get(person.getName());
                toPay.add(part);
                share.put(person.getName(),toPay);
            }
        }
        return ResponseEntity.ok(share);
    }
}
