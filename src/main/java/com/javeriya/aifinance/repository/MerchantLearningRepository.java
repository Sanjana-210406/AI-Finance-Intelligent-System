package com.javeriya.aifinance.repository;

import com.javeriya.aifinance.entity.MerchantLearning;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MerchantLearningRepository extends JpaRepository<MerchantLearning, Long> {

    // Find mapping for a specific user and merchant
    Optional<MerchantLearning> findByUserIdAndMerchantNameIgnoreCase(Long userId, String merchantName);

    // Get all mappings for a user
    List<MerchantLearning> findByUserId(Long userId);
}
