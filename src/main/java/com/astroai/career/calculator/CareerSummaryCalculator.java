package com.astroai.career.calculator;

import com.astroai.astrology.model.*;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class CareerSummaryCalculator {

    // =========================================================
    // CREATE SHORT CAREER SUMMARY
    // =========================================================

    public CareerSummary calculate(
            CareerScore careerScore,
            List<CareerFieldScore> careerFields,
            List<JobTimingPrediction> jobTimings,
            JobTimingPrediction currentDasha
    ) {

        // =====================================================
        // 1. Keep only favourable periods
        // =====================================================

        List<JobTimingPrediction> favourablePeriods =
                jobTimings.stream()

                        .filter(
                                prediction ->
                                        prediction.score() >= 70
                        )

                        .sorted(
                                Comparator.comparing(
                                        JobTimingPrediction::score
                                ).reversed()
                        )

                        .limit(10)

                        .toList();


        // =====================================================
        // 2. Generate career conclusion
        // =====================================================

        String conclusion =
                generateConclusion(
                        careerScore,
                        careerFields,
                        currentDasha,
                        favourablePeriods
                );


        // =====================================================
        // 3. Return short summary
        // =====================================================

        return new CareerSummary(
                careerScore,
                careerFields,
                currentDasha,
                favourablePeriods,
                conclusion
        );
    }


    // =========================================================
    // GENERATE CONCLUSION
    // =========================================================

    private String generateConclusion(
            CareerScore careerScore,
            List<CareerFieldScore> careerFields,
            JobTimingPrediction currentDasha,
            List<JobTimingPrediction> favourablePeriods
    ) {

        int overallScore =
                careerScore.overallScore();


        String careerStatus;

        if (overallScore >= 80) {

            careerStatus =
                    "Very strong career potential.";

        } else if (overallScore >= 65) {

            careerStatus =
                    "Good career potential with steady growth.";

        } else if (overallScore >= 50) {

            careerStatus =
                    "Moderate career potential. Progress may require patience and consistent effort.";

        } else {

            careerStatus =
                    "Career growth may require additional effort and careful planning.";
        }


        // =====================================================
        // Current Dasha
        // =====================================================

        String dashaMessage = "";

        if (currentDasha != null) {

            dashaMessage =
                    " Current period is "
                            + currentDasha.mahadashaLord()
                            + " Mahadasha / "
                            + currentDasha.antardashaLord()
                            + " Antardasha with score "
                            + currentDasha.score()
                            + ".";
        }


        // =====================================================
        // Favourable periods
        // =====================================================

        String timingMessage;

        if (!favourablePeriods.isEmpty()) {

            int highestScore =
                    favourablePeriods.stream()
                            .mapToInt(
                                    JobTimingPrediction::score
                            )
                            .max()
                            .orElse(0);

            timingMessage =
                    " The analysis identifies favourable career periods, with the strongest period scoring "
                            + highestScore
                            + ".";

        } else {

            timingMessage =
                    " No strongly favourable period was identified in the analysed timeline.";
        }


        return careerStatus
                + dashaMessage
                + timingMessage;
    }
}