package com.astroai.astrology.model;

public record HousePosition(
        int house,
        double longitude,
        String sign
) {
}