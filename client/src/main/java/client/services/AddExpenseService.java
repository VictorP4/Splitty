package client.services;

import client.scenes.MainCtrl;
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
    private final MainCtrl mainCtrl;

    /**
     * Creates a new OverviewService.
     *
     * @param utils The utility class for server interaction.
     * @param mainCtrl The main controller of the application.
     */
    @Inject
    public AddExpenseService(ServerUtils utils, MainCtrl mainCtrl) {
        this.serverUtils = utils;
        this.mainCtrl = mainCtrl;
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
     * adds an expense to the event.
     *
     * @param event the event the expense has to be added to
     * @param globalExpense the global expense in the AddExpenseCtrl (this.expense)
     * @param expense the expense to be added
     */
    public void addExpense(Event event, Expense globalExpense ,Expense expense) {
        if (globalExpense != null) {
            expense.setId(globalExpense.getId());
        }
        List<Long> ids = new ArrayList<>();
        for (Expense e : event.getExpenses()) {
            ids.add(e.getId());
        }
        if (ids.contains(expense.getId())) {
            updateExp(event, expense);
            Expense newExp = serverUtils.updateExpense(event.getId(), expense);
            mainCtrl.addPrevExp(newExp);
        } else {
            Expense newExp = serverUtils.addExpense(expense, event.getId());
            mainCtrl.addPrevExp(newExp);
        }
    }

    /**
     * Checks if every participant in the old expense is still a participant in the event
     * (in case participants have been deleted). Will return true, if all the participants in the expense
     * are still participants in the event, false otherwise.
     *
     *
     * @param event the event that contains the expense
     * @param expense the expense we want to reset
     * @return a boolean that states whether every participant in the old expense is still a participant in the event
     */
    public Boolean checkExpense(Event event, Expense expense) {
        Expense prevExpense = mainCtrl.getPrevExp(expense.getId());
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
