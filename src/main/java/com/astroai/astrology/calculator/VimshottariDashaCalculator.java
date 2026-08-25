package com.astroai.astrology.calculator;

import com.astroai.astrology.model.DashaPeriod;
import com.astroai.astrology.model.Planet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VimshottariDashaCalculator {

    // Vimshottari Mahadasha duration in years
    private static final double TOTAL_DASHA_YEARS = 120.0;

    private static final Planet[] DASHA_ORDER = {
            Planet.KETU,
            Planet.VENUS,
            Planet.SUN,
            Planet.MOON,
            Planet.MARS,
            Planet.RAHU,
            Planet.JUPITER,
            Planet.SATURN,
            Planet.MERCURY
    };

    private static final double[] DASHA_YEARS = {
            7.0,
            20.0,
            6.0,
            10.0,
            7.0,
            18.0,
            16.0,
            19.0,
            17.0
    };

    /**
     * Calculates Vimshottari Mahadasha sequence.
     *
     * @param moonLongitude Sidereal Moon longitude (0 - 360)
     * @param birthYear Birth year
     * @param numberOfPeriods Number of Mahadasha periods required
     */
    public List<DashaPeriod> calculateMahadashas(
            double moonLongitude,
            double birthYear,
            int numberOfPeriods
    ) {

        List<DashaPeriod> periods =
                new ArrayList<>();

        // ---------------------------------------------------------
        // 1. Calculate Nakshatra
        // ---------------------------------------------------------

        double nakshatraSize =
                360.0 / 27.0;

        int nakshatraIndex =
                (int) (moonLongitude / nakshatraSize);

        // ---------------------------------------------------------
        // 2. Find starting Dasha lord
        // ---------------------------------------------------------

        Planet startingPlanet =
                DASHA_ORDER[nakshatraIndex % 9];

        // ---------------------------------------------------------
        // 3. Position inside Nakshatra
        // ---------------------------------------------------------

        double nakshatraStart =
                nakshatraIndex * nakshatraSize;

        double positionInNakshatra =
                moonLongitude - nakshatraStart;

        double remainingFraction =
                1.0 -
                        (positionInNakshatra / nakshatraSize);

        // ---------------------------------------------------------
        // 4. Find starting planet index
        // ---------------------------------------------------------

        int startingIndex =
                getPlanetIndex(startingPlanet);

        // ---------------------------------------------------------
        // 5. Remaining duration of first Mahadasha
        // ---------------------------------------------------------

        double firstDashaDuration =
                DASHA_YEARS[startingIndex]
                        * remainingFraction;

        double currentYear =
                birthYear;

        // ---------------------------------------------------------
        // 6. First Mahadasha
        // ---------------------------------------------------------

        double endYear =
                currentYear + firstDashaDuration;

        periods.add(
                new DashaPeriod(
                        startingPlanet,
                        currentYear,
                        endYear
                )
        );

        currentYear = endYear;

        // ---------------------------------------------------------
        // 7. Remaining Mahadashas
        // ---------------------------------------------------------

        int index =
                (startingIndex + 1) % DASHA_ORDER.length;

        while (periods.size() < numberOfPeriods) {

            Planet planet =
                    DASHA_ORDER[index];

            double duration =
                    DASHA_YEARS[index];

            endYear =
                    currentYear + duration;

            periods.add(
                    new DashaPeriod(
                            planet,
                            currentYear,
                            endYear
                    )
            );

            currentYear = endYear;

            index =
                    (index + 1) % DASHA_ORDER.length;
        }

        return periods;
    }

    private int getPlanetIndex(
            Planet planet
    ) {

        for (int i = 0; i < DASHA_ORDER.length; i++) {

            if (DASHA_ORDER[i] == planet) {
                return i;
            }
        }

        throw new IllegalArgumentException(
                "Planet not found in Vimshottari Dasha order: "
                        + planet
        );
    }
}