package com.carrerai.astrology.model;

public record CareerFieldScore(
        String field,
        int score,
        String reason
) {
}