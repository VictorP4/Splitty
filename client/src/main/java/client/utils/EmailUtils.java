package client.utils;


import com.google.inject.Inject;
import commons.EmailRequestBody;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.MailException;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailUtils {
    private JavaMailSenderImpl javaMailSender;
    private final ExecutorService exec ;
    @Inject
    public EmailUtils() {
        this.javaMailSender = new JavaMailSenderImpl();
        this.exec = Executors.newSingleThreadExecutor();
    }

    /**
     * Sends invitation emails to a list of recipients.
     *
     * @param recipients List of email addresses to send invitations to.
     * @param code       Invitation code to be included in the email.
     * @return HttpStatus indicating the success or failure of the email sending operation.
     */
    public void sendInvites(List<String> recipients, String code, String username, String password, String server) {
        exec.submit(()->{
            try {

                for (String recipient : recipients) {
                    sendEmail(recipient, "You have been invited to join a new Splitty event!",
                            "\nServer URL: "+ server + "\nHere's the invite code: " + code, username, password);
                }

            } catch (MailException | jakarta.mail.MessagingException e) {
                System.out.println("Error while sending invites");
            }
        });
        exec.shutdown();
    }


    /**
     * Sends an email to the specified recipient.
     *
     * @param to      Email address of the recipient.
     * @param subject Subject of the email.
     * @param content Content of the email.
     * @throws MessagingException If an error occurs during the creation or sending of the email message.
     */
    public void sendEmail(String to, String subject, String content, String username, String password) throws jakarta.mail.MessagingException {
        try {
            javaMailSender.setHost("smtp.gmail.com");
            javaMailSender.setPort(587);
            javaMailSender.setUsername(username);
            javaMailSender.setPassword(password);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            Properties props = javaMailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.debug", "true");
            helper.setTo(to);
            helper.setCc(username);
            helper.setSubject(subject);
            helper.setText(content);

            javaMailSender.send(message);
        } catch (MailException | jakarta.mail.MessagingException e) {
            System.out.println("Error while sending emails");

        }
    }

    /**
     * Send reminder to the respective person
     *
     * @param emailRequest info for reminder
     * @return http status
     */
    public void sendReminder(EmailRequestBody emailRequest, String username, String password) {
        exec.submit(()->{
            try{

                sendEmail(emailRequest.getEmailAddresses().get(1),"Debt Reminder","Hello "+
                        emailRequest.getEmailAddresses().get(0)+"\n The debt you owe to "+
                        emailRequest.getEmailAddresses().get(2)+" is "+emailRequest.getCode()+" euros\nBank details:\n"+
                        "IBAN: "+emailRequest.getEmailAddresses().get(3)+", BIC: "+emailRequest.getEmailAddresses().get(4),username,password);

            }
            catch (MailException | jakarta.mail.MessagingException e){
                System.out.println("Error while sending reminder");

            }
        });
        exec.shutdown();

    }

    /**
     * Sends confirmation
     * @param username the email
     * @param password the password for the email
     */
    public void sendConfirmation(String username, String password){
        exec.submit(()->{
            try{
                sendEmail(username,"Email Confirmation","This is the email confirmation for the splitty app",username,password);
            }
            catch (MailException | jakarta.mail.MessagingException e){
                System.out.println("Error while sending the confirmation");

            }
        });
        exec.shutdown();
    }
}
