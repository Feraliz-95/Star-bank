package com.starbank.recommender.service;


import com.starbank.recommender.dto.QueryJson;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RuleValidator {

    private static final Set<String> PRODUCT_TYPES = Set.of("DEBIT", "CREDIT", "INVEST", "SAVING");
    private static final Set<String> TX_TYPES = Set.of("DEPOSIT", "WITHDRAW");
    private static final Set<String> OPERATORS = Set.of(">", "<", "=", ">=", "<=");
    private static final Set<String> QUERY_TYPES = Set.of(
            "USER_OF", "ACTIVE_USER_OF", "TRANSACTION_SUM_COMPARE",
            "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW");

    public void validate(QueryJson q) {
        if (!QUERY_TYPES.contains(q.query())) {
            throw new IllegalArgumentException("Неизвестный тип запроса: " + q.query());
        }
        switch (q.query()) {
            case "USER_OF", "ACTIVE_USER_OF" -> {
                checkArgs(q, 1);
                check(q.arguments().get(0), PRODUCT_TYPES);
            }
            case "TRANSACTION_SUM_COMPARE" -> {
                checkArgs(q, 4);
                check(q.arguments().get(0), PRODUCT_TYPES);
                check(q.arguments().get(1), TX_TYPES);
                check(q.arguments().get(2), OPERATORS);
                int value = Integer.parseInt(q.arguments().get(3));
                if (value < 0) {
                    throw new IllegalArgumentException("Число C должно быть неотрицательным");
                }
            }
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> {
                checkArgs(q, 2);
                check(q.arguments().get(0), PRODUCT_TYPES);
                check(q.arguments().get(1), OPERATORS);
            }
        }
    }

    private void checkArgs(QueryJson q, int expected) {
        if (q.arguments() == null || q.arguments().size() != expected) {
            throw new IllegalArgumentException(
                    "Запрос " + q.query() + " требует " + expected + " аргументов");
        }
    }

    private void check(String value, Set<String> allowed) {
        if (!allowed.contains(value)) {
            throw new IllegalArgumentException("Недопустимое значение: " + value);
        }
    }
}