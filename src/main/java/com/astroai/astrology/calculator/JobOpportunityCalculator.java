package com.astroai.astrology.calculator;

import com.astroai.astrology.model.JobOpportunityPrediction;
import com.astroai.astrology.model.JobTimingPrediction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class JobOpportunityCalculator {

    public JobOpportunityPrediction calculate(
            List<JobTimingPrediction> jobTimings,
            JobTimingPrediction currentDasha
    ) {

        // =====================================================
        // 1. Current Score
        // =====================================================

        int currentScore =
                currentDasha != null
                        ? currentDasha.score()
                        : 0;


        // =====================================================
        // 2. Current Status
        // =====================================================

        String currentStatus;

        if (currentScore >= 80) {

            currentStatus = "Very Strong";

        } else if (currentScore >= 65) {

            currentStatus = "Strong";

        } else if (currentScore >= 50) {

            currentStatus = "Moderate";

        } else {

            currentStatus = "Weak";
        }


        // =====================================================
        // 3. Find Best Upcoming Period
        // =====================================================

        JobTimingPrediction bestUpcomingPeriod = null;

        if (jobTimings != null) {

            bestUpcomingPeriod =
                    jobTimings.stream()
                            .filter(period ->
                                    period.endYear()
                                            >= getCurrentYear()
                            )
                            .max(
                                    Comparator
                                            .comparingInt(
                                                    JobTimingPrediction::score
                                            )
                                            .thenComparing(
                                                    JobTimingPrediction::startYear
                                            )
                            )
                            .orElse(null);
        }


        // =====================================================
        // 4. Best Period Description
        // =====================================================

        String bestPeriod;

        if (bestUpcomingPeriod == null) {

            bestPeriod =
                    "No strong upcoming job period identified";

        } else {

            bestPeriod =
                    bestUpcomingPeriod.mahadashaLord()
                            + " - "
                            + bestUpcomingPeriod.antardashaLord()
                            + " : "
                            + formatYear(
                            bestUpcomingPeriod.startYear()
                    )
                            + " to "
                            + formatYear(
                            bestUpcomingPeriod.endYear()
                    )
                            + " (Score "
                            + bestUpcomingPeriod.score()
                            + ")";
        }


        // =====================================================
        // 5. Reasons
        // =====================================================

        List<String> reasons =
                new ArrayList<>();

        reasons.add(
                "Current job opportunity status: "
                        + currentStatus
        );

        if (currentDasha != null) {

            reasons.add(
                    "Current Dasha: "
                            + currentDasha.mahadashaLord()
                            + " - "
                            + currentDasha.antardashaLord()
            );

            reasons.add(
                    currentDasha.reason()
            );
        }

        if (bestUpcomingPeriod != null) {

            reasons.add(
                    "Strongest upcoming period identified with score "
                            + bestUpcomingPeriod.score()
            );

            reasons.add(
                    bestUpcomingPeriod.reason()
            );
        }


        // =====================================================
        // 6. Final Result
        // =====================================================

        return new JobOpportunityPrediction(
                currentScore,
                currentStatus,
                bestPeriod,
                reasons
        );
    }


    // =========================================================
    // Current Year
    // =========================================================

    private double getCurrentYear() {

        return java.time.LocalDate.now()
                .getYear();
    }


    // =========================================================
    // Format Year
    // =========================================================

    private String formatYear(
            double year
    ) {

        return String.format(
                "%.2f",
                year
        );
    }
}