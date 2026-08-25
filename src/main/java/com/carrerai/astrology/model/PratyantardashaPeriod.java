package com.carrerai.astrology.model;

public record PratyantardashaPeriod(
        Planet mahadashaLord,
        Planet antardashaLord,
        Planet pratyantardashaLord,
        double startYear,
        double endYear
) {
}