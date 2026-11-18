package com.starbank.recommender.rules;


import com.starbank.recommender.dto.RecommendationDto;
import com.starbank.recommender.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.UUID;
import static com.starbank.recommender.rules.RecommendationConstants.*;

@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {

    private final RecommendationsRepository repository;

    public SimpleCreditRuleSet(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> apply(UUID userId) {
        boolean usesCredit = repository.userUsesProductType(userId, "CREDIT");
        int debitDeposits = repository.sumByTypeAndOperation(userId, "DEBIT", "DEPOSIT");
        int debitSpends = repository.sumByTypeAndOperation(userId, "DEBIT", "WITHDRAW");

        if (!usesCredit && debitDeposits > debitSpends && debitSpends > 100_000) {
            return Optional.of(new RecommendationDto(SIMPLE_CREDIT_ID, SIMPLE_CREDIT_NAME, SIMPLE_CREDIT_TEXT));
        }
        return Optional.empty();
    }
}