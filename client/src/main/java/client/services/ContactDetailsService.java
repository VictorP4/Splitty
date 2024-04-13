package client.services;

import commons.Participant;

import javax.inject.Inject;

public class ContactDetailsService {
    @Inject
    public ContactDetailsService() {}

    public Participant saveParticipant(Participant participant, String name, String email, String bic, String iban){
        participant.setName(name);
        participant.setEmail(email);
        participant.setBIC(bic);
        participant.setIBAN(iban);
        return participant;
    }
}
