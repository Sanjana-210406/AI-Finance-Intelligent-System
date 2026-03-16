package com.javeriya.aifinance;

import com.javeriya.aifinance.entity.User;
import com.javeriya.aifinance.entity.Transaction;
import com.javeriya.aifinance.service.UserService;
import com.javeriya.aifinance.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/api/users")
    public User createUser(@RequestBody User user) {
        return userService.registerUser(user); // fixed: was saveUser()
    }

    @PostMapping("/api/users/{userId}/transactions")
public ResponseEntity<?> addTransaction(
        @PathVariable Long userId,
        @RequestBody Transaction transaction) {
    try {
        Transaction saved = transactionService.addTransaction(userId, transaction);
        return ResponseEntity.ok(saved);
    } catch (RuntimeException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
}

    @GetMapping("/api/users/{userId}/transactions")
    public List<Transaction> getTransactions(@PathVariable Long userId) {
        return transactionService.getUserTransactions(userId);
    }

    @GetMapping("/api/status")
    public Map<String, String> status() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Running");
        return response;
    }
}