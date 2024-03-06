package server.api.controllers;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import server.api.services.AdminService;

import java.util.Map;

@RestController
@RequestMapping("api/admin")
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
            return ResponseEntity.status(200).body("redirect:/api/events");
        }
        else {
            redirectAttributes.addFlashAttribute("error", "Invalid credentials");
            return ResponseEntity.status(302).body("redirect:api/admin/login");
        }
    }


}
