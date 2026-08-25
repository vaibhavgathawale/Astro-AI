package com.carrerai.astrology.calculator;

import org.springframework.stereotype.Component;

@Component
public class PlanetHouseCalculator {

    public int calculateHouse(
            double planetLongitude,
            double[] houseCusps
    ) {

        for (int house = 1; house <= 12; house++) {

            double start = houseCusps[house];

            double end;

            if (house == 12) {
                end = houseCusps[1];
            } else {
                end = houseCusps[house + 1];
            }

            // Normal range
            if (start < end) {

                if (planetLongitude >= start &&
                        planetLongitude < end) {

                    return house;
                }

            }
            // Range crosses 360°
            else {

                if (planetLongitude >= start ||
                        planetLongitude < end) {

                    return house;
                }
            }
        }

        return 0;
    }
}