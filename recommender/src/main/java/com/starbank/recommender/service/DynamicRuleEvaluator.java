package com.starbank.recommender.service;


import com.starbank.recommender.dto.QueryJson;
import com.starbank.recommender.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class DynamicRuleEvaluator {

    private final RecommendationsRepository repository;

    public DynamicRuleEvaluator(RecommendationsRepository repository) {
        this.repository = repository;
    }

    /** Правило выполняется, когда все запросы вернули true (с учётом negate). */
    public boolean evaluate(UUID userId, List<QueryJson> queries) {
        return queries.stream().allMatch(q -> evaluateQuery(userId, q) != q.negate());
    }

    private boolean evaluateQuery(UUID userId, QueryJson q) {
        return switch (q.query()) {
            case "USER_OF" ->
                    repository.countTransactionsByProductType(userId, q.arguments().get(0)) > 0;
            case "ACTIVE_USER_OF" ->
                    repository.countTransactionsByProductType(userId, q.arguments().get(0)) >= 5;
            case "TRANSACTION_SUM_COMPARE" -> compare(
                    repository.sumByTypeAndOperation(userId, q.arguments().get(0), q.arguments().get(1)),
                    q.arguments().get(2),
                    Integer.parseInt(q.arguments().get(3)));
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> compare(
                    repository.sumByTypeAndOperation(userId, q.arguments().get(0), "DEPOSIT"),
                    q.arguments().get(1),
                    repository.sumByTypeAndOperation(userId, q.arguments().get(0), "WITHDRAW"));
            default -> false;
        };
    }

    private boolean compare(int left, String operator, int right) {
        return switch (operator) {
            case ">" -> left > right;
            case "<" -> left < right;
            case "=" -> left == right;
            case ">=" -> left >= right;
            case "<=" -> left <= right;
            default -> false;
        };
    }
}