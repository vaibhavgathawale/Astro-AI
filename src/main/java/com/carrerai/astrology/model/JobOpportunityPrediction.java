package com.carrerai.astrology.model;

import java.util.List;

public record JobOpportunityPrediction(
        int currentScore,
        String currentStatus,
        String bestPeriod,
        List<String> reasons
) {
}