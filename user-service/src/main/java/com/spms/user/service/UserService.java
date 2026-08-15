package com.spms.user.service;

import com.spms.user.dto.UserDTOs.*;
import com.spms.user.entity.User;
import com.spms.user.exception.EmailAlreadyExistsException;
import com.spms.user.exception.InvalidCredentialsException;
import com.spms.user.exception.UserNotFoundException;
import com.spms.user.repository.UserRepository;
import com.spms.user.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserProfile registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email is already in use: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(hashPassword(request.getPassword()));
        user.setRole(request.getRole().toUpperCase());

        User savedUser = userRepository.save(user);
        return new UserProfile(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getRole());
    }

    public AuthResponse loginUser(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        String hashedInputPassword = hashPassword(request.getPassword());
        if (!user.getPassword().equals(hashedInputPassword)) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = JwtUtil.generateToken(user.getId(), user.getEmail(), user.getRole());
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getRole());
    }

    public UserProfile getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return new UserProfile(user.getId(), user.getName(), user.getEmail(), user.getRole());
    }

    public boolean checkUserExists(Long id) {
        return userRepository.existsById(id);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
