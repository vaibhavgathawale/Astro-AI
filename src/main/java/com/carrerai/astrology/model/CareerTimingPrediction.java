package com.carrerai.astrology.model;

public record CareerTimingPrediction(
        Planet mahadashaLord,
        Planet antardashaLord,
        Planet pratyantardashaLord,
        double startYear,
        double endYear,
        int score,
        String reason
) {
}