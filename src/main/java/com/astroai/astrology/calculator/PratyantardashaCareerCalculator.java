package com.astroai.astrology.calculator;

import com.astroai.astrology.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PratyantardashaCareerCalculator {

    public List<JobTimingPrediction> calculate(
            List<PratyantardashaPeriod> periods,
            List<HouseLordPosition> houseLordPositions
    ) {

        List<JobTimingPrediction> result =
                new ArrayList<>();

        Planet secondLord =
                findHouseLord(
                        houseLordPositions,
                        2
                );

        Planet sixthLord =
                findHouseLord(
                        houseLordPositions,
                        6
                );

        Planet tenthLord =
                findHouseLord(
                        houseLordPositions,
                        10
                );

        Planet eleventhLord =
                findHouseLord(
                        houseLordPositions,
                        11
                );

        for (PratyantardashaPeriod period : periods) {

            Planet maha =
                    period.mahadashaLord();

            Planet antar =
                    period.antardashaLord();

            Planet pratyantar =
                    period.pratyantardashaLord();

            int score = 30;

            List<String> reasons =
                    new ArrayList<>();

            // =================================================
            // Mahadasha
            // =================================================

            if (isCareerLord(
                    maha,
                    secondLord,
                    sixthLord,
                    tenthLord,
                    eleventhLord
            )) {

                score += 10;

                reasons.add(
                        "Mahadasha lord supports career"
                );
            }

            // =================================================
            // Antardasha
            // =================================================

            if (isCareerLord(
                    antar,
                    secondLord,
                    sixthLord,
                    tenthLord,
                    eleventhLord
            )) {

                score += 15;

                reasons.add(
                        "Antardasha lord supports career"
                );
            }

            // =================================================
            // Pratyantardasha
            // =================================================

            if (isCareerLord(
                    pratyantar,
                    secondLord,
                    sixthLord,
                    tenthLord,
                    eleventhLord
            )) {

                score += 25;

                reasons.add(
                        "Pratyantardasha lord activates a career house"
                );
            }

            // =================================================
            // 10th House Lord
            // =================================================

            if (maha == tenthLord) {

                score += 10;

                reasons.add(
                        "10th house lord is running Mahadasha"
                );
            }

            if (antar == tenthLord) {

                score += 10;

                reasons.add(
                        "10th house lord is running Antardasha"
                );
            }

            if (pratyantar == tenthLord) {

                score += 15;

                reasons.add(
                        "10th house lord is running Pratyantardasha"
                );
            }

            // =================================================
            // 6th House Lord
            // =================================================

            if (pratyantar == sixthLord) {

                score += 10;

                reasons.add(
                        "6th house lord supports employment"
                );
            }

            // =================================================
            // 11th House Lord
            // =================================================

            if (pratyantar == eleventhLord) {

                score += 10;

                reasons.add(
                        "11th house lord supports gains and income"
                );
            }

            // =================================================
            // Planetary Support
            // =================================================

            score +=
                    calculatePlanetScore(
                            pratyantar
                    ) / 10;

            score =
                    Math.min(
                            score,
                            100
                    );

            String reason =
                    reasons.isEmpty()
                            ? "General planetary career support"
                            : String.join(
                            "; ",
                            reasons
                    );

            // =================================================
            // Job Timing Prediction
            // =================================================

            result.add(
                    new JobTimingPrediction(
                            maha,
                            antar,
                            period.startYear(),
                            period.endYear(),
                            score,
                            reason
                    )
            );
        }

        return result;
    }


    // =========================================================
    // Career Lord Check
    // =========================================================

    private boolean isCareerLord(
            Planet planet,
            Planet secondLord,
            Planet sixthLord,
            Planet tenthLord,
            Planet eleventhLord
    ) {

        return planet == secondLord
                || planet == sixthLord
                || planet == tenthLord
                || planet == eleventhLord;
    }


    // =========================================================
    // Find House Lord
    // =========================================================

    private Planet findHouseLord(
            List<HouseLordPosition> positions,
            int house
    ) {

        return positions.stream()
                .filter(
                        position ->
                                position.house() == house
                )
                .map(
                        HouseLordPosition::lord
                )
                .findFirst()
                .orElse(null);
    }


    // =========================================================
    // Planetary Score
    // =========================================================

    private int calculatePlanetScore(
            Planet planet
    ) {

        if (planet == null) {
            return 0;
        }

        return switch (planet) {

            case SATURN -> 90;
            case MERCURY -> 90;
            case RAHU -> 90;
            case JUPITER -> 85;
            case SUN -> 75;
            case MARS -> 75;
            case VENUS -> 70;
            case MOON -> 65;
            case KETU -> 50;
        };
    }
}