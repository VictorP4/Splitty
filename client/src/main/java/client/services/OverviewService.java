package client.services;

import client.UserConfig;
import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.ws.rs.core.Response;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.inject.Inject;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OverviewService {

    private final ServerUtils serverUtils;
    private final UserConfig userConfig;
    private Map<Long, List<Expense>> previousExpenses;


    /**
     * Creates a new OverviewService.
     *
     * @param utils The utility class for server interaction.
     * @param userConfig the user configuration for persisted data.
     */
    @Inject
    public OverviewService(ServerUtils utils, UserConfig userConfig) {
        this.serverUtils = utils;
        this.userConfig = userConfig;
    }

    /**
     * Formats the date and returns a string version of it.
     *
     * @param date The date to be formatted.
     * @return the string of the formatted date.
     */
    public String formattedDate(java.util.Date date) {
        Date sqldate = new Date(date.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
        return dateFormat.format(sqldate);
    }

    /**
     * Returns all the expenses of an event.
     *
     * @param event the event the expenses have to be returned for.
     * @return an observable list of all expenses.
     */
    public ObservableList<Expense> getAllExpenses(Event event) {
        ObservableList<Expense> original = FXCollections.observableArrayList();
        if(event != null){
            for (Expense e : event.getExpenses()) {
                if (e.getTitle().equalsIgnoreCase("debt repayment")) {
                    continue;
                }
                original.add(e);
            }
        }
        return original;
    }

    /**
     * Returns a new list with all the expenses that are paid by the selected participants.
     *
     * @param event the event where the expense is
     * @param selected the participant the expenses have to be paid by
     * @return an observable list of the expenses the selected participant has paid for.
     */
    public ObservableList<Expense> getFromSelected(Event event, Participant selected) {
        ObservableList<Expense> original = FXCollections.observableArrayList();
        for (Expense e : event.getExpenses()) {
            if (e.getTitle().equalsIgnoreCase("debt repayment")) {
                continue;
            }
            if (e.getPaidBy().equals(selected)) {
                original.add(e);
            }
        }
        return original;
    }

    /**
     * Returns a new list with all the expenses that involve the selected participants.
     *
     * @param event the event where the expense is
     * @param selected the participant the expenses have to involve
     * @return an observable list of all the expenses the selected participant was involved in
     */
    public ObservableList<Expense> getIncludingSelected(Event event, Participant selected) {
        ObservableList<Expense> original = FXCollections.observableArrayList();
        for (Expense e : event.getExpenses()) {
            if (e.getTitle().equalsIgnoreCase("debt repayment")) {
                continue;
            }
            if (e.getPaidBy().equals(selected) ||
                    e.getInvolvedParticipants().contains(selected)) {
                original.add(e);
            }
        }
        return original;
    }

    /**
     * Creates and returns a string of the involvedParticipants list.
     *
     * @param involvedParticipants the list of involved participants
     * @return a string of the involvedParticipants list.
     */
    public String setParticipantsString(List<Participant> involvedParticipants) {
        StringBuilder participantString = new StringBuilder();
        for (int i = 0; i < involvedParticipants.size(); i++) {
            participantString.append(involvedParticipants.get(i).getName());
            if (i < involvedParticipants.size() - 1) {
                participantString.append(", ");
            }
        }
        return participantString.toString();
    }

    /**
     * Converts the currency of all the expenses.
     *
     * @param a The list with expenses to be converted.
     * @return a new list of expenses with the converted currencies.
     */
    public List<Expense> convertCurrency(List<Expense> a) {
        String c = userConfig.getCurrencyConfig();
        for (Expense b : a) {
            if (!b.getCurrency().equals(c)) {
                int amn = (int) (serverUtils.convertCurrency(b.getAmount(), b.getCurrency(),
                        c, new Date(b.getDate().getTime()).toLocalDate()) * 100);
                b.setAmount((double) amn / 100);
                b.setCurrency(c);
            }
        }
        return a;
    }

    /**
     * marks if an admin is accessing the event overview (true), or not (false)
     *
     * @param b the boolean that describes whether the admin is accessing an event overview.
     */
    public void setAdmin(boolean admin, boolean b) {
        admin = b;
    }

    /**
     * Deletes the expense from the cached ones
     *
     * @param previousExpenses the map of cached expenses.
     * @param expense the expense to be deleted from the cache
     */
    public void deletePrevExp(Map<Long, List<Expense>> previousExpenses, Expense expense) {
        if (previousExpenses.get(expense.getId()) != null) {
            previousExpenses.get(expense.getId()).removeLast();
            if (previousExpenses.get(expense.getId()).isEmpty())
                previousExpenses.remove(expense.getId());
        }
    }

    /**
     * add an expense to the cache
     *
     * @param previousExpenses the map of cached expenses
     * @param expense the expense to be added to the cache
     */
    public void addPrevExp(Map<Long, List<Expense>> previousExpenses, Expense expense) {
        if (previousExpenses.get(expense.getId()) == null) {
            previousExpenses.put(expense.getId(), new ArrayList<>());
            previousExpenses.get(expense.getId()).add(expense);
        } else
            previousExpenses.get(expense.getId()).add(expense);
    }

    /**
     * Returning the previous version of the expense stored in the cache
     *
     * @param id the id of the expense
     * @return the previous version of the expense
     */
    public Expense getPrevExp(Map<Long, List<Expense>> previousExpenses, Long id) {
        if (previousExpenses.get(id) == null)
            return null;
        return previousExpenses.get(id).getLast();
    }

    /**
     * Get the currency
     *
     * @return the currency
     */
    public String getCurrency() {
        return userConfig.getCurrencyConfig();
    }

    /**
     * Deletes the selected expense.
     *
     * @param event the event the expense is in
     * @param expense the expense to be deleted
     */
    public void deleteExpense(Event event, Expense expense) {
        try {
            Response response = serverUtils.deleteExpense(event.getId(),
                    expense);
            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                System.out.println("OK! good job " + response.getStatus());
                deletePrevExp(previousExpenses, expense);
            } else {
                System.out.println("Status code: " + response.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes a participant from the event. It also removed the participant from
     * any related expenses or removed
     * the expense all together if they are the only involved participant.
     *
     * @param event the event where the participant has to be removed of.
     * @param contact the participant to be removed from the event and expenses
     */
    public void removeParticipantFromEvent(Event event, Participant contact) {
        event.removeParticipant(contact);
        serverUtils.updateEvent(event);
        List<Expense> toDelete = new ArrayList<>();
        for (Expense expense : event.getExpenses()) {
            if (expense.getPaidBy().equals(contact))
                toDelete.add(expense);
            else if (expense.getInvolvedParticipants().contains(contact)) {
                if (expense.getInvolvedParticipants().size() == 1)
                    toDelete.add(expense);
                else {
                    expense.getInvolvedParticipants().remove(contact);
                    serverUtils.updateExpense(event.getId(), expense);
                }
            }
        }
        for (Expense expense1 : toDelete) {
            serverUtils.deleteExpense(event.getId(), expense1);
        }
        serverUtils.deleteParticipant(contact);
        event.setParticipants(serverUtils.getEvent(event.getId()).getParticipants());
        serverUtils.updateEvent(event);
    }

    /**
     * Updates participant information of the participant in the given event.
     *
     * @param event the event where the participant has to be updated for.
     * @param participant The participant to be updated.
     */
    public void updateParticipant(Event event, Participant participant) {
        boolean participantExists = false;
        int index = -1;
        for (Participant p : event.getParticipants()) {
            if (Objects.equals(p.getId(), participant.getId())) {
                index = event.getParticipants().indexOf(p);
                participantExists = true;
                break;
            }
        }
        if (!participantExists) {
            Participant newPart = serverUtils.addParticipant(participant);
            event.getParticipants().add(newPart);
        } else {
            serverUtils.updateParticipant(participant);
            event.getParticipants().set(index, participant);
        }
        serverUtils.updateEvent(event);
    }
}
