package com.astroai.astrology.model;

import java.time.LocalDate;

public record CareerImportantWindow(
        LocalDate startDate,
        LocalDate endDate,
        String type,
        String description,
        int score,
        String reason,
        String remedy
) {
}