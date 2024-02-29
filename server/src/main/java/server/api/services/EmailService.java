package server.api.services;

import commons.EmailRequestBody;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import java.util.List;

/**
 * Service class for sending emails.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    private String serverURL = "http://localhost:8080/StartScreen/";

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);


    /**
     * Sends invitation emails to a list of recipients.
     *
     * @param recipients List of email addresses to send invitations to.
     * @param code       Invitation code to be included in the email.
     * @return HttpStatus indicating the success or failure of the email sending operation.
     */
    public HttpStatus sendInvites(List<String> recipients, String code) {
        try {
            for (String recipient : recipients) {
                sendEmail(recipient, "You have been invited to join a new Splitty event!",
                        "\nServer URL: "+ serverURL + "\nHere's the invite code: " + code);
            }
            return HttpStatus.OK;
        } catch (MailException | MessagingException e) {
            logger.error("Error occurred while sending invites:", e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }


    /**
     * Sends an email to the specified recipient.
     *
     * @param to      Email address of the recipient.
     * @param subject Subject of the email.
     * @param content Content of the email.
     * @throws MessagingException If an error occurs during the creation or sending of the email message.
     */
    private void sendEmail(String to, String subject, String content) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);

            javaMailSender.send(message);
        } catch (MailException | MessagingException e) {
            logger.error("Error occurred while sending email to {}: {}", to, e.getMessage());
            throw e;
        }
    }

    /**
     * Send reminder to the respective person
     *
     * @param emailRequest info for reminder
     * @return http status
     */
    public HttpStatus sendReminder(EmailRequestBody emailRequest) {
        try{
            sendEmail(emailRequest.getEmailAddresses().get(1),"Debt Reminder","Hello "+
                    emailRequest.getEmailAddresses().get(0)+"\n The debt you owe to "+
                    emailRequest.getEmailAddresses().get(2)+" is "+emailRequest.getCode()+" euros\nBank details:\n"+
                    "IBAN: "+emailRequest.getEmailAddresses().get(3)+", BIC: "+emailRequest.getEmailAddresses().get(4));
            return HttpStatus.OK;
        }
        catch (MailException | MessagingException e){
            logger.error("Error occurred while sending reminder:", e);
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }
}
