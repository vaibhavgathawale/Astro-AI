package com.astroai.astrology.model;

import java.time.LocalDate;
import java.util.List;

public record CareerForecast(
        LocalDate forecastStartDate,
        LocalDate forecastEndDate,
        List<CareerForecastPeriod> periods
) {
}