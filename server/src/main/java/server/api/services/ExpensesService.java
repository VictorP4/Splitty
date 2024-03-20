package server.api.services;

import commons.Event;
import commons.Expense;
import commons.Participant;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpensesRepository;
import server.database.ParticipantRepository;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpensesService {
    private final ExpensesRepository expRepo;
    private final EventRepository eventRepo;
    private final ParticipantRepository participantRepo;
    public ExpensesService(ExpensesRepository expRepo, EventRepository eventRepo,
                           ParticipantRepository participantRepo) {
        this.expRepo = expRepo;
        this.eventRepo = eventRepo;
        this.participantRepo = participantRepo;
    }

    /**
     * Gets all expenses for the event by id
     * @param id of the event
     * @return all expenses
     */
    public List<Expense> getAll(long id) {
        if (id < 0 || !eventRepo.existsById(id)) {
            return null;
        }
        return eventRepo.findById(id).get().getExpenses();
    }

    /**
     * Create a new expense, apply it to the relevant event, add it to the database of expenses
     * @param id
     * @param expense
     * @return
     */
    public Expense addNew(long id, Expense expense){
        Event event = eventRepo.getReferenceById((id));
        if (event == null || event.getTitle() == null || expense == null || expense.getTitle() == null) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("##.00");
        Expense newExp = expRepo.save(expense);
        //updating debts
        Participant p = newExp.getPaidBy();
        double newDebt = p.getDebt()+Double.parseDouble(df.format(newExp.getAmount()));
        p.setDebt(newDebt);
        participantRepo.save(p);
        for(Participant people : newExp.getInvolvedParticipants()){
            if(p.getId().equals(people.getId())) people.setDebt(p.getDebt());
            newDebt = people.getDebt()-Double.parseDouble(df.format(((double)newExp.getAmount())/newExp.getInvolvedParticipants().size()));
            people.setDebt(newDebt);
            participantRepo.save(people);
        }
        //updating the list of expenses in the event
        List<Expense> oldExpenses = event.getExpenses();
        oldExpenses.add((newExp));
        event.setExpenses(oldExpenses);

        eventRepo.save(event);
        return newExp;
    }

    /**
     * Changes an expense's data
     * @param id
     * @param expId
     * @param expense
     * @return
     */
    public Expense update(long id,long expId, Expense expense){
        if (id < 0 || !eventRepo.existsById(id) || expId < 0 || !expRepo.existsById(expId)) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("##.00");
        Expense oldExp = expRepo.findById(expId).get();
        //resetting debts
        Participant p1 = oldExp.getPaidBy();
        double oldDebt = p1.getDebt()-Double.parseDouble(df.format(oldExp.getAmount()));
        p1.setDebt(oldDebt);
        participantRepo.save(p1);
        for(Participant people : oldExp.getInvolvedParticipants()){
            if(people.getId().equals(p1.getId())) people.setDebt(p1.getDebt());
            oldDebt = people.getDebt()+Double.parseDouble(df.format(((double)oldExp.getAmount())/oldExp.getInvolvedParticipants().size()));
            people.setDebt(oldDebt);
            participantRepo.save(people);
        }
        //updating expense
        oldExp.setAmount(expense.getAmount());
        oldExp.setInvolvedParticipants(expense.getInvolvedParticipants());
        oldExp.setPaidBy(expense.getPaidBy());
        oldExp.setTitle(expense.getTitle());
        oldExp.setDate(expense.getDate());
        //updating debts
        Participant p = oldExp.getPaidBy();
        double newDebt = p.getDebt()+Double.parseDouble(df.format(oldExp.getAmount()));
        p.setDebt(newDebt);
        participantRepo.save(p);
        for(Participant people : oldExp.getInvolvedParticipants()){
            if(people.getId().equals(p.getId())) people.setDebt(p.getDebt());
            newDebt = people.getDebt()-Double.parseDouble(df.format(((double)oldExp.getAmount())/oldExp.getInvolvedParticipants().size()));
            people.setDebt(newDebt);
            participantRepo.save(people);
        }

        Expense newExp = expRepo.save(oldExp); // ! ERROR OCCURS HERE
        return newExp;
    }

    /**
     * Deletes an expense and removes it from the list of expenses of the corresponding event
     * @param id id of the event
     * @param expId the id of the expense
     * @return the deleted item
     */
    public Expense delete(long id, long expId){
        if (id < 0 || !eventRepo.existsById(id) || expId < 0 || !expRepo.existsById(expId)) {
            return null;
        }
        DecimalFormat df = new DecimalFormat("##.00");
        Expense expense = expRepo.findById(expId).get();
        //resetting debts
        Participant p1 = expense.getPaidBy();
        double oldDebt = p1.getDebt()-Double.parseDouble(df.format(expense.getAmount()));
        p1.setDebt(oldDebt);
        participantRepo.save(p1);
        for(Participant people : expense.getInvolvedParticipants()){
            if(people.getId().equals(p1.getId())) people.setDebt(p1.getDebt());
            oldDebt = people.getDebt()+Double.parseDouble(df.format(((double)expense.getAmount())/expense.getInvolvedParticipants().size()));
            people.setDebt(oldDebt);
            participantRepo.save(people);
        }

        //deleting expense in event
        Event event = eventRepo.findById(id).get();
        List<Expense> expenses = event.getExpenses();
        expenses.remove(expense);
        event.setExpenses(expenses);
        eventRepo.save(event);

        //deleting expense
        expRepo.deleteById(expId);

        return expense;
    }

    /**
     * See how much you owe for the event, notably also can tell you you owe a debt to yourself,
     * would need access to the user's profile to add a check which would exclude yourself from
     * what you owe, it's also why I can't currently make the thing to see how much you're owed.
     * @param id
     * @return
     */

    public Map<String,List<Double>> debt(long id){
        if (id < 0 || !eventRepo.existsById(id)) {
            return null;
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
        return debts;
    }

    /**
     * More or less the same thing as above, lists each person and their respective share.
     * @param id
     * @return
     */
    public Map<String,List<Double>> share(long id){
        if (id < 0 || !eventRepo.existsById(id)) {
            return null;
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
        return share;
    }

    /**
     * Returns the total expenses for an event
     * @param id
     * @return
     */
    public Double total(long id) {
        if (id < 0 || !eventRepo.existsById(id)) {
            return null;
        }
        Event event = eventRepo.getReferenceById(id);
        double sum = 0;
        for (Expense exp : event.getExpenses()) {
            sum += exp.getAmount();
        }
        return sum;
    }



}
