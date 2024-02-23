package commons;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Date;
import java.util.Objects;

public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int amount;
    private Participant personOwed;
    private Participant personInDebt;
    private boolean paidOff;
    private Date dateCreation;
    private Expense debtFor;

    /**
     * constructs a participants debt related to a specific expense
     * @param amount amount owed in debt
     * @param personOwed the person who needs to be paid to
     * @param personInDebt person who needs to pay the debt
     * @param paidOff states if the debts been paid
     * @param dateCreation date when the debt was created
     * @param debtFor which expense is the debt for
     */
    public Debt(int amount, Participant personOwed, Participant personInDebt,
                boolean paidOff, Date dateCreation, Expense debtFor) {
        this.amount = amount;
        this.personOwed = personOwed;
        this.personInDebt = personInDebt;
        this.paidOff = paidOff;
        this.dateCreation = dateCreation;
        this.debtFor = debtFor;
    }

    /**
     * Default constructor for the Expense class for object mapper.
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
    public int getAmount() {
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
    public boolean isPaidOff() {
        return paidOff;
    }

    /**
     * retrieves the fate the debt was created
     * @return date of creation
     */
    public Date getDateCreation() {
        return dateCreation;
    }

    /**
     * retrieves the expense the debt is related to
     * @return the expense
     */
    public Expense getDebtFor() {
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
    public void setAmount(int amount) {
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
     * @param paidOff boolean value to set
     */
    public void setPaidOff(boolean paidOff) {
        this.paidOff = paidOff;
    }

    /**
     * sets the date when the debt was created
     * @param dateCreation date to set
     */
    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    /**
     * sets the expanse that the debt is for
     * @param debtFor expense to set
     */
    public void setDebtFor(Expense debtFor) {
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
        return amount == debt.amount && paidOff == debt.paidOff && Objects.equals(id, debt.id)
                && Objects.equals(personOwed, debt.personOwed) && Objects.equals(personInDebt, debt.personInDebt)
                && Objects.equals(dateCreation, debt.dateCreation) && Objects.equals(debtFor, debt.debtFor);
    }

    /**
     * generates a hashcode for the debt
     * @return the hashcode for the debt
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, amount, personOwed, personInDebt, paidOff, dateCreation, debtFor);
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
                ", payedOf: " + paidOff +
                ", debt created on: " + dateCreation +
                ", debt for=" + debtFor +
                '}';
    }
}
