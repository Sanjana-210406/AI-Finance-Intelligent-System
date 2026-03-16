package com.javeriya.aifinance;

import com.javeriya.aifinance.entity.ExpenseCategory;
import com.javeriya.aifinance.entity.MerchantLearning;
import com.javeriya.aifinance.service.MerchantLearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/merchant")
public class MerchantLearningController {

    @Autowired
    private MerchantLearningService merchantLearningService;

    // Save a merchant mapping
    @PostMapping("/learn/{userId}")
    public ResponseEntity<?> saveMerchantMapping(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> request) {
        try {
            String merchantName = (String) request.get("merchantName");
            Long categoryId = Long.valueOf(request.get("categoryId").toString());

            MerchantLearning mapping = merchantLearningService
                    .saveMerchantMapping(userId, merchantName, categoryId);

            return ResponseEntity.ok(mapping);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get category for a merchant
    @GetMapping("/lookup/{userId}")
    public ResponseEntity<?> lookupMerchant(
            @PathVariable Long userId,
            @RequestParam String merchantName) {
        Optional<ExpenseCategory> category = merchantLearningService
                .getCategoryForMerchant(userId, merchantName);

        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        } else {
            return ResponseEntity.badRequest().body("No mapping found for: " + merchantName);
        }
    }

    // Get all mappings for a user
    @GetMapping("/mappings/{userId}")
    public ResponseEntity<?> getAllMappings(@PathVariable Long userId) {
        List<MerchantLearning> mappings = merchantLearningService.getAllMappings(userId);
        return ResponseEntity.ok(mappings);
    }
}
