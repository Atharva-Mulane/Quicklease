package com.quicklease.quicklease_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail-from:}")
    private String from;

    public void sendVerificationEmail(String toEmail, String verificationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        if (from != null && !from.isBlank()) {
            message.setFrom(from);
        }
        message.setTo(toEmail);
        message.setSubject("Verify your QuickLease account");
        message.setText("Welcome to QuickLease!\n\nPlease verify your email by clicking the link below:\n\n"
                + verificationLink + "\n\nIf you didn't sign up, ignore this email.");
        mailSender.send(message);
    }
}
