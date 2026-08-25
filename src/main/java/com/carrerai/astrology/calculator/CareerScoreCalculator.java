package com.carrerai.astrology.calculator;

import com.carrerai.astrology.model.CareerHouseAnalysis;
import com.carrerai.astrology.model.CareerScore;
import com.carrerai.astrology.model.Planet;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CareerScoreCalculator {

    public CareerScore calculate(
            List<CareerHouseAnalysis> analysis
    ) {

        int incomeScore = calculateHouseScore(
                findHouse(analysis, 2)
        );

        int jobScore = calculateHouseScore(
                findHouse(analysis, 6)
        );

        int professionScore = calculateHouseScore(
                findHouse(analysis, 10)
        );

        int growthScore = calculateHouseScore(
                findHouse(analysis, 11)
        );

        int overallScore =
                (incomeScore
                        + jobScore
                        + professionScore
                        + growthScore) / 4;

        return new CareerScore(
                incomeScore,
                jobScore,
                professionScore,
                growthScore,
                overallScore
        );
    }

    private CareerHouseAnalysis findHouse(
            List<CareerHouseAnalysis> analysis,
            int house
    ) {

        return analysis.stream()
                .filter(a -> a.house() == house)
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                "Career house not found: " + house
                        )
                );
    }

    private int calculateHouseScore(
            CareerHouseAnalysis analysis
    ) {

        int score = 50;

        // Lord placed in a favourable career-related
        // house gives additional weight.
        if (isFavourableLordHouse(
                analysis.lordHouse()
        )) {
            score += 20;
        }

        // Planets occupying the house add influence.
        score += analysis.planetsInHouse().size() * 5;

        return Math.min(score, 100);
    }

    private boolean isFavourableLordHouse(
            int house
    ) {

        return house == 1
                || house == 5
                || house == 9
                || house == 10
                || house == 11;
    }
}