package com.starbank.recommender.dto;

import java.util.List;

public record QueryJson(String query, List<String> arguments, boolean negate) {}