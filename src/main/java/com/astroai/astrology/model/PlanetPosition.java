package com.astroai.astrology.model;

public record  PlanetPosition(
        String planet,
        double longitude,
        double speed,
        String sign
){
}
