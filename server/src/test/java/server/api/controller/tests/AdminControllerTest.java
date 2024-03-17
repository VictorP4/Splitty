package server.api.controller.tests;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import server.api.controllers.AdminController;
import server.api.services.AdminService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    AdminService adminService;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpSession session;

    @Mock
    RedirectAttributes redirectAttributes;

    @InjectMocks
    AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        when(request.getSession()).thenReturn(session);
    }

    /**
     * Tests the badLoginResponse() method of AdminController.
     * Verifies that it returns the correct response entity with an incorrect password.
     */
    @Test
    void testBadLoginResponse() {
        ResponseEntity<String> responseEntity = adminController.badLoginResponse();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Incorrect password", responseEntity.getBody());
    }

    /**
     * Tests the login() method of AdminController with a valid password.
     * Verifies that it redirects to the correct URL and sets the session attribute on successful login.
     */
    @Test
    void testSuccessfulLogin() {
        String password = "validPassword";
        when(adminService.adminLogin(password)).thenReturn(true);

        ResponseEntity<?> responseEntity = adminController.login(password, request, redirectAttributes);

        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
        assertEquals("/api/events", responseEntity.getHeaders().getLocation().toString());
        verify(session).setAttribute("adminLogged", true);
        verify(redirectAttributes).addFlashAttribute("Login successful!");
    }

    /**
     * Tests the login() method of AdminController with an invalid password.
     * Verifies that it redirects to the correct URL and sets the appropriate flash attribute on invalid login.
     */
    @Test
    void testInvalidLogin() {
        String password = "invalidPassword";
        when(adminService.adminLogin(password)).thenReturn(false);

        ResponseEntity<?> responseEntity = adminController.login(password, request, redirectAttributes);

        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
        assertEquals("/api/admin/login", responseEntity.getHeaders().getLocation().toString());
        verify(session, never()).setAttribute(anyString(), any());
        verify(redirectAttributes).addFlashAttribute("error", "Invalid credentials");
    }

}
