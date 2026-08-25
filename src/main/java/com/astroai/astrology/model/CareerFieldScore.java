package com.astroai.astrology.model;

public record CareerFieldScore(
        String field,
        int score,
        String reason
) {
}