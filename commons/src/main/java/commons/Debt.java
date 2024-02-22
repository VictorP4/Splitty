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
    private Participant personOwned;
    private Participant personInDebt;
    private boolean payedOf;
    private Date dateCreation;
    private Expense debtFor;

    /**
     * constructs a participants debt related to a specific expense
     * @param amount amount owed in debt
     * @param personOwned the person who needs to be paid to
     * @param personInDebt person who needs to pay the debt
     * @param payedOf states if the debts been paid
     * @param dateCreation date when the debt was created
     * @param debtFor which expense is the debt for
     */
    public Debt(int amount, Participant personOwned, Participant personInDebt,
                boolean payedOf, Date dateCreation, Expense debtFor) {
        this.amount = amount;
        this.personOwned = personOwned;
        this.personInDebt = personInDebt;
        this.payedOf = payedOf;
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

    public Participant getPersonOwned() {
        return personOwned;
    }

    public Participant getPersonInDebt() {
        return personInDebt;
    }

    public boolean isPayedOf() {
        return payedOf;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public Expense getDebtFor() {
        return debtFor;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPersonOwned(Participant personOwned) {
        this.personOwned = personOwned;
    }

    public void setPersonInDebt(Participant personInDebt) {
        this.personInDebt = personInDebt;
    }

    public void setPayedOf(boolean payedOf) {
        this.payedOf = payedOf;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setDebtFor(Expense debtFor) {
        this.debtFor = debtFor;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Debt debt = (Debt) other;
        return amount == debt.amount && payedOf == debt.payedOf && Objects.equals(id, debt.id)
                && Objects.equals(personOwned, debt.personOwned) && Objects.equals(personInDebt, debt.personInDebt)
                && Objects.equals(dateCreation, debt.dateCreation) && Objects.equals(debtFor, debt.debtFor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, personOwned, personInDebt, payedOf, dateCreation, debtFor);
    }

    @Override
    public String toString() {
        return "Debt{" +
                "id: " + id +
                ", amount owned:" + amount +
                ", to " + personOwned +
                ", by" + personInDebt +
                ", payedOf: " + payedOf +
                ", debt created on: " + dateCreation +
                ", debt for=" + debtFor +
                '}';
    }// 73 + 19
}
