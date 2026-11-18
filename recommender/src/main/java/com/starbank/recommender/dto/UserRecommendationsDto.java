package com.starbank.recommender.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record UserRecommendationsDto(
        @JsonProperty("user_id") String userId,
        List<RecommendationDto> recommendations) {}