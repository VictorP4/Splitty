package server.api.services;

import jakarta.mail.MessagingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Objects;
import java.util.Random;

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
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:'\",<.>/?";
        String generatedString = RandomStringUtils.random( 15, characters );
        sessionPass=generatedString;
    }

    public boolean adminLogin(String password){
        if(sessionPass.equals(password)) return true;
        return false;
    }
}
