package com.astroai.astrology.calculator;

import com.astroai.astrology.model.Nakshatra;
import org.springframework.stereotype.Component;

@Component
public class NakshatraCalculator {

    private static final double NAKSHATRA_SIZE =
            360.0 / 27.0;

    private static final double PADA_SIZE =
            NAKSHATRA_SIZE / 4.0;

    public Nakshatra calculateNakshatra(
            double siderealLongitude
    ) {

        int index =
                (int) (siderealLongitude / NAKSHATRA_SIZE);

        return Nakshatra.values()[index];
    }

    public int calculatePada(
            double siderealLongitude
    ) {

        double positionInNakshatra =
                siderealLongitude % NAKSHATRA_SIZE;

        return (int)
                (positionInNakshatra / PADA_SIZE) + 1;
    }
}