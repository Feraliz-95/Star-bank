package com.starbank.recommender.entity;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "dynamic_rules")
public class DynamicRule {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_id", nullable = false, unique = true)
    private String productId;

    @Column(name = "product_text", nullable = false)
    private String productText;

    @Column(name = "rule_json", nullable = false, columnDefinition = "TEXT")
    private String ruleJson;

    public UUID getId() { return id; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getProductText() { return productText; }
    public void setProductText(String productText) { this.productText = productText; }
    public String getRuleJson() { return ruleJson; }
    public void setRuleJson(String ruleJson) { this.ruleJson = ruleJson; }
}