package com.example.demo.service;

import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest req){
        if(userRepository.existsByUsername(req.getUsername())){
            throw new IllegalArgumentException("Username is already exists");
        }
        if(userRepository.existsByEmail(req.getEmail())){
            throw new IllegalArgumentException("Email is already exists");
        }

        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        userRepository.saveAndFlush(user);

        long count = userRepository.count();
        System.out.println(">>> [ДЕТЕКТОР] Пользователей в базе сейчас: " + count);

        userRepository.findAll().forEach(u ->
                System.out.println(">>> [ДЕТЕКТОР] Нашел юзера: " + u.getUsername())
        );
    }

}
