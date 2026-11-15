package com.starbank.recommender.rules;

import com.starbank.recommender.dto.RecommendationDto;
import com.starbank.recommender.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static com.starbank.recommender.rules.RecommendationConstants.*;

@Component
public class Invest500RuleSet implements RecommendationRuleSet {

    private final RecommendationsRepository repository;

    public Invest500RuleSet(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> apply(UUID userId) {
        boolean usesDebit = repository.userUsesProductType(userId, "DEBIT");
        boolean usesInvest = repository.userUsesProductType(userId, "INVEST");
        int savingDeposits = repository.sumByTypeAndOperation(userId, "SAVING", "DEPOSIT");

        if (usesDebit && !usesInvest && savingDeposits > 1000) {
            return Optional.of(new RecommendationDto(INVEST_500_ID, INVEST_500_NAME, INVEST_500_TEXT));
        }
        return Optional.empty();
    }
}