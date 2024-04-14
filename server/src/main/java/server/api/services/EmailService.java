package server.api.services;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



/**
 * Service class for sending emails.
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    private String serverURL = "http://localhost:8080/StartScreen/";

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    /**
     * Sends an email to the specified recipient.
     *
     * @param to      Email address of the recipient.
     * @param subject Subject of the email.
     * @param content Content of the email.
     * @throws MessagingException If an error occurs during the creation or sending of the email message.
     */
    public void sendEmail(String to, String subject, String content) throws MessagingException {
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


    public void sendAdminPass(String to, String subject, String content) throws MessagingException {
        javaMailSender.setUsername("ooppteam58@gmail.com");
        javaMailSender.setPassword("npxruthvatcivuqz");
        sendEmail(to,subject,content);
    }



}
