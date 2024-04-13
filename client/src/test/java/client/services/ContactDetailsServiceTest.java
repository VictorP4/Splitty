package client.services;

import commons.Participant;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContactDetailsServiceTest {

    @Test
    void saveParticipantWithValidDataReturnsParticipant() {
        ContactDetailsService service = new ContactDetailsService();
        Participant participant = new Participant();
        String name = "John Doe";
        String email = "john@example.com";
        String bic = "ABCD1234";
        String iban = "DE89370400440532013000";

        Participant savedParticipant = service.saveParticipant(participant, name, email, bic, iban);

        assertEquals(name, savedParticipant.getName());
        assertEquals(email, savedParticipant.getEmail());
        assertEquals(bic, savedParticipant.getBic());
        assertEquals(iban, savedParticipant.getIban());
    }


    @Test
    void saveParticipantWithValidDataReturnsSameParticipantInstance() {
        // Arrange
        ContactDetailsService service = new ContactDetailsService();
        Participant participant = new Participant();
        String name = "John Doe";
        String email = "john@example.com";
        String bic = "ABCD1234";
        String iban = "DE89370400440532013000";

        Participant savedParticipant = service.saveParticipant(participant, name, email, bic, iban);

        assertSame(participant, savedParticipant);
    }
}

