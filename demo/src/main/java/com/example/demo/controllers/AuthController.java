package com.example.demo.controllers;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.service.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/api/auth")
public class AuthController {


    private final RegistrationService registrationService;

    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public Object register(@Valid @ModelAttribute RegisterRequest request) {
        try {
            User newUser = new User();
            newUser.setUsername(request.getUsername());
            newUser.setEmail(request.getEmail());
            newUser.setPassword(request.getPassword());
            newUser.setFirstName(request.getFirstName());
            newUser.setLastName(request.getLastName());

            registrationService.register(newUser);


            return new RedirectView("/registration-success", true);

        } catch (IllegalStateException ex) {
            System.err.println("Registration error: " + ex.getMessage());
            return new RedirectView("/register?error=" + ex.getMessage(), true);

        } catch (Exception ex) {
            System.err.println("Server error during registration: " + ex.getMessage());
            ex.printStackTrace();
            return new RedirectView("/register?error=ServerError", true);
        }
    }
}
