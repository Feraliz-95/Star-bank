package com.starbank.recommender.service;


import com.starbank.recommender.dto.DynamicRuleDto;
import com.starbank.recommender.dto.RecommendationDto;
import com.starbank.recommender.dto.UserRecommendationsDto;
import com.starbank.recommender.rules.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;
    private final RuleService ruleService;
    private final DynamicRuleEvaluator evaluator;

    public RecommendationService(List<RecommendationRuleSet> ruleSets,
                                 RuleService ruleService,
                                 DynamicRuleEvaluator evaluator) {
        this.ruleSets = ruleSets;
        this.ruleService = ruleService;
        this.evaluator = evaluator;
    }

    public UserRecommendationsDto getRecommendations(UUID userId) {
        Map<String, RecommendationDto> byProduct = new LinkedHashMap<>();

        // 1. Статические правила — как раньше
        ruleSets.stream()
                .map(rule -> rule.apply(userId))
                .flatMap(Optional::stream)
                .forEach(rec -> byProduct.put(rec.id(), rec));

        // 2. Динамические правила из базы
        for (DynamicRuleDto rule : ruleService.getAll()) {
            if (evaluator.evaluate(userId, rule.rule())) {
                byProduct.putIfAbsent(rule.productId(),
                        new RecommendationDto(rule.productId(), rule.productName(), rule.productText()));
            }
        }

        return new UserRecommendationsDto(userId.toString(), new ArrayList<>(byProduct.values()));
    }
}