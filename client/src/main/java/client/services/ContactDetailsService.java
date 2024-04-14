package client.services;

import commons.Participant;

public class ContactDetailsService {

    /**
     * Constructs a new ContactDetailsService
     */
    public ContactDetailsService() {}

    /**
     * Saves a participant
     *
     * @param participant The participant in question
     * @param name the name of the participant
     * @param email the email of the participant
     * @param bic the bic of the participant
     * @param iban the iban of the participant
     * @return the newly created or updated participant
     */
    public Participant saveParticipant(Participant participant, String name, String email, String bic, String iban){
        participant.setName(name);
        participant.setEmail(email);
        participant.setBIC(bic);
        participant.setIBAN(iban);
        return participant;
    }
}
