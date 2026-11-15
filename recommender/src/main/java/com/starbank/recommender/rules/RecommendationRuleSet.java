package com.starbank.recommender.rules;

import com.starbank.recommender.dto.RecommendationDto;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<RecommendationDto> apply(UUID userId);
}