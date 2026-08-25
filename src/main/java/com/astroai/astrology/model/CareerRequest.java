package com.astroai.astrology.model;

public record CareerRequest(
        int year,
        int month,
        int day,
        double hour,
        double latitude,
        double longitude
) {
}