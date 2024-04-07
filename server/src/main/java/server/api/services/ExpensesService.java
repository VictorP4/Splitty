package server.api.services;

import commons.Event;
import commons.Expense;
import commons.Participant;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpensesRepository;
import server.database.ParticipantRepository;

import java.sql.Date;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

@Service
public class ExpensesService {
    private final ExpensesRepository expRepo;
    private final EventRepository eventRepo;
    private final ParticipantRepository participantRepo;
    private final CurrencyService currencyService;
    public ExpensesService(ExpensesRepository expRepo, EventRepository eventRepo,
                           ParticipantRepository participantRepo, CurrencyService currencyService) {
        this.expRepo = expRepo;
        this.eventRepo = eventRepo;
        this.participantRepo = participantRepo;
        this.currencyService = currencyService;
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
     * @param id of the event
     * @param expense to be applied
     * @return the expense
     */
    public Expense addNew(long id, Expense expense){
        Event event = eventRepo.getReferenceById((id));
        if (event.getTitle() == null || expense == null || expense.getTitle() == null) {
            return null;
        }
        // forcing the decimal format to be '.', since depending on ont the user location, the DecimalFormat works differently
        Locale currentLocale = Locale.getDefault();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
        DecimalFormat df = new DecimalFormat("##.00", otherSymbols);
        otherSymbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(otherSymbols);
        if(expense.getInvolvedParticipants().contains(expense.getPaidBy())){
            expense.getInvolvedParticipants().remove(expense.getPaidBy());
            expense.getInvolvedParticipants().add(expense.getPaidBy());
        }
        Expense newExp = expRepo.save(expense);
        double amount = newExp.getCurrency().equals("EUR") ? newExp.getAmount() :
                currencyService.convertCurrency(newExp.getAmount(), newExp.getCurrency(), "EUR",new Date(newExp.getDate().getTime()).toLocalDate()).getBody();
        //updating debts
        Participant p = newExp.getPaidBy();

        double newDebt = p.getDebt()+Double.parseDouble(df.format(amount));
        p.setDebt(newDebt);
        participantRepo.save(p);
        for(int i=0; i<expense.getInvolvedParticipants().size();i++){
            Participant people = expense.getInvolvedParticipants().get(i);
            if(people.getId().equals(p.getId())) {
                expense.getInvolvedParticipants().remove(i);
                expense.getInvolvedParticipants().add(i,p);
                people = expense.getInvolvedParticipants().get(i);
            }
            newDebt = people.getDebt()-Double.parseDouble(df.format(((double)amount)/newExp.getInvolvedParticipants().size()));
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
     * @param id of the event
     * @param expId of the expense
     * @param expense the expense to be updated
     * @return the new expense
     */
    public Expense update(long id,long expId, Expense expense){
        if (id < 0 || !eventRepo.existsById(id) || expId < 0 || !expRepo.existsById(expId)) {
            return null;
        }

        Locale currentLocale = Locale.getDefault();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
        DecimalFormat df = new DecimalFormat("##.00", otherSymbols);
        otherSymbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(otherSymbols);
        Expense oldExp = expRepo.findById(expId).get();
        double amount1 = oldExp.getCurrency().equals("EUR") ? oldExp.getAmount() :
                currencyService.convertCurrency(oldExp.getAmount(), oldExp.getCurrency(),"EUR" ,new Date(oldExp.getDate().getTime()).toLocalDate()).getBody();
        //resetting debts
        Participant p1 = oldExp.getPaidBy();
        double oldDebt = p1.getDebt()-Double.parseDouble(df.format(amount1));
        p1.setDebt(oldDebt);
        participantRepo.save(p1);
        for(Participant people : oldExp.getInvolvedParticipants()){
            if(people.getId().equals(p1.getId())) people.setDebt(p1.getDebt());
            oldDebt = people.getDebt()+Double.parseDouble(df.format(((double)amount1)/oldExp.getInvolvedParticipants().size()));
            people.setDebt(oldDebt);
            participantRepo.save(people);
        }

        //updating expense
        oldExp.setAmount(expense.getAmount());
        oldExp.setInvolvedParticipants(expense.getInvolvedParticipants());
        oldExp.setPaidBy(expense.getPaidBy());
        oldExp.setTitle(expense.getTitle());
        oldExp.setDate(expense.getDate());
        oldExp.setTag(expense.getTag());
        oldExp.setCurrency(expense.getCurrency());
        double amount = expense.getCurrency().equals("EUR") ? expense.getAmount() :
                currencyService.convertCurrency(expense.getAmount(), expense.getCurrency(),"EUR" ,new Date(expense.getDate().getTime()).toLocalDate()).getBody();
        //updating debts
        oldExp.setPaidBy(participantRepo.getById(oldExp.getPaidBy().getId()));
        Participant p = oldExp.getPaidBy();
        double newDebt = p.getDebt()+Double.parseDouble(df.format(amount));
        p.setDebt(newDebt);
        participantRepo.save(p);
        for(int i=0; i<oldExp.getInvolvedParticipants().size();i++){
            Participant people = oldExp.getInvolvedParticipants().get(i);
            if(people.getId().equals(p.getId())) {
                oldExp.getInvolvedParticipants().remove(i);
                oldExp.getInvolvedParticipants().add(i,p);
                people = oldExp.getInvolvedParticipants().get(i);
            }
            else{
                oldExp.getInvolvedParticipants().remove(i);
                oldExp.getInvolvedParticipants().add(i,participantRepo.findById(people.getId()).get());
                people = oldExp.getInvolvedParticipants().get(i);
            }
            newDebt = people.getDebt()-Double.parseDouble(df.format(((double)amount)/oldExp.getInvolvedParticipants().size()));
            people.setDebt(newDebt);
            participantRepo.save(people);
        }
        Expense newExp = expRepo.save(oldExp);
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

        Locale currentLocale = Locale.getDefault();
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
        DecimalFormat df = new DecimalFormat("##.00", otherSymbols);
        otherSymbols.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(otherSymbols);
        Expense expense = expRepo.findById(expId).get();
        double amount = expense.getCurrency().equals("EUR") ? expense.getAmount() :
                currencyService.convertCurrency(expense.getAmount(), expense.getCurrency(), "EUR",new Date(expense.getDate().getTime()).toLocalDate()).getBody();
        //resetting debts
        Participant p1 = expense.getPaidBy();
        double oldDebt = p1.getDebt()-Double.parseDouble(df.format(amount));
        p1.setDebt(oldDebt);
        participantRepo.save(p1);
        for(Participant people : expense.getInvolvedParticipants()){
            if(people.getId().equals(p1.getId())) people.setDebt(p1.getDebt());
            oldDebt = people.getDebt()+Double.parseDouble(df.format(((double)amount)/expense.getInvolvedParticipants().size()));
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
     * @param id of the event
     * @return the map of debts
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
     * @param id of the event
     * @return the map of shares
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
     * @param id of the event
     * @return the total sum of expenses
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
    /**
     * Used for the updates handling for the websockets
     * @param id event id
     * @return the found event
     *
     */
    public Event getEvent(Long id){
        return eventRepo.findById(id).get();
    }

}
