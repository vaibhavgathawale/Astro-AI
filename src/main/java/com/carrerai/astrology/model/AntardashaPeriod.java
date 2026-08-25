package com.carrerai.astrology.model;

public record AntardashaPeriod(
        Planet mahadashaLord,
        Planet antardashaLord,
        double startYear,
        double endYear
) {
}