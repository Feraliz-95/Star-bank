package com.starbank.recommender.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.starbank.recommender.dto.DynamicRuleDto;
import com.starbank.recommender.dto.QueryJson;
import com.starbank.recommender.entity.DynamicRule;
import com.starbank.recommender.repository.rules.DynamicRuleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class RuleService {

    private final DynamicRuleRepository repository;
    private final ObjectMapper objectMapper;
    private final RuleValidator validator;

    public RuleService(DynamicRuleRepository repository,
                       ObjectMapper objectMapper,
                       RuleValidator validator) {
        this.repository = repository;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    public DynamicRuleDto create(DynamicRuleDto dto) {
        dto.rule().forEach(validator::validate);
        DynamicRule entity = new DynamicRule();
        entity.setProductName(dto.productName());
        entity.setProductId(dto.productId());
        entity.setProductText(dto.productText());
        entity.setRuleJson(serialize(dto.rule()));
        return toDto(repository.save(entity));
    }

    public List<DynamicRuleDto> getAll() {
        return repository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional
    public void deleteByProductId(String productId) {
        repository.deleteByProductId(productId);
    }

    private String serialize(List<QueryJson> rule) {
        try {
            return objectMapper.writeValueAsString(rule);
        } catch (Exception e) {
            throw new IllegalStateException("Не удалось сериализовать правило", e);
        }
    }

    private DynamicRuleDto toDto(DynamicRule entity) {
        try {
            List<QueryJson> rule = objectMapper.readValue(
                    entity.getRuleJson(), new TypeReference<List<QueryJson>>() {});
            return new DynamicRuleDto(
                    entity.getId().toString(),
                    entity.getProductName(),
                    entity.getProductId(),
                    entity.getProductText(),
                    rule);
        } catch (Exception e) {
            throw new IllegalStateException("Не удалось десериализовать правило", e);
        }
    }
}