package client.services;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class AddExpenseService {

    private final ServerUtils serverUtils;

    /**
     * Creates a new OverviewService.
     *
     * @param utils The utility class for server interaction.
     */
    @Inject
    public AddExpenseService(ServerUtils utils) {
        this.serverUtils = utils;
    }

    /**
     * updates the expanse in the event
     *
     * @param event the event that is expense is a part of
     * @param expense the updated expense
     */
    public void updateExp(Event event, Expense expense) {
        int index = 0;
        for (Expense e : event.getExpenses()) {
            if (e.getId().equals(expense.getId())) {
                event.getExpenses().set(index, expense);
                return;
            } else {
                index++;
            }
        }
    }

    /**
     * adds a transfer to the event.
     *
     * @param event the event the expense has to be added to
     * @param globalExpense the global expense in the AddExpenseCtrl (this.expense)
     * @param transfer the transfer to be added
     */
    public void addTransfer(Event event, Expense globalExpense, Expense transfer) {
        if (globalExpense != null) {
            transfer.setId(globalExpense.getId());
        }
        serverUtils.addExpense(transfer, event.getId());
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
     * @param expense the current values of the expense
     * @param prevExpense the previous values of the expense
     * @return a boolean that states whether every participant in the old expense is still a participant in the event
     */
    public Boolean checkExpenseParticipants(Event event, Expense expense, Expense prevExpense) {
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
