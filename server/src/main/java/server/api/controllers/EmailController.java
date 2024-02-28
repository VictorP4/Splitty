package server.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import commons.EmailRequestBody;
import server.api.services.EmailService;

/**
 * Controller class for handling email-related API endpoints.
 */
@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    /**
     * Endpoint for sending invites via email.
     *
     * @param emailRequest Request body containing email addresses and invite code.
     * @return ResponseEntity with HTTP status indicating the success or failure of the email sending operation.
     */
    @PostMapping("/sendInvites")
    public ResponseEntity<?> sendInvites(@RequestBody EmailRequestBody emailRequest) {
        return ResponseEntity.status(emailService.sendInvites(
                emailRequest.getEmailAddresses(), emailRequest.getCode())).build();
    }

}
