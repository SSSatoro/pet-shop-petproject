package com.example.demo.controllers;

import com.example.demo.entity.ConfirmationToken;
import com.example.demo.entity.User;
import com.example.demo.repository.ConfirmationTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private ConfirmationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }


    @PostMapping("/register")
    public String registerUser(User user) {
        registrationService.register(user);
        return "redirect:/login?sent";
    }


    @GetMapping("/confirm")
    public String confirmToken(@RequestParam("token") String token, Model model) {
        ConfirmationToken confirmationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("Токен не найден"));

        if (confirmationToken.getConfirmedAt() != null) {
            model.addAttribute("error", "Email уже подтвержден.");
            return "login";
        }


        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            model.addAttribute("error", "Срок действия ссылки истек.");
            return "login";
        }


        confirmationToken.setConfirmedAt(LocalDateTime.now());
        tokenRepository.save(confirmationToken);


        User user = confirmationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);


        return "redirect:/login?confirmed";
    }
}
