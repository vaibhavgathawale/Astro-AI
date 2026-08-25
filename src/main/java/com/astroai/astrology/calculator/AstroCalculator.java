package com.astroai.astrology.calculator;

import com.astroai.astrology.model.AstroChart;

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
