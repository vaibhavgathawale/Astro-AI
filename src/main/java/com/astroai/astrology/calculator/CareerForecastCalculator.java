package com.astroai.astrology.calculator;

import com.astroai.astrology.model.CareerForecast;
import com.astroai.astrology.model.CareerForecastPeriod;
import com.astroai.astrology.model.JobTimingPrediction;
import com.astroai.career.calculator.CareerForecastScoringCalculator;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Component
public class CareerForecastCalculator {

    private final CareerForecastScoringCalculator scoringCalculator;

    public CareerForecastCalculator(CareerForecastScoringCalculator scoringCalculator) {
        this.scoringCalculator = scoringCalculator;
    }

    // =========================================================
// GENERATE COMPLETE CAREER FORECAST
// =========================================================

    public CareerForecast generateForecast(
            List<JobTimingPrediction> jobTimings
    ) {

        LocalDate today = LocalDate.now();

        List<CareerForecastPeriod> periods =
                new ArrayList<>();

        // =====================================================
        // 1. CURRENT MONTH - DAY WISE
        // =====================================================

        LocalDate currentMonthEnd =
                today.withDayOfMonth(
                        today.lengthOfMonth()
                );

        LocalDate currentDate = today;

        while (!currentDate.isAfter(currentMonthEnd)) {

            periods.add(
                    scorePeriod(
                            currentDate,
                            currentDate,
                            "DAY_WISE",
                            jobTimings
                    )
            );

            currentDate =
                    currentDate.plusDays(1);
        }


        // =====================================================
        // 2. NEXT 11 MONTHS - MONTH WISE
        // =====================================================

        YearMonth currentMonth =
                YearMonth.from(today);

        for (int i = 1; i <= 11; i++) {

            YearMonth month =
                    currentMonth.plusMonths(i);

            LocalDate startDate =
                    month.atDay(1);

            LocalDate endDate =
                    month.atEndOfMonth();

            periods.add(
                    scorePeriod(
                            startDate,
                            endDate,
                            "MONTH_WISE",
                            jobTimings
                    )
            );
        }


        // =====================================================
        // 3. COMPLETE FORECAST WINDOW
        // =====================================================

        LocalDate forecastStartDate =
                today;

        LocalDate forecastEndDate =
                currentMonth
                        .plusMonths(11)
                        .atEndOfMonth();


        return new CareerForecast(
                forecastStartDate,
                forecastEndDate,
                periods
        );
    }

    // =========================================================
    // SCORE A FORECAST WINDOW
    // =========================================================

    public CareerForecastPeriod scorePeriod(
            LocalDate startDate,
            LocalDate endDate,
            String periodType,
            List<JobTimingPrediction> jobTimings
    ) {

        List<JobTimingPrediction> overlappingPeriods =
                findOverlappingPeriods(
                        startDate,
                        endDate,
                        jobTimings
                );

        // -----------------------------------------------------
        // No Dasha period found
        // -----------------------------------------------------

        if (overlappingPeriods.isEmpty()) {

            return new CareerForecastPeriod(
                    startDate,
                    endDate,
                    periodType,
                    0,
                    "WEAK",
                    "No active career-supporting Dasha period identified",
                    List.of(),
                    List.of()
            );
        }

        // -----------------------------------------------------
        // Strongest overlapping period
        // -----------------------------------------------------

        JobTimingPrediction strongestPeriod =
                overlappingPeriods.stream()
                        .max(
                                Comparator.comparingInt(
                                        JobTimingPrediction::score
                                )
                        )
                        .orElseThrow();

        int score =
                strongestPeriod.score();

        String status =
                calculateStatus(score);

        // -----------------------------------------------------
        // Dynamic reason
        // -----------------------------------------------------

        List<String> reasons =
                new ArrayList<>();

        overlappingPeriods.stream()
                .sorted(
                        Comparator.comparingInt(
                                JobTimingPrediction::score
                        ).reversed()
                )
                .limit(3)
                .forEach(
                        period ->
                                reasons.add(
                                        period.reason()
                                )
                );

        if (overlappingPeriods.size() > 1) {

            reasons.add(
                    "Multiple career-supporting Dasha periods overlap this window"
            );
        }

        String reason =
                reasons.stream()
                        .distinct()
                        .reduce(
                                (a, b) -> a + "; " + b
                        )
                        .orElse(
                                "General planetary career support"
                        );

        // -----------------------------------------------------
        // Important events
        // -----------------------------------------------------

        List<String> importantEvents =
                generateImportantEvents(
                        score
                );

        // -----------------------------------------------------
        // Remedies
        // -----------------------------------------------------

        List<String> remedies =
                generateRemedies(
                        score
                );

        return new CareerForecastPeriod(
                startDate,
                endDate,
                periodType,
                score,
                status,
                reason,
                importantEvents,
                remedies
        );
    }


    // =========================================================
    // FIND OVERLAPPING DASHA PERIODS
    // =========================================================

    private List<JobTimingPrediction> findOverlappingPeriods(
            LocalDate startDate,
            LocalDate endDate,
            List<JobTimingPrediction> jobTimings
    ) {

        List<JobTimingPrediction> result =
                new ArrayList<>();

        if (jobTimings == null ||
                jobTimings.isEmpty()) {

            return result;
        }

        double windowStart =
                toDecimalYear(startDate);

        double windowEnd =
                toDecimalYear(
                        endDate.plusDays(1)
                );

        for (JobTimingPrediction period :
                jobTimings) {

            /*
             * Overlap condition:
             *
             * period.start < windowEnd
             * AND
             * period.end > windowStart
             */

            if (period.startYear() < windowEnd &&
                    period.endYear() > windowStart) {

                result.add(period);
            }
        }

        return result;
    }


    // =========================================================
    // LOCAL DATE → DECIMAL YEAR
    // =========================================================

    private double toDecimalYear(
            LocalDate date
    ) {

        int year =
                date.getYear();

        LocalDate startOfYear =
                LocalDate.of(
                        year,
                        1,
                        1
                );

        LocalDate startOfNextYear =
                LocalDate.of(
                        year + 1,
                        1,
                        1
                );

        long elapsedDays =
                ChronoUnit.DAYS.between(
                        startOfYear,
                        date
                );

        long totalDays =
                ChronoUnit.DAYS.between(
                        startOfYear,
                        startOfNextYear
                );

        return year +
                ((double) elapsedDays / totalDays);
    }


    // =========================================================
    // STATUS
    // =========================================================

    private String calculateStatus(
            int score
    ) {

        if (score >= 80) {
            return "VERY_STRONG";
        }

        if (score >= 65) {
            return "STRONG";
        }

        if (score >= 50) {
            return "MODERATE";
        }

        return "WEAK";
    }


    // =========================================================
    // IMPORTANT EVENTS
    // =========================================================

    private List<String> generateImportantEvents(
            int score
    ) {

        if (score >= 80) {

            return List.of(
                    "Favorable period for job opportunities",
                    "Suitable for interviews and career initiatives"
            );
        }

        if (score >= 65) {

            return List.of(
                    "Good period for job search and professional progress"
            );
        }

        if (score >= 50) {

            return List.of(
                    "Continue job search with consistent effort"
            );
        }

        return List.of(
                "Avoid relying only on timing; maintain consistent effort"
        );
    }


    // =========================================================
    // REMEDIES
    // =========================================================

    private List<String> generateRemedies(
            int score
    ) {

        if (score >= 80) {

            return List.of();
        }

        if (score >= 65) {

            return List.of(
                    "Maintain consistency in career efforts"
            );
        }

        if (score >= 50) {

            return List.of(
                    "Increase networking and interview preparation"
            );
        }

        return List.of(
                "Focus on preparation, skill development and persistence"
        );
    }



}