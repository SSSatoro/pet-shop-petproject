package com.example.demo.controllers;

import com.example.demo.entity.PasswordResetToken;
import com.example.demo.entity.User;
import com.example.demo.repository.TokenRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class PasswordController {

    @Autowired private UserRepository userRepository;
    @Autowired private TokenRepository tokenRepository;
    @Autowired private JavaMailSender mailSender;
    @Autowired private PasswordEncoder passwordEncoder;


    @PostMapping("/profile/request-password-change")
    public String requestPasswordChange(Principal principal, Model model) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username)
                .or(() -> userRepository.findByEmail(username)).orElseThrow();


        PasswordResetToken token = new PasswordResetToken(user);
        tokenRepository.save(token);


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Смена пароля");
        message.setText("Для смены пароля по ссылке: " +
                "http://localhost:8080/change-password?token=" + token.getToken());

        mailSender.send(message);

        return "redirect:/profile?message=EmailSent";
    }


    @GetMapping("/change-password")
    public String showChangePasswordPage(@RequestParam("token") String token, Model model) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        if (resetToken == null || resetToken.isExpired()) {
            model.addAttribute("error", "Ссылка недействительна");
            return "error-page";
        }

        model.addAttribute("token", token);
        return "change_password_form";
    }

    @PostMapping("/save-password")
    public String saveNewPassword(@RequestParam("token") String token,
                                  @RequestParam("password") String newPassword) {

        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        // Тут тоже стоит проверить валидность токена
        if (resetToken == null || resetToken.isExpired()) return "redirect:/login?error";

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Удаляем использованный токен
        tokenRepository.delete(resetToken);

        return "redirect:/login?message=PasswordChanged";
    }
}
