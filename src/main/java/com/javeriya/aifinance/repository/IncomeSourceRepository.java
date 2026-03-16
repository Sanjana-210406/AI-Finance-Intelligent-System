package com.javeriya.aifinance.repository;

import com.javeriya.aifinance.entity.IncomeSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeSourceRepository extends JpaRepository<IncomeSource, Long> {

    List<IncomeSource> findByUserId(Long userId);
}