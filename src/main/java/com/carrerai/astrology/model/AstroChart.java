package com.carrerai.astrology.model;

import java.util.List;

public record AstroChart(
        String ascendant,
        List<PlanetPosition> planets
) {
}
