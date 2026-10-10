package com.quicklease.quicklease_backend.service;

import com.quicklease.quicklease_backend.dto.RegisterRequest;
import com.quicklease.quicklease_backend.dto.LoginRequest;
import com.quicklease.quicklease_backend.dto.UserResponse;
import com.quicklease.quicklease_backend.dto.LoginResponse;
import com.quicklease.quicklease_backend.util.JwtUtil;
import com.quicklease.quicklease_backend.model.User;
import com.quicklease.quicklease_backend.repository.UserRepository;
import com.quicklease.quicklease_backend.dto.ProfileRequest;
import com.quicklease.quicklease_backend.dto.ProfileResponse;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
private EmailService emailService;

@Value("${app.frontend-url}")
private String frontendUrl;

    public UserResponse registerUser(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        User user = new User(
                request.getEmail(),
                hashedPassword,
                request.getFullName()
        );

        user.setVerificationToken(UUID.randomUUID().toString());

        User savedUser = userRepository.save(user);

        String verificationLink = frontendUrl + "/verify-email?token=" + savedUser.getVerificationToken();
    try {
        emailService.sendVerificationEmail(savedUser.getEmail(), verificationLink); 
    } catch (Exception e) {
        System.err.println("Could not send verification email: " + e.getMessage());
    }
        return new UserResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getFullName());
    }

    public LoginResponse loginUser(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return new LoginResponse(token, user.getId(), user.getEmail(), user.getFullName());
    }

    public String verifyEmail(String token) {
        User user = userRepository.findByVerificationToken(token)
        .orElseThrow(() -> new IllegalArgumentException("Invalid verification token"));

        user.setEmailVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);

        return "Email verified successfully";
    }
    public ProfileResponse getProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return mapToProfile(user);
    }
    
    public ProfileResponse updateProfile(String email, ProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    
        if (request.getPhone() == null || !request.getPhone().matches("^[6-9]\\d{9}$")) {
            throw new IllegalArgumentException("Enter a valid 10-digit Indian mobile number");
        }
        if (request.getCity() == null || request.getCity().isBlank()) {
            throw new IllegalArgumentException("City is required");
        }
    
        user.setPhone(request.getPhone());
        user.setCity(request.getCity().trim());
        user.setAddress(request.getAddress() == null ? null : request.getAddress().trim());
        userRepository.save(user);
    
        return mapToProfile(user);
    }
    
    private ProfileResponse mapToProfile(User user) {
        return new ProfileResponse(user.getFullName(), user.getEmail(), user.getEmailVerified(),
                user.getPhone(), user.getAddress(), user.getCity());
    }
}