package com.example.demo.controllers;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    private final UserRepository userRepository;


    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = auth.getName();


        User user = userRepository.findByUsername(currentPrincipalName)
                // Если вдруг не нашли по username, можно попробовать поискать по email (опционально)
                .or(() -> userRepository.findByEmail(currentPrincipalName))
                .orElseThrow(() -> new RuntimeException("Пользователь не найден в базе"));


        model.addAttribute("user", user);

        return "profile";
    }
}
