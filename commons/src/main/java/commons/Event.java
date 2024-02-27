package commons;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private Date creationDate;
    private Date lastActivityDate;
    private String inviteCode;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "EVENT_PARTICIPANT",
        joinColumns = @JoinColumn(name="Event_id"),
        inverseJoinColumns = @JoinColumn(name = "Participant_id"))
    private List<Participant> participants;
    @OneToMany(cascade = CascadeType.PERSIST)
    @Transient
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

    /**
     * Retrieves the ID of the event.
     *
     * @return the ID of the event
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the event.
     *
     * @param id the ID of the event to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves the title of the event.
     *
     * @return the title of the event
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the event.
     *
     * @param title the title of the event to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the creation date of the event.
     *
     * @return the creation date of the event
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date of the event.
     *
     * @param creationDate the creation date of the event to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Retrieves the last activity date of the event.
     *
     * @return the last activity date of the event
     */
    public Date getLastActivityDate() {
        return lastActivityDate;
    }

    /**
     * Sets the last activity date of the event.
     *
     * @param lastActivityDate the last activity date of the event to set
     */
    public void setLastActivityDate(Date lastActivityDate) {
        this.lastActivityDate = lastActivityDate;
    }

    /**
     * Retrieves the invite code of the event.
     *
     * @return the invite code of the event
     */
    public String getInviteCode() {
        return inviteCode;
    }

    /**
     * Sets the invite code of the event.
     *
     * @param inviteCode the invite code of the event to set
     */
    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    /**
     * Retrieves the list of participants of the event.
     *
     * @return the list of participants of the event
     */
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * Sets the list of participants of the event.
     *
     * @param participants the list of participants to set
     */
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    /**
     * Retrieves the list of expenses of the event.
     *
     * @return the list of expenses of the event
     */
    public List<Expense> getExpenses() {
        return expenses;
    }

    /**
     * Sets the list of expenses of the event.
     *
     * @param expenses the list of expenses to set
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    /**
     * Adds a participant to the list of participants of the event.
     *
     * @param participant the participant to add
     */
    public void addParticipant(Participant participant) {
        this.participants.add(participant);
    }

    /**
     * Removes a participant from the list of participants of the event.
     *
     * @param participant the participant to remove
     */
    public void removeParticipant(Participant participant) {
        this.participants.remove(participant);
    }

    /**
     * Adds an expense to the list of expenses of the event.
     *
     * @param expense the expense to add
     */
    public void addExpense(Expense expense) {
        this.expenses.add(expense);
    }

    /**
     * Removes an expense from the list of expenses of the event.
     *
     * @param expense the expense to remove
     */
    public void removeExpense(Expense expense) {
        this.expenses.remove(expense);
    }

    /**
     * Checks if this event is equal to another object.
     *
     * @param o the object to compare
     * @return true if this event is equal to the other object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(title, event.title) &&
                Objects.equals(creationDate, event.creationDate) && Objects.equals(lastActivityDate, event.lastActivityDate) &&
                Objects.equals(inviteCode, event.inviteCode) && Objects.equals(participants, event.participants) && Objects.equals(expenses, event.expenses);
    }

    /**
     * Generates a hash code for this event.
     *
     * @return the hash code for this event
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, creationDate, lastActivityDate, inviteCode, participants, expenses);
    }
}
