package com.carrerai.astrology.model;

public record HouseLordPosition(
        int house,
        String sign,
        Planet lord,
        int lordHouse
) {
}