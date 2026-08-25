package com.astroai.astrology.model;

import java.util.List;

public record CareerHouseAnalysis(
        int house,
        String sign,
        Planet lord,
        int lordHouse,
        List<Planet> planetsInHouse
) {
}