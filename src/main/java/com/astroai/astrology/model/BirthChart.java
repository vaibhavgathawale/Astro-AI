package com.astroai.astrology.model;

import java.util.List;

public record BirthChart(
        List<PlanetPosition> planets,
        List<HousePosition> houses,
        double ascendant,
        String ascendantSign
) {
}