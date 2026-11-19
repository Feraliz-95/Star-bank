package com.starbank.recommender.repository.rules;

import com.starbank.recommender.entity.DynamicRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DynamicRuleRepository extends JpaRepository<DynamicRule, UUID> {
    Optional<DynamicRule> findByProductId(String productId);
    void deleteByProductId(String productId);
}