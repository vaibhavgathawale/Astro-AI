package com.astroai.career.calculator;

import com.astroai.astrology.calculator.TransitCalculator;
import com.astroai.astrology.model.JobTimingPrediction;
import com.astroai.astrology.model.Planet;
import org.springframework.stereotype.Component;

@Component
public class CareerTimingCalculator {

    private final TransitCalculator transitCalculator;

    public CareerTimingCalculator(
            TransitCalculator transitCalculator
    ) {
        this.transitCalculator = transitCalculator;
    }

    public int calculateCombinedScore(
            JobTimingPrediction dasha,
            int year,
            int month,
            int day,
            double hour,
            double[] siderealHouses
    ) {

        int dashaScore =
                dasha.score();

        int transitScore = 0;

        // =====================================================
        // Jupiter Transit
        // =====================================================

        transitScore +=
                transitCalculator.calculateCareerTransitScore(
                        Planet.JUPITER,
                        year,
                        month,
                        day,
                        hour,
                        siderealHouses
                );

        // =====================================================
        // Saturn Transit
        // =====================================================

        transitScore +=
                transitCalculator.calculateCareerTransitScore(
                        Planet.SATURN,
                        year,
                        month,
                        day,
                        hour,
                        siderealHouses
                );

        // =====================================================
        // Rahu Transit
        // =====================================================

        transitScore +=
                transitCalculator.calculateCareerTransitScore(
                        Planet.RAHU,
                        year,
                        month,
                        day,
                        hour,
                        siderealHouses
                );

        // =====================================================
        // Ketu Transit
        // =====================================================

        transitScore +=
                transitCalculator.calculateCareerTransitScore(
                        Planet.KETU,
                        year,
                        month,
                        day,
                        hour,
                        siderealHouses
                );

        // =====================================================
        // Combined Score
        // =====================================================

        int finalScore =
                dashaScore + transitScore;

        return Math.min(
                finalScore,
                100
        );
    }
}