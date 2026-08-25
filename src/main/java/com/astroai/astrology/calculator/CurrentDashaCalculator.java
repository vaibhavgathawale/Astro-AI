package com.astroai.astrology.calculator;

import com.astroai.astrology.model.JobTimingPrediction;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class CurrentDashaCalculator {

    public List<JobTimingPrediction> findCurrentDasha(
            List<JobTimingPrediction> jobTimings
    ) {

        double currentYear = calculateCurrentYear();

        return jobTimings.stream()
                .filter(period ->
                        currentYear >= period.startYear()
                                && currentYear < period.endYear()
                )
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