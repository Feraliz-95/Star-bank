package com.starbank.recommender.repository;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;
    private final Cache<String, Integer> userOfCache;
    private final Cache<String, Integer> sumByTypeCache;

    public RecommendationsRepository(JdbcTemplate jdbcTemplate,
                                     Cache<String, Integer> userOfCache,
                                     Cache<String, Integer> sumByTypeCache) {
        this.jdbcTemplate = jdbcTemplate;
        this.userOfCache = userOfCache;
        this.sumByTypeCache = sumByTypeCache;
    }

    private static final String COUNT_BY_TYPE = """
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

    /** Использует ли пользователь хотя бы один продукт заданного типа (для USER_OF, с кешем). */
    public boolean userUsesProductType(UUID userId, String productType) {
        return countTransactionsByProductType(userId, productType) > 0;
    }

    /** Сколько транзакций пользователь сделал по продуктам типа X (для USER_OF и ACTIVE_USER_OF). */
    public int countTransactionsByProductType(UUID userId, String productType) {
        String key = userId + "|" + productType;
        Integer cached = userOfCache.getIfPresent(key);
        if (cached != null) return cached;
        Integer count = jdbcTemplate.queryForObject(COUNT_BY_TYPE, Integer.class, userId, productType);
        int result = count == null ? 0 : count;
        userOfCache.put(key, result);
        return result;
    }

    /** Сумма транзакций типа Y (DEPOSIT/WITHDRAW) по продуктам типа X (с кешем). */
    public int sumByTypeAndOperation(UUID userId, String productType, String transactionType) {
        String key = userId + "|" + productType + "|" + transactionType;
        Integer cached = sumByTypeCache.getIfPresent(key);
        if (cached != null) return cached;
        Integer sum = jdbcTemplate.queryForObject(SUM_BY_TYPE_AND_OPERATION, Integer.class,
                userId, productType, transactionType);
        int result = sum == null ? 0 : sum;
        sumByTypeCache.put(key, result);
        return result;
    }
}
