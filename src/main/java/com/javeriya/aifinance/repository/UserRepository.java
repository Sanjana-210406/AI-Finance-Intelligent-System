package com.javeriya.aifinance.repository;

import com.javeriya.aifinance.entity.User;
import com.javeriya.aifinance.entity.User.ProfileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email — used during login & registration
    Optional<User> findByEmail(String email);

    // Check if email already exists — used during registration to prevent duplicates
    boolean existsByEmail(String email);

    // Find all users by profile type — useful for admin/testing
    List<User> findByProfileType(ProfileType profileType);
}