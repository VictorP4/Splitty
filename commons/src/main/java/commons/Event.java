import commons.Participant;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class Event {
    private Long id;
    private String title;
    private Date creationDate;
    private Date lastActivityDate;
    private String inviteCode;
    private List<Participant> participants;
    private List<Expense> expenses;


    /**
     * Default constructor for the Event class.
     * Initializes the participants and expenses lists as empty ArrayLists.
     * Sets the creationDate and lastActivityDate to the current date and time.
     */
    public Event() {
        this.participants = new ArrayList<>();
        this.expenses = new ArrayList<>();
        this.creationDate = new Date();
        this.lastActivityDate = this.creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
    }

    public void removeParticipant(Participant participant) {
        this.participants.remove(participant);
    }

    public void addExpense(Expense expense) {
        this.expenses.add(expense);
    }

    public void removeExpense(Expense expense) {
        this.expenses.remove(expense);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(title, event.title) && Objects.equals(creationDate, event.creationDate) && Objects.equals(lastActivityDate, event.lastActivityDate) && Objects.equals(inviteCode, event.inviteCode) && Objects.equals(participants, event.participants) && Objects.equals(expenses, event.expenses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, creationDate, lastActivityDate, inviteCode, participants, expenses);
    }
}
