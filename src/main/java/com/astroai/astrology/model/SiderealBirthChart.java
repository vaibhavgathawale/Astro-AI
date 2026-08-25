package com.astroai.astrology.model;

import java.util.List;

public record SiderealBirthChart(
        List<SiderealPlanetPosition> planets,
        List<HousePosition> houses,
        List<PlanetHousePosition> planetHousePositions,
        List<HouseLordPosition> houseLordPositions,
        List<CareerIndicator> careerIndicators,
        double ascendant,
        String ascendantSign
) {
}