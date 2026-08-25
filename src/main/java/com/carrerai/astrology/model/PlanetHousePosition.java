package com.carrerai.astrology.model;

public record PlanetHousePosition(
        Planet planet,
        double longitude,
        String sign,
        String nakshatra,
        int pada,
        int house
) {
}