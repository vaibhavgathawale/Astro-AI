package com.carrerai.astrology.calculator;

import com.carrerai.astrology.model.DashaPeriod;
import com.carrerai.astrology.model.Planet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VimshottariAntardashaCalculator {

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
    // Calculate Antardashas inside one Mahadasha
    // =========================================================

    public List<DashaPeriod> calculateAntardashas(
            DashaPeriod mahadasha
    ) {

        List<DashaPeriod> antardashas =
                new ArrayList<>();

        Planet mahadashaPlanet =
                mahadasha.planet();

        int mahadashaIndex =
                getPlanetIndex(mahadashaPlanet);

        double mahadashaStart =
                mahadasha.startYear();

        double mahadashaEnd =
                mahadasha.endYear();

        /*
         * Full Mahadasha duration
         */
        double mahadashaDuration =
                mahadashaEnd - mahadashaStart;


        /*
         * Antardasha starts from the
         * Mahadasha lord itself.
         */
        for (int i = 0; i < DASHA_ORDER.length; i++) {

            int antardashaIndex =
                    (mahadashaIndex + i)
                            % DASHA_ORDER.length;

            Planet antardashaPlanet =
                    DASHA_ORDER[antardashaIndex];


            /*
             * Standard Vimshottari formula:
             *
             * AD duration =
             *
             * MD duration × AD lord years
             * --------------------------------
             *        120 years
             */
            double duration =
                    mahadashaDuration
                            * DASHA_YEARS[antardashaIndex]
                            / TOTAL_DASHA_YEARS;


            double startYear;

            if (antardashas.isEmpty()) {

                startYear =
                        mahadashaStart;

            } else {

                startYear =
                        antardashas
                                .get(antardashas.size() - 1)
                                .endYear();
            }


            double endYear =
                    startYear + duration;


            /*
             * Floating point rounding protection.
             */
            if (endYear > mahadashaEnd) {
                endYear = mahadashaEnd;
            }


            antardashas.add(
                    new DashaPeriod(
                            antardashaPlanet,
                            startYear,
                            endYear
                    )
            );
        }

        return antardashas;
    }


    // =========================================================
    // Find Planet index
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
                "Planet not found in Vimshottari order: "
                        + planet
        );
    }
}