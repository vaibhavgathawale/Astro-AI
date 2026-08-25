package com.astroai.astrology.calculator;

import com.astroai.astrology.model.AntardashaPeriod;
import com.astroai.astrology.model.DashaPeriod;
import com.astroai.astrology.model.Planet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AntardashaCalculator {

    // =========================================================
    // Vimshottari Dasha Order
    // =========================================================

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


    private static final double TOTAL_DASHA_YEARS = 120.0;


    // =========================================================
    // Calculate Antardashas
    // =========================================================

    public List<AntardashaPeriod> calculate(
            DashaPeriod mahadasha
    ) {

        List<AntardashaPeriod> result =
                new ArrayList<>();


        Planet mahadashaLord =
                mahadasha.planet();


        double mahadashaStart =
                mahadasha.startYear();


        double mahadashaEnd =
                mahadasha.endYear();


        double mahadashaDuration =
                mahadashaEnd - mahadashaStart;


        // -----------------------------------------------------
        // Find Mahadasha planet index
        // -----------------------------------------------------

        int mahadashaIndex =
                getPlanetIndex(
                        mahadashaLord
                );


        // -----------------------------------------------------
        // Antardasha starts from Mahadasha lord
        // -----------------------------------------------------

        int index =
                mahadashaIndex;


        double currentYear =
                mahadashaStart;


        // -----------------------------------------------------
        // Generate 9 Antardashas
        // -----------------------------------------------------

        for (int i = 0; i < DASHA_ORDER.length; i++) {

            Planet antardashaLord =
                    DASHA_ORDER[index];


            /*
             * Antardasha duration:
             *
             * Mahadasha duration ×
             * Antardasha planet years / 120
             */

            double duration =
                    mahadashaDuration
                            * DASHA_YEARS[index]
                            / TOTAL_DASHA_YEARS;


            double endYear =
                    currentYear + duration;


            // Avoid floating point overflow
            if (i == DASHA_ORDER.length - 1) {
                endYear = mahadashaEnd;
            }


            result.add(
                    new AntardashaPeriod(
                            mahadashaLord,
                            antardashaLord,
                            currentYear,
                            endYear
                    )
            );


            currentYear =
                    endYear;


            index =
                    (index + 1)
                            % DASHA_ORDER.length;
        }


        return result;
    }


    // =========================================================
    // Find Planet Index
    // =========================================================

    private int getPlanetIndex(
            Planet planet
    ) {

        for (int i = 0;
             i < DASHA_ORDER.length;
             i++) {

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