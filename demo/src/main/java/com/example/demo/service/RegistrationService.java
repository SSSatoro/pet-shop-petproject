package com.example.demo.service;

import com.example.demo.entity.ConfirmationToken;
import com.example.demo.entity.User;
import com.example.demo.repository.ConfirmationTokenRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmationTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSenderService emailSender;

    @Transactional
    public void register(User user) {
        try {
            System.out.println("=== [START] Регистрация для: " + user.getEmail());


            if (userRepository.existsByEmail(user.getEmail())) {
                System.out.println("=== [ERROR] Email занят");
                throw new IllegalStateException("Email уже занят");
            }


            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setEnabled(false);
            userRepository.save(user);
            System.out.println("=== [STEP 1] Юзер сохранен (enabled=false)");


            String token = UUID.randomUUID().toString();
            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    user
            );

            tokenRepository.save(confirmationToken);
            System.out.println("Токен сохранен в базе: " + token);


            String link = "http://localhost:8080/confirm?token=" + token;
            System.out.println("Ссылка создана");

            emailSender.send(user.getEmail(), buildEmail(user.getUsername(), link));

            System.out.println("Команда  передана");

        } catch (Exception e) {
            System.out.println("Произошел сбой: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private String buildEmail(String name, String link) {
        return "Привет, " + name + "! \n\n" +
                "Для подтверждения регистрации перейдите по ссылке:\n" + link;
    }
}
