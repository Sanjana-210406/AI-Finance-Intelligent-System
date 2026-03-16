package com.javeriya.aifinance.service;

import com.javeriya.aifinance.entity.ExpenseCategory;
import com.javeriya.aifinance.entity.MerchantLearning;
import com.javeriya.aifinance.entity.User;
import com.javeriya.aifinance.repository.ExpenseCategoryRepository;
import com.javeriya.aifinance.repository.MerchantLearningRepository;
import com.javeriya.aifinance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MerchantLearningService {

    @Autowired
    private MerchantLearningRepository merchantLearningRepository;

    @Autowired
    private ExpenseCategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    // Save or update a merchant mapping
    public MerchantLearning saveMerchantMapping(Long userId, String merchantName, Long categoryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ExpenseCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Check if mapping already exists
        Optional<MerchantLearning> existing = merchantLearningRepository
                .findByUserIdAndMerchantNameIgnoreCase(userId, merchantName);

        if (existing.isPresent()) {
            // Update times used
            MerchantLearning mapping = existing.get();
            mapping.setCategory(category);
            mapping.setTimesUsed(mapping.getTimesUsed() + 1);
            return merchantLearningRepository.save(mapping);
        }

        // Create new mapping
        MerchantLearning newMapping = new MerchantLearning(user, merchantName.toLowerCase(), category);
        return merchantLearningRepository.save(newMapping);
    }

    // Fetch category for a merchant
    public Optional<ExpenseCategory> getCategoryForMerchant(Long userId, String merchantName) {
        return merchantLearningRepository
                .findByUserIdAndMerchantNameIgnoreCase(userId, merchantName)
                .map(MerchantLearning::getCategory);
    }

    // Get all learned mappings for a user
    public List<MerchantLearning> getAllMappings(Long userId) {
        return merchantLearningRepository.findByUserId(userId);
    }
}
