package com.carrerai.astrology.model;

public record JobTimingPrediction(
        Planet mahadashaLord,
        Planet antardashaLord,
        double startYear,
        double endYear,
        int score,
        String reason
) {
}