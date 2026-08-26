package com.astroai.career.calculator;

import com.astroai.astrology.model.CareerForecastPeriod;
import com.astroai.astrology.model.JobTimingPrediction;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Component
public class CareerForecastScoringCalculator {

    // =========================================================
    // SCORE FORECAST PERIOD
    // =========================================================

    public CareerForecastPeriod scorePeriod(
            LocalDate startDate,
            LocalDate endDate,
            String periodType,
            List<JobTimingPrediction> jobTimings
    ) {

        // -----------------------------------------------------
        // Find Job Timing periods overlapping this forecast
        // -----------------------------------------------------

        List<JobTimingPrediction> overlappingPeriods =
                jobTimings.stream()
                        .filter(
                                timing ->
                                        overlaps(
                                                startDate,
                                                endDate,
                                                timing
                                        )
                        )
                        .sorted(
                                Comparator.comparingInt(
                                        JobTimingPrediction::score
                                ).reversed()
                        )
                        .toList();


        // -----------------------------------------------------
        // No Dasha overlap
        // -----------------------------------------------------

        if (overlappingPeriods.isEmpty()) {

            return new CareerForecastPeriod(
                    startDate,
                    endDate,
                    periodType,
                    0,
                    "NEUTRAL",
                    "No strong Dasha-based career activation in this period.",
                    List.of(),
                    List.of()
            );
        }


        // -----------------------------------------------------
        // Strongest overlapping career period
        // -----------------------------------------------------

        JobTimingPrediction strongest =
                overlappingPeriods.get(0);


        int score =
                strongest.score();


        String status =
                determineStatus(score);


        String reason =
                buildReason(
                        strongest,
                        overlappingPeriods
                );


        // -----------------------------------------------------
        // Forecast Period
        // -----------------------------------------------------

        return new CareerForecastPeriod(
                startDate,
                endDate,
                periodType,
                score,
                status,
                reason,
                List.of(),
                List.of()
        );
    }


    // =========================================================
    // CHECK DATE OVERLAP
    // =========================================================

    private boolean overlaps(
            LocalDate forecastStart,
            LocalDate forecastEnd,
            JobTimingPrediction timing
    ) {

        LocalDate timingStart =
                decimalYearToDate(
                        timing.startYear()
                );

        LocalDate timingEnd =
                decimalYearToDate(
                        timing.endYear()
                );


        return !timingEnd.isBefore(forecastStart)
                && !timingStart.isAfter(forecastEnd);
    }


    // =========================================================
    // BUILD DYNAMIC REASON
    // =========================================================

    private String buildReason(
            JobTimingPrediction strongest,
            List<JobTimingPrediction> overlappingPeriods
    ) {

        StringBuilder reason =
                new StringBuilder(
                        strongest.reason()
                );


        /*
         * If more than one Dasha-based career period
         * overlaps the forecast window, mention that
         * additional support exists.
         */

        if (overlappingPeriods.size() > 1) {

            reason.append(
                    "; Multiple career-supporting Dasha periods overlap this window"
            );
        }


        return reason.toString();
    }


    // =========================================================
    // DETERMINE STATUS
    // =========================================================

    private String determineStatus(
            int score
    ) {

        if (score >= 80) {
            return "VERY_STRONG";
        }

        if (score >= 70) {
            return "STRONG";
        }

        if (score >= 60) {
            return "FAVORABLE";
        }

        if (score >= 40) {
            return "MODERATE";
        }

        return "LOW";
    }


    // =========================================================
    // DECIMAL YEAR → LOCAL DATE
    // =========================================================

    private LocalDate decimalYearToDate(
            double decimalYear
    ) {

        int year =
                (int) Math.floor(decimalYear);

        double fraction =
                decimalYear - year;


        /*
         * Number of days in this year.
         */

        int daysInYear =
                LocalDate.of(
                        year,
                        12,
                        31
                ).getDayOfYear();


        /*
         * Convert fractional year into elapsed days.
         */

        long elapsedDays =
                Math.round(
                        fraction * daysInYear
                );


        /*
         * Prevent overflow into next year.
         */

        elapsedDays =
                Math.min(
                        elapsedDays,
                        daysInYear - 1
                );


        return LocalDate.of(
                        year,
                        1,
                        1
                )
                .plusDays(elapsedDays);
    }
}