package com.astroai.astrology.model;

import java.time.LocalDate;
import java.util.List;

public record CareerForecastPeriod(
        LocalDate startDate,
        LocalDate endDate,
        String periodType,
        int score,
        String status,
        String reason,
        List<String> importantEvents,
        List<String> remedies
) {
}