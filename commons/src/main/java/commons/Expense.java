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

    @OneToOne(cascade = CascadeType.PERSIST)
    private Participant paidBy;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<Participant> involvedParticipants;
    private Date date;

    /**
     * Constructs an Expense with the specified title, amount, payer, involved participants, and date.
     *
     * @param title              the title of the expense
     * @param amount             the amount of the expense
     * @param paidBy             the participant who paid for the expense
     * @param involvedParticipants the list of participants involved in the expense
     * @param date               the date of the expense
     */
    public Expense(String title, double amount, Participant paidBy, List<Participant> involvedParticipants, Date date) {
        this.title = title;
        this.amount = amount;
        this.paidBy = paidBy;
        this.involvedParticipants = involvedParticipants;
        this.date = date;
    }

    public Expense() {
        // for object mapper
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Participant getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(Participant paidBy) {
        this.paidBy = paidBy;
    }

    public List<Participant> getInvolvedParticipants() {
        return involvedParticipants;
    }

    public void setInvolvedParticipants(List<Participant> involvedParticipants) {
        this.involvedParticipants = involvedParticipants;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

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
                Objects.equals(date, expense.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, amount, paidBy, involvedParticipants, date);
    }
}

