package server.api.controller.tests;

import commons.EmailRequestBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.api.controllers.EmailController;
import server.api.services.EmailService;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class EmailControllerTests {
    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    @Test
    public void testSendInvitesSuccess() {
        when(emailService.sendInvites(anyList(), anyString())).thenReturn(HttpStatus.OK);
        EmailRequestBody request = new EmailRequestBody(Arrays.asList("email1@example.com", "email2@example.com"), "invite_code");
        ResponseEntity<?> responseEntity = emailController.sendInvites(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testSendInvitesFailure() {
        when(emailService.sendInvites(anyList(), anyString())).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        EmailRequestBody request = new EmailRequestBody(Arrays.asList("email1@example.com", "email2@example.com"), "invite_code");
        ResponseEntity<?> responseEntity = emailController.sendInvites(request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testSendReminderSuccess() {
        when(emailService.sendReminder(any(EmailRequestBody.class))).thenReturn(HttpStatus.OK);
        EmailRequestBody request = new EmailRequestBody(List.of("email@example.com"), "reminder_info");
        ResponseEntity<?> responseEntity = emailController.sendReminder(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testSendReminderFailure() {
        when(emailService.sendReminder(any(EmailRequestBody.class))).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        EmailRequestBody request = new EmailRequestBody(List.of("email@example.com"), "reminder_info");
        ResponseEntity<?> responseEntity = emailController.sendReminder(request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testSendReminderInvalidRequestBody() {
        when(emailService.sendReminder(null)).thenReturn(HttpStatus.BAD_REQUEST);
        ResponseEntity<?> responseEntity = emailController.sendReminder(null);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testSendReminderInvalidEmailAddresses() {
        EmailRequestBody request = new EmailRequestBody(List.of("invalid_email"), "reminder_info");
        when(emailService.sendReminder(any(EmailRequestBody.class))).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<?> responseEntity = emailController.sendReminder(request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testSendReminderNullEmailAddresses() {
        EmailRequestBody request = new EmailRequestBody(null, "reminder_info");
        when(emailService.sendReminder(any(EmailRequestBody.class))).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<?> responseEntity = emailController.sendReminder(request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void testSendReminderEmptyEmailAddresses() {
        EmailRequestBody request = new EmailRequestBody(List.of(), "reminder_info");
        when(emailService.sendReminder(any(EmailRequestBody.class))).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<?> responseEntity = emailController.sendReminder(request);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

}
