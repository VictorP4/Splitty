package client.services;

import client.utils.EmailUtils;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InvitationServiceTest {

    @Test
    void getEmailsWithValidLinesReturnsNonEmptyArrayList() {
        InvitationService service = new InvitationService();
        String[] lines = {"test1@example.com", "test2@example.com"};

        ArrayList<String> emails = service.getEmails(lines);

        assertEquals(2, emails.size());
        assertEquals("test1@example.com", emails.get(0));
        assertEquals("test2@example.com", emails.get(1));
    }

    @Test
    void getEmailsWithEmptyLinesReturnsEmptyArrayList() {
        InvitationService service = new InvitationService();
        String[] lines = {"", "  ", "\t"};

        ArrayList<String> emails = service.getEmails(lines);

        assertTrue(emails.isEmpty());
    }


    @Test
    void sendInvitesWithNoValidEmailsDoesNotCallEmailUtilsSendInvites() {
        // Arrange
        InvitationService service = new InvitationService();
        String[] lines = {"", "  ", "\t"};
        String inviteCode = "12345";
        String email = "sender@example.com";
        String userPass = "password";
        String urlConfig = "http://example.com";
        EmailUtils emailUtilsMock = mock(EmailUtils.class);

        service.sendInvites(lines, inviteCode, email, userPass, urlConfig);

        verify(emailUtilsMock, never()).sendInvites(any(), any(), any(), any(), any());
    }



    @Test
    void sendInvitesWithEmptyLinesDoesNotCallEmailUtilsSendInvites() {
        // Arrange
        InvitationService service = new InvitationService();
        String[] lines = {"", "  ", "\t"};
        String inviteCode = "12345";
        String email = "sender@example.com";
        String userPass = "password";
        String urlConfig = "http://example.com";
        EmailUtils emailUtilsMock = mock(EmailUtils.class);

        service.sendInvites(lines, inviteCode, email, userPass, urlConfig);

        verify(emailUtilsMock, never()).sendInvites(any(), any(), any(), any(), any());
    }
}

