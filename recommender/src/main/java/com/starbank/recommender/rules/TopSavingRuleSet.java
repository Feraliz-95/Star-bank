package com.starbank.recommender.rules;


import com.starbank.recommender.dto.RecommendationDto;
import com.starbank.recommender.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;

import static com.starbank.recommender.rules.RecommendationConstants.*;

@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private final RecommendationsRepository repository;

    public TopSavingRuleSet(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> apply(UUID userId) {
        boolean usesDebit = repository.userUsesProductType(userId, "DEBIT");
        int debitDeposits = repository.sumByTypeAndOperation(userId, "DEBIT", "DEPOSIT");
        int savingDeposits = repository.sumByTypeAndOperation(userId, "SAVING", "DEPOSIT");
        int debitSpends = repository.sumByTypeAndOperation(userId, "DEBIT", "WITHDRAW");

        if (usesDebit
                && (debitDeposits >= 50_000 || savingDeposits >= 50_000)
                && debitDeposits > debitSpends) {
            return Optional.of(new RecommendationDto(TOP_SAVING_ID, TOP_SAVING_NAME, TOP_SAVING_TEXT));
        }
        return Optional.empty();
    }
}
