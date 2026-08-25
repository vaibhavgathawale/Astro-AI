package com.carrerai.astrology.model;

public record CareerIndicator(
        int house,
        Planet lord,
        int lordHouse,
        String field,
        int score,
        String reason
) {
}