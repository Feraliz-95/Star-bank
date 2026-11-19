package com.starbank.recommender.controller;


import com.starbank.recommender.dto.DynamicRuleDto;
import com.starbank.recommender.service.RuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rule")
public class RuleController {

    private final RuleService ruleService;

    public RuleController(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    @PostMapping
    public DynamicRuleDto create(@RequestBody DynamicRuleDto dto) {
        return ruleService.create(dto);
    }

    @GetMapping
    public Map<String, List<DynamicRuleDto>> getAll() {
        return Map.of("data", ruleService.getAll());
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable String productId) {
        ruleService.deleteByProductId(productId);
        return ResponseEntity.noContent().build();
    }
}