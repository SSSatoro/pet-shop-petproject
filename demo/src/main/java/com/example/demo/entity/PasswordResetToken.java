package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expiryDate;
    public PasswordResetToken() {}

    public PasswordResetToken(User user) {
        this.user = user;
        this.expiryDate = LocalDateTime.now().plusMinutes(60);
        this.token = UUID.randomUUID().toString();
    }


    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
