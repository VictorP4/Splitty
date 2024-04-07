package server.api.controllers;


import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import server.api.services.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    /**
     * A get to the page is always wrong, it should only take posts
     * @return a bad request
     */
    @GetMapping("/login")
    public ResponseEntity<String> badLoginResponse() {
        return new ResponseEntity<>("Incorrect password", HttpStatus.OK);
    }

    /**
     * MAkes a login request and redirects you appropriately
     * @param password of the admin
     * @param request a servlet request
     * @param redirectAttributes redirect to the event page
     * @return a response
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String password, HttpServletRequest request,
                                   RedirectAttributes redirectAttributes){
        logger.info("Received a login request with password: {}", password);
        boolean loginAttempt = adminService.adminLogin(password);

        System.out.println("Password was correct?    " + loginAttempt);

        if(loginAttempt){
            request.getSession().setAttribute("adminLogged", true);
            redirectAttributes.addFlashAttribute("Login successful!");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location","/api/events");
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        else {
            redirectAttributes.addFlashAttribute("error", "Invalid credentials");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location","/api/admin/login");
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
    }
}
