package commons;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double amount;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Participant personOwed;
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Participant personInDebt;
    private boolean settled;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Event debtFor;

    /**
     * constructs a participants debt related to a specific expense
     * @param amount amount owed in debt
     * @param personOwed the person who needs to be paid to
     * @param personInDebt person who needs to pay the debt
     * @param debtFor which expense is the debt for
     */
    public Debt(double amount, Participant personOwed, Participant personInDebt,
                Event debtFor) {
        this.amount = amount;
        this.personOwed = personOwed;
        this.personInDebt = personInDebt;
        this.settled = false;
        this.debtFor = debtFor;
    }

    /**
     * Default constructor for the Event class for object mapper.
     */
    public Debt() {
        //for Object Mapper
    }

    /**
     * retrieves the id of the debt
     * @return id of the debt
     */
    public Long getId() {
        return id;
    }

    /**
     * retrieves the amount of debt
     * @return amount of debt
     */
    public double getAmount() {
        return amount;
    }

    /**
     * retrieves the participant that is owed the money
     * @return the participant
     */
    public Participant getPersonOwed() {
        return personOwed;
    }

    /**
     * retrieves the participant in debt
     * @return participant in debt
     */
    public Participant getPersonInDebt() {
        return personInDebt;
    }

    /**
     * retirieves the boolean value is the debt paid of
     * @return boolean isPaidOf
     */
    public boolean isSettled() {
        return settled;
    }

    /**
     * retrieves the expense the debt is related to
     * @return the expense
     */
    public Event getDebtFor() {
        return debtFor;
    }

    /**
     * sets the id of the debt
     * @param id the id of the debt to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * sets the amount of the debt
     * @param amount the amount of the debt to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * setes the person owed the debt
     * @param personOwed person owed to set
     */
    public void setPersonOwed(Participant personOwed) {
        this.personOwed = personOwed;
    }

    /**
     * sets the person that is in debt
     * @param personInDebt person in debt to set
     */
    public void setPersonInDebt(Participant personInDebt) {
        this.personInDebt = personInDebt;
    }

    /**
     * sets boolean was the debt paid off
     * @param settled boolean value to set
     */
    public void setSettled(boolean settled) {
        this.settled = settled;
    }

    /**
     * sets the expanse that the debt is for
     * @param debtFor expense to set
     */
    public void setDebtFor(Event debtFor) {
        this.debtFor = debtFor;
    }

    /**
     * checks if two debts are equal
     * @param other the object to compare to
     * @return boolean are the objects equal
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Debt debt = (Debt) other;
        return amount == debt.amount && settled == debt.settled && Objects.equals(id, debt.id)
                && Objects.equals(personOwed, debt.personOwed) && Objects.equals(personInDebt, debt.personInDebt)
                && Objects.equals(debtFor, debt.debtFor);
    }

    /**
     * generates a hashcode for the debt
     * @return the hashcode for the debt
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, amount, personOwed, personInDebt, settled, debtFor);
    }

    /**
     * creates a string version of the debt
     * @return the string version of the debt
     */
    @Override
    public String toString() {
        return "Debt{" +
                "id: " + id +
                ", amount owned:" + amount +
                ", to " + personOwed +
                ", by" + personInDebt +
                ", payedOf: " + settled +
                ", debt for=" + debtFor +
                '}';
    }
}
