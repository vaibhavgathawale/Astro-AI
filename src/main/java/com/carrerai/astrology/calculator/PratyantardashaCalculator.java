package com.carrerai.astrology.calculator;

import com.carrerai.astrology.model.AntardashaPeriod;
import com.carrerai.astrology.model.Planet;
import com.carrerai.astrology.model.PratyantardashaPeriod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PratyantardashaCalculator {

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


    public List<PratyantardashaPeriod> calculate(
            AntardashaPeriod antardasha
    ) {

        List<PratyantardashaPeriod> result =
                new ArrayList<>();

        Planet mahadashaLord =
                antardasha.mahadashaLord();

        Planet antardashaLord =
                antardasha.antardashaLord();

        double startYear =
                antardasha.startYear();

        double endYear =
                antardasha.endYear();

        double antardashaDuration =
                endYear - startYear;


        // -----------------------------------------------------
        // Start Pratyantardasha from Antardasha Lord
        // -----------------------------------------------------

        int index =
                getPlanetIndex(antardashaLord);

        double currentYear =
                startYear;


        // -----------------------------------------------------
        // Generate 9 Pratyantardashas
        // -----------------------------------------------------

        for (int i = 0; i < DASHA_ORDER.length; i++) {

            Planet pratyantardashaLord =
                    DASHA_ORDER[index];

            double duration =
                    antardashaDuration
                            * DASHA_YEARS[index]
                            / TOTAL_DASHA_YEARS;

            double pratyantardashaEnd =
                    currentYear + duration;


            // Avoid floating-point rounding
            if (i == DASHA_ORDER.length - 1) {
                pratyantardashaEnd = endYear;
            }


            result.add(
                    new PratyantardashaPeriod(
                            mahadashaLord,
                            antardashaLord,
                            pratyantardashaLord,
                            currentYear,
                            pratyantardashaEnd
                    )
            );


            currentYear =
                    pratyantardashaEnd;


            index =
                    (index + 1)
                            % DASHA_ORDER.length;
        }

        return result;
    }


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