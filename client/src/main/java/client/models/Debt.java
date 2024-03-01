package client.models;

import commons.Participant;

import java.util.Objects;

public class Debt {
    private double amount;
    private Participant personOwed;
    private Participant personInDebt;

    /**
     * Constructor for debt
     * @param amount
     * @param personOwed
     * @param personInDebt
     */
    public Debt(double amount, Participant personOwed, Participant personInDebt) {
        this.amount = amount;
        this.personOwed = personOwed;
        this.personInDebt = personInDebt;
    }

    /**
     * Returns the debt amount
     *
     * @return amount
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
     * sets the amount of the debt
     * @param amount the amount of the debt to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    /**
     * sets the person owed the debt
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
     * checks if two debts are equal
     * @param o the object to compare to
     * @return boolean are the objects equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Debt debt = (Debt) o;
        return Double.compare(debt.amount, amount) == 0 && Objects.equals(personOwed, debt.personOwed) && Objects.equals(personInDebt, debt.personInDebt);
    }
    /**
     * generates a hashcode for the debt
     * @return the hashcode for the debt
     */
    @Override
    public int hashCode() {
        return Objects.hash(amount, personOwed, personInDebt);
    }
}
