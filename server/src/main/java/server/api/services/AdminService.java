package server.api.services;

import jakarta.mail.MessagingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class AdminService implements ApplicationListener<ApplicationStartedEvent> {

    private String sessionPass;
    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);
    @Autowired
    private EmailService serv;
    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        generateSessionPass();
        try {
            System.out.println(sessionPass);
            serv.sendEmail("splittyadmin@protonmail.com","Session Password",
                "Your admin password for this session is: "+sessionPass);
        } catch (MessagingException e) {
            logger.error("Admin couldn't receive password.");
        }
    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }

    public void generateSessionPass() {
        String characters =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!";
        String generatedString = RandomStringUtils.random( 15, characters );
        sessionPass=generatedString;
    }

    public boolean adminLogin(String password){
        return sessionPass.equals(password);
    }
}
