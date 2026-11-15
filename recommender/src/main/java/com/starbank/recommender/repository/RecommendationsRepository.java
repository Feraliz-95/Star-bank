package com.starbank.recommender.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecommendationsRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String USES_PRODUCT_TYPE = """
            SELECT COUNT(*)
            FROM transactions t
            JOIN products p ON t.product_id = p.id
            WHERE t.user_id = ? AND p.type = ?
            """;


    private static final String SUM_BY_TYPE_AND_OPERATION = """
        SELECT COALESCE(SUM(t.amount), 0)
        FROM transactions t
        JOIN products p ON t.product_id = p.id
        WHERE t.user_id = ? AND p.type = ? AND t.type = ?
        """;
    /** Использует ли пользователь хотя бы один продукт заданного типа. */
    public boolean userUsesProductType(UUID userId, String productType) {
        Integer count = jdbcTemplate.queryForObject(
                USES_PRODUCT_TYPE, Integer.class, userId, productType);
        return count != null && count > 0;
    }

    /** Сумма транзакций заданного типа (DEPOSIT/WITHDRAW) по продуктам заданного типа. */
    public int sumByTypeAndOperation(UUID userId, String productType, String operationType) {
        Integer sum = jdbcTemplate.queryForObject(
                SUM_BY_TYPE_AND_OPERATION, Integer.class, userId, productType, operationType);
        return sum == null ? 0 : sum;
    }
}