package commons;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String bankAccount;
    @ManyToMany(cascade = CascadeType.PERSIST,
        mappedBy = "participants")

    private List<Event> eventsFollowed;

    /**
     * Constructs a Participant with the specified name and email.
     *
     * @param name  the name of the participant
     * @param email the email address of the participant
     */
    public Participant(String name, String email) {
        this.name = name;
        this.email = email;
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
     * Retrieves the bank account information of the participant.
     *
     * @return the bank account information of the participant
     */
    public String getBankAccount() {
        return bankAccount;
    }

    /**
     * Sets the bank account information of the participant.
     *
     * @param bankAccount the bank account information to be set
     */
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * Retrieves the events followed by the participant.
     *
     * @return the events followed by the participant
     */
    public List<Event> getEventsFollowed() {
        return eventsFollowed;
    }

    /**
     * Sets the events followed by the participant.
     *
     * @param eventsFollowed the events followed by the participant
     */
    public void setEventsFollowed(List<Event> eventsFollowed) {
        this.eventsFollowed = eventsFollowed;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param o the reference object with which to compare
     * @return true if this object is the same as the obj argument; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(bankAccount, that.bankAccount) && Objects.equals(eventsFollowed, that.eventsFollowed);
    }

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, bankAccount, eventsFollowed);
    }

    /**
     * Returns a string representation of the object.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "Participant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", bankAccount='" + bankAccount + '\'' +
                ", eventsFollowed=" + eventsFollowed +
                '}';
    }
}
