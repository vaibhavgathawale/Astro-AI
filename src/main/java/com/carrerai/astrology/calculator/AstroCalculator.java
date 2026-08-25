package com.carrerai.astrology.calculator;

import com.carrerai.astrology.model.AstroChart;

import java.time.LocalDate;
import java.time.LocalTime;

public interface AstroCalculator {

    AstroChart calculate(
            LocalDate dateOfBirth,
            LocalTime timeOfBirth,
            double latitude,
            double longitude
    );
}
