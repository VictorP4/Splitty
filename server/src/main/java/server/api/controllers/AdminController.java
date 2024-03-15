package server.api.controllers;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import server.api.services.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String password, HttpServletRequest request,
                                   RedirectAttributes redirectAttributes){
        boolean loginAttempt = adminService.adminLogin(password);
        if(loginAttempt){
            request.getSession().setAttribute("adminLogged", true);
            redirectAttributes.addFlashAttribute("Login successful!");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location","/api/events");
            return new ResponseEntity(headers, HttpStatus.FOUND);
        }
        else {
            redirectAttributes.addFlashAttribute("error", "Invalid credentials");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location","/api/participants");
            return new ResponseEntity(headers, HttpStatus.FOUND);
        }
    }


}
