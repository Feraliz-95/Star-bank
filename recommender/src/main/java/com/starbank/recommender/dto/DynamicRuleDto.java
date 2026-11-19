package com.starbank.recommender.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DynamicRuleDto(
        String id,
        @JsonProperty("product_name") String productName,
        @JsonProperty("product_id") String productId,
        @JsonProperty("product_text") String productText,
        List<QueryJson> rule) {}