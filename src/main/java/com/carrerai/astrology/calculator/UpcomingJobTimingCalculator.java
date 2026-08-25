package com.carrerai.astrology.calculator;

import com.carrerai.astrology.model.JobTimingPrediction;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Component
public class UpcomingJobTimingCalculator {

    public List<JobTimingPrediction> findUpcomingBestTimings(
            List<JobTimingPrediction> jobTimings
    ) {

        double currentYear = calculateCurrentYear();

        return jobTimings.stream()
                .filter(period ->
                        period.endYear() > currentYear
                )
                .filter(period ->
                        period.score() >= 70
                )
                .sorted(
                        Comparator.comparingDouble(
                                JobTimingPrediction::startYear
                        )
                )
                .limit(5)
                .toList();
    }

    private double calculateCurrentYear() {

        LocalDate today = LocalDate.now();

        int year = today.getYear();
        int dayOfYear = today.getDayOfYear();
        int totalDays = today.lengthOfYear();

        return year +
                ((double) (dayOfYear - 1) / totalDays);
    }
}