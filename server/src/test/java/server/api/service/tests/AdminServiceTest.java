package server.api.service.tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.mail.MessagingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import server.api.services.AdminService;
import server.api.services.EmailService;
import java.lang.reflect.Field;

public class AdminServiceTest {

    @Mock
    private EmailService emailServiceMock;

    @Mock
    private Logger loggerMock;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.initMocks(this);
        adminService = new AdminService();
        setPrivateField(adminService, "serv", emailServiceMock);
    }

    /**
     * Tests the onApplicationEvent method.
     * Verifies that the email service is called with the correct arguments.
     *
     * @throws MessagingException if an error occurs while sending the email
     */
    @Test
    public void testOnApplicationEvent() throws MessagingException {
        String expectedSubject = "Session Password";
        ArgumentCaptor<String> subjectCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);

        adminService.onApplicationEvent(mock(ApplicationStartedEvent.class));

        verify(emailServiceMock).sendEmail(eq("splittyadmin@protonmail.com"), subjectCaptor.capture(), bodyCaptor.capture());
        assertEquals(expectedSubject, subjectCaptor.getValue());
        assertTrue(bodyCaptor.getValue().contains("Your admin password for this session is: "));
    }

    /**
     * Tests the generateSessionPass method.
     * Verifies that a session password is generated with the correct length.
     *
     * @throws NoSuchFieldException   if a field with the specified name is not found
     * @throws IllegalAccessException if the field is inaccessible
     */
    @Test
    public void testGenerateSessionPass() throws NoSuchFieldException, IllegalAccessException {
        Field sessionPassField = AdminService.class.getDeclaredField("sessionPass");
        sessionPassField.setAccessible(true);

        adminService.generateSessionPass();

        assertNotNull(sessionPassField.get(adminService));
        assertEquals(15, ((String) sessionPassField.get(adminService)).length());
    }

    /**
     * Tests the adminLogin method with a correct password.
     * Verifies that admin can successfully log in.
     *
     * @throws NoSuchFieldException   if a field with the specified name is not found
     * @throws IllegalAccessException if the field is inaccessible
     */
    @Test
    public void testAdminLoginSuccess() throws NoSuchFieldException, IllegalAccessException {
        Field sessionPassField = AdminService.class.getDeclaredField("sessionPass");
        sessionPassField.setAccessible(true);
        sessionPassField.set(adminService, RandomStringUtils.random(15));

        assertTrue(adminService.adminLogin((String) sessionPassField.get(adminService)));
    }

    /**
     * Tests the adminLogin method with an incorrect password.
     * Verifies that admin login fails.
     *
     * @throws NoSuchFieldException   if a field with the specified name is not found
     * @throws IllegalAccessException if the field is inaccessible
     */
    @Test
    public void testAdminLoginFailure() throws NoSuchFieldException, IllegalAccessException {
        Field sessionPassField = AdminService.class.getDeclaredField("sessionPass");
        sessionPassField.setAccessible(true);
        sessionPassField.set(adminService, "correctPassword");

        assertFalse(adminService.adminLogin("incorrectPassword"));
    }

    /**
     * Helper method to set private fields using reflection.
     * @param object the object whose field needs to be set
     * @param fieldName the name of the field to set
     * @param value the value to set for the field
     * @throws NoSuchFieldException if a field is not found
     * @throws IllegalAccessException if field cannot be accessed
     */
    private void setPrivateField(Object object, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
    /**
     * Tests the adminLogin method with a null password.
     * Verifies that admin login fails with a null password.
     *
     * @throws NoSuchFieldException   if a field with the specified name is not found
     * @throws IllegalAccessException if the field is inaccessible
     */
    @Test
    public void testAdminLoginNullPassword() throws NoSuchFieldException, IllegalAccessException {
        Field sessionPassField = AdminService.class.getDeclaredField("sessionPass");
        sessionPassField.setAccessible(true);
        sessionPassField.set(adminService, "correctPassword");

        assertFalse(adminService.adminLogin(null));
    }

    /**
     * Tests the adminLogin method with an empty password.
     * Verifies that admin login fails with an empty password.
     *
     * @throws NoSuchFieldException   if a field with the specified name is not found
     * @throws IllegalAccessException if the field is inaccessible
     */
    @Test
    public void testAdminLoginEmptyPassword() throws NoSuchFieldException, IllegalAccessException {
        Field sessionPassField = AdminService.class.getDeclaredField("sessionPass");
        sessionPassField.setAccessible(true);
        sessionPassField.set(adminService, "correctPassword");

        assertFalse(adminService.adminLogin(""));
    }

    /**
     * Tests the adminLogin method with whitespace password.
     * Verifies that admin login fails with a whitespace password.
     *
     * @throws NoSuchFieldException   if a field with the specified name is not found
     * @throws IllegalAccessException if the field is inaccessible
     */
    @Test
    public void testAdminLoginWhitespacePassword() throws NoSuchFieldException, IllegalAccessException {
        Field sessionPassField = AdminService.class.getDeclaredField("sessionPass");
        sessionPassField.setAccessible(true);
        sessionPassField.set(adminService, "correctPassword");

        assertFalse(adminService.adminLogin("   "));
    }
}
