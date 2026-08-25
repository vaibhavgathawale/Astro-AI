package com.carrerai.astrology.model;

import java.util.List;

public record CareerConclusion(
        String overallConclusion,
        int overallScore,
        String jobOpportunity,
        String careerGrowth,
        String incomeOutlook,
        String bestPeriod,
        String currentPeriod,
        List<String> keyReasons
) {
}