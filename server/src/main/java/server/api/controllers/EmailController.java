package server.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import commons.EmailRequestBody;
import server.api.services.EmailService;

import java.util.List;

/**
 * Controller class for handling email-related API endpoints.
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    /**
     * Endpoint for sending invites via email.
     *
     * @param emailRequest Request body containing email addresses and invite code.
     * @return ResponseEntity with HTTP status indicating the success or failure of the email sending operation.
     */
    @PostMapping("/invites")
    public ResponseEntity<?> sendInvites(@RequestBody EmailRequestBody emailRequest) {
        return ResponseEntity.status(emailService.sendInvites(
                emailRequest.getEmailAddresses(), emailRequest.getCode())).build();
    }

    @PostMapping("/submission")
    public ResponseEntity<?> submit(@RequestBody List<String> details) {
        return ResponseEntity.status(emailService.submitDetails(details.get(0), details.get(1))).build();
    }
    /**
     * Endpoint for sending the reminder via email
     *
     * @param emailRequest Request body containing the information for the reminder
     * @return HTTP status indicating failure or success
     */
    @PostMapping("/reminders")
    public ResponseEntity<?> sendReminder(@RequestBody EmailRequestBody emailRequest) {
        return ResponseEntity.status(emailService.sendReminder(emailRequest)).build();
    }

}
