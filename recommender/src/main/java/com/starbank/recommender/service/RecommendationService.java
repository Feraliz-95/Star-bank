package com.starbank.recommender.service;

import com.starbank.recommender.dto.RecommendationDto;
import com.starbank.recommender.dto.UserRecommendationsDto;
import com.starbank.recommender.rules.RecommendationRuleSet;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;

    public RecommendationService(List<RecommendationRuleSet> ruleSets) {
        this.ruleSets = ruleSets;
    }

    public UserRecommendationsDto getRecommendations(UUID userId) {
        List<RecommendationDto> recommendations = ruleSets.stream()
                .map(rule -> rule.apply(userId))
                .flatMap(Optional::stream)
                .toList();
        return new UserRecommendationsDto(userId.toString(), recommendations);
    }
}