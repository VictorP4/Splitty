package commons;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private double amount;
    @ManyToOne(cascade = CascadeType.MERGE)
    private Participant paidBy;
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<Participant> involvedParticipants;
    private Date date;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(nullable = true)
    private Tag tag;
    private String currency;

    /**
     * Constructs an Expense with the specified title, amount, payer, involved participants, and date.
     *
     * @param title              the title of the expense
     * @param amount             the amount of the expense
     * @param paidBy             the participant who paid for the expense
     * @param involvedParticipants the list of participants involved in the expense
     * @param date               the date of the expense
     * @param currency           the currency of the event
     */
    public Expense(String title, double amount, Participant paidBy, List<Participant> involvedParticipants, Date date, Tag tag, String currency) {
        this.title = title;
        this.amount = amount;
        this.paidBy = paidBy;
        this.involvedParticipants = involvedParticipants;
        this.date = date;
        this.tag = tag;
        this.currency = currency;
    }

    /**
     * Default constructor for the Expense class for object mapper.
     */
    public Expense() {
        // for object mapper
    }

    /**
     * Retrieves the ID of the expense.
     *
     * @return the ID of the expense
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the expense.
     *
     * @param id the ID of the expense to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves the title of the expense.
     *
     * @return the title of the expense
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the expense.
     *
     * @param title the title of the expense to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the amount of the expense.
     *
     * @return the amount of the expense
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the expense.
     *
     * @param amount the amount of the expense to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Retrieves the participant who paid for the expense.
     *
     * @return the participant who paid for the expense
     */
    public Participant getPaidBy() {
        return paidBy;
    }

    /**
     * Sets the participant who paid for the expense.
     *
     * @param paidBy the participant who paid for the expense to set
     */
    public void setPaidBy(Participant paidBy) {
        this.paidBy = paidBy;
    }

    /**
     * Retrieves the list of participants involved in the expense.
     *
     * @return the list of participants involved in the expense
     */
    public List<Participant> getInvolvedParticipants() {
        return involvedParticipants;
    }

    /**
     * Sets the list of participants involved in the expense.
     *
     * @param involvedParticipants the list of participants involved in the expense to set
     */
    public void setInvolvedParticipants(List<Participant> involvedParticipants) {
        this.involvedParticipants = involvedParticipants;
    }

    /**
     * Retrieves the date of the expense.
     *
     * @return the date of the expense
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date of the expense.
     *
     * @param date the date of the expense to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * gets the tag of the expense
     * @return tag of the expense
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * sets the tag of the expense
     * @param tag the tag of the expense to set
     */
    public void setTag(Tag tag) {
        this.tag = tag;
    }

    /**
     * Retrieves the currency type of the expense.
     *
     * @return the title of the expense
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Sets the currency type of the expense.
     *
     * @param currency the currency type of the expense to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * Checks if this expense is equal to another object.
     *
     * @param o the object to compare
     * @return true if this expense is equal to the other object, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expense expense = (Expense) o;
        return Double.compare(expense.amount, amount) == 0 &&
                Objects.equals(id, expense.id) &&
                Objects.equals(title, expense.title) &&
                Objects.equals(paidBy, expense.paidBy) &&
                Objects.equals(involvedParticipants, expense.involvedParticipants) &&
                Objects.equals(date, expense.date) &&
                Objects.equals(tag, expense.tag) &&
                Objects.equals(currency, expense.currency);
    }

    /**
     * Generates a hash code for this expense.
     *
     * @return the hash code for this expense
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, title, amount, paidBy, involvedParticipants, date, tag, currency);
    }

    /**
     * to string method for an expense
     * @return string version of object expense
     */
    @Override
    public String toString() {
        return title + " " + amount + " " + currency +  ", " + (tag == null ? "no tag" : tag.getName());
    }
}

