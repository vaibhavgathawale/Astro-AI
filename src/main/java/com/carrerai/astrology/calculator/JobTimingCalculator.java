package com.carrerai.astrology.calculator;

import com.carrerai.astrology.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JobTimingCalculator {

    // =========================================================
    // Calculate Job Timing
    // =========================================================

    public List<JobTimingPrediction> calculate(
            List<AntardashaPeriod> antardashas,
            List<HouseLordPosition> houseLordPositions
    ) {

        List<JobTimingPrediction> result =
                new ArrayList<>();

        // -----------------------------------------------------
        // Career house lords
        // -----------------------------------------------------

        Planet secondLord =
                findHouseLord(houseLordPositions, 2);

        Planet sixthLord =
                findHouseLord(houseLordPositions, 6);

        Planet tenthLord =
                findHouseLord(houseLordPositions, 10);

        Planet eleventhLord =
                findHouseLord(houseLordPositions, 11);

        // -----------------------------------------------------
        // Evaluate every Antardasha
        // -----------------------------------------------------

        for (AntardashaPeriod period : antardashas) {

            Planet mahadashaLord =
                    period.mahadashaLord();

            Planet antardashaLord =
                    period.antardashaLord();

            int score = 20;

            List<String> reasons =
                    new ArrayList<>();

            // =================================================
            // Mahadasha Lord
            // =================================================

            if (isCareerLord(
                    mahadashaLord,
                    secondLord,
                    sixthLord,
                    tenthLord,
                    eleventhLord
            )) {

                score += 20;

                reasons.add(
                        "Mahadasha lord is connected with a career house"
                );
            }

            // =================================================
            // Antardasha Lord
            // =================================================

            if (isCareerLord(
                    antardashaLord,
                    secondLord,
                    sixthLord,
                    tenthLord,
                    eleventhLord
            )) {

                score += 25;

                reasons.add(
                        "Antardasha lord is connected with a career house"
                );
            }

            // =================================================
            // 10th Lord
            // =================================================

            if (mahadashaLord == tenthLord) {

                score += 15;

                reasons.add(
                        "10th house lord is running Mahadasha"
                );
            }

            if (antardashaLord == tenthLord) {

                score += 20;

                reasons.add(
                        "10th house lord is running Antardasha"
                );
            }

            // =================================================
            // 6th Lord → Job / Service
            // =================================================

            if (mahadashaLord == sixthLord) {

                score += 10;

                reasons.add(
                        "6th house lord supports employment and service"
                );
            }

            if (antardashaLord == sixthLord) {

                score += 15;

                reasons.add(
                        "6th house lord supports employment and service"
                );
            }

            // =================================================
            // 11th Lord → Gains / Income
            // =================================================

            if (mahadashaLord == eleventhLord) {

                score += 10;

                reasons.add(
                        "11th house lord supports gains and income"
                );
            }

            if (antardashaLord == eleventhLord) {

                score += 15;

                reasons.add(
                        "11th house lord supports gains and income"
                );
            }

            // =================================================
            // Strong Job Combination
            // 6th + 10th + 11th
            // =================================================

            boolean mahadashaSupportsJob =
                    mahadashaLord == sixthLord
                            || mahadashaLord == tenthLord
                            || mahadashaLord == eleventhLord;

            boolean antardashaSupportsJob =
                    antardashaLord == sixthLord
                            || antardashaLord == tenthLord
                            || antardashaLord == eleventhLord;

            if (mahadashaSupportsJob && antardashaSupportsJob) {

                score += 20;

                reasons.add(
                        "Mahadasha and Antardasha jointly activate 6th, 10th or 11th house"
                );
            }

               // =================================================
              // Career House Connection Bonus
             // 2nd + 6th + 10th + 11th
            // =================================================

            int careerLordCount = 0;

            if (mahadashaLord == secondLord
                    || mahadashaLord == sixthLord
                    || mahadashaLord == tenthLord
                    || mahadashaLord == eleventhLord) {

                careerLordCount++;
            }

            if (antardashaLord == secondLord
                    || antardashaLord == sixthLord
                    || antardashaLord == tenthLord
                    || antardashaLord == eleventhLord) {

                careerLordCount++;
            }

            if (careerLordCount == 2) {

                score += 15;

                reasons.add(
                        "Both Mahadasha and Antardasha lords are connected with career houses"
                );
            }

            // =================================================
            // Planet's general career score
            // =================================================

            score +=
                    calculatePlanetScore(
                            mahadashaLord
                    ) / 10;

            score +=
                    calculatePlanetScore(
                            antardashaLord
                    ) / 10;

            // =================================================
            // Limit score to 100
            // =================================================

            score =
                    Math.min(score, 100);

            // =================================================
            // Reason
            // =================================================

            String reason;

            if (reasons.isEmpty()) {

                reason =
                        "General planetary career support";

            } else {

                reason =
                        String.join(
                                "; ",
                                reasons
                        );
            }

            result.add(
                    new JobTimingPrediction(
                            mahadashaLord,
                            antardashaLord,
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
    // Check Career Lord
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
    // General Planet Career Score
    // =========================================================

    private int calculatePlanetScore(
            Planet planet
    ) {

        return switch (planet) {

            case SATURN ->
                    90;

            case MERCURY ->
                    90;

            case RAHU ->
                    90;

            case JUPITER ->
                    85;

            case SUN ->
                    75;

            case MARS ->
                    75;

            case VENUS ->
                    70;

            case MOON ->
                    65;

            case KETU ->
                    50;
        };
    }
}