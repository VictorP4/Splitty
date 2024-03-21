package commons;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private double debt;
    private String iban;
    private String bic;

    /**
     * Constructs a Participant with the specified name and email.
     *
     * @param name  the name of the participant
     * @param email the email address of the participant
     */
    public Participant(String name, String email) {
        this.name = name;
        this.email = email;
        this.debt=0.00;
    }

    /**
     * Default constructor for the Participant class for object mapper.
     */
    public Participant() {
        // for object mapper
    }

    /**
     * Retrieves the ID of the participant.
     *
     * @return the ID of the participant
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the participant.
     *
     * @param id the ID to be set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the participant.
     *
     * @return the name of the participant
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the participant.
     *
     * @param name the name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the email address of the participant.
     *
     * @return the email address of the participant
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the participant.
     *
     * @param email the email address to be set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the open debt
     *
     * @return debt of person
     */
    public double getDebt() {
        return debt;
    }

    /**
     * Sets the debt
     *
     * @param debt the new debt
     */
    public void setDebt(double debt) {
        this.debt = debt;
    }

    /**
     * Retrieves the IBAN
     *
     * @return the IBAN
     */
    public String getIban() {
        return iban;
    }

    /**
     * Sets the IBAN
     *
     * @param iban the new IBAN
     */
    public void setIBAN(String iban) {
        this.iban = iban;
    }

    /**
     * Retrieves the BIC
     *
     * @return BIC
     */
    public String getBic() {
        return bic;
    }

    /**
     * Sets the BIC
     *
     * @param bic the new BIC
     */
    public void setBIC(String bic) {
        this.bic = bic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Double.compare(that.debt, debt) == 0 && Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) && Objects.equals(email, that.email) &&
                Objects.equals(iban, that.iban) && Objects.equals(bic, that.bic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, debt, iban, bic);
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return  name ;
    }
}
