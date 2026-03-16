package com.javeriya.aifinance.service;

import com.javeriya.aifinance.entity.User;
import com.javeriya.aifinance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register a new user
    public User registerUser(User user) {

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered: " + user.getEmail());
        }

        // Set registration timestamp
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    // Find user by email (used during login)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Find user by ID
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Get all users (useful for testing)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update user profile
    public User updateUser(Long id, User updatedUser) {
        User existing = findById(id);
        existing.setName(updatedUser.getName());
        existing.setProfileType(updatedUser.getProfileType());
        return userRepository.save(existing);
    }
}