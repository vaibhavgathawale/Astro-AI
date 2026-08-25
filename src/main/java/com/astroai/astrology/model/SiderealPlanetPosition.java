package com.astroai.astrology.model;

public record SiderealPlanetPosition(
        String planet,
        double tropicalLongitude,
        double ayanamsa,
        double siderealLongitude,
        String sign,
        String nakshatra,
        int pada
) {
}