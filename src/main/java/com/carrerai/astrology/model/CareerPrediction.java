package com.carrerai.astrology.model;

import java.util.List;

public record CareerPrediction(
        List<CareerFieldScore> careerFields,
        List<JobTimingPrediction> bestPeriods,
        JobTimingPrediction currentDasha,
        CareerScore careerScore,
        CareerConclusion conclusion,
        JobOpportunityPrediction jobOpportunity
) {
}