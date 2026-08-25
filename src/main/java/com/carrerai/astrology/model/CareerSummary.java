package com.carrerai.astrology.model;

import java.util.List;

public record CareerSummary(

        CareerScore careerScore,

        List<CareerFieldScore> careerFields,

        JobTimingPrediction currentDasha,

        List<JobTimingPrediction> favourablePeriods,

        String careerConclusion
) {
}