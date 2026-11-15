package com.starbank.recommender.controller;


import com.starbank.recommender.dto.UserRecommendationsDto;
import com.starbank.recommender.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @GetMapping("/recommendation/{userId}")
    public UserRecommendationsDto getRecommendations(@PathVariable UUID userId) {
        return service.getRecommendations(userId);
    }
}