package client.services;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddExpenseService {

    /**
     * Creates a new OverviewService.
     */
    public AddExpenseService() { }

    /**
     * updates the expanse in the event
     *
     * @param event the event that is expense is a part of
     * @param expense the updated expense
     */
    public Expense updateExp(Event event, Expense expense) {
        int index = 0;
        for (Expense e : event.getExpenses()) {
            if (e.getId().equals(expense.getId())) {
                event.getExpenses().set(index, expense);
                return expense;
            } else {
                index++;
            }
        }
        return expense;
    }

    /**
     * Returns a list of all the expenses in the event.
     *
     * @param event the event of which we want to return all the expenses.
     */
    public List<Long> getAllExpenses(Event event) {
        List<Long> ids = new ArrayList<>();
        for (Expense e : event.getExpenses()) {
            ids.add(e.getId());
        }
        return ids;
    }

    /**
     * Checks if every participant in the old expense is still a participant in the event
     * (in case participants have been deleted). Will return true, if all the participants in the expense
     * are still participants in the event, false otherwise.
     *
     *
     * @param event the event that contains the expense
     * @param prevExpense the previous values of the expense
     * @return a boolean that states whether every participant in the old expense is still a participant in the event
     */
    public Boolean checkExpenseParticipants(Event event, Expense prevExpense) {
        final AtomicBoolean b = new AtomicBoolean(true);
        List<Long> participantsIDs =
                event.getParticipants().stream()
                        .map(Participant::getId).toList();

        prevExpense.getInvolvedParticipants()
                .forEach((p)-> {
                    boolean contains = participantsIDs.contains(p.getId());
                    b.set(b.get() && contains);
                });

        boolean contains = participantsIDs.contains(prevExpense.getPaidBy().getId())
                && event.getTags().stream()
                        .map(Tag::getId).toList().contains(prevExpense.getTag().getId());

        return b.get() && contains;
    }
}
