package com.carrerai.astrology.calculator;

import com.carrerai.astrology.model.Planet;
import org.springframework.stereotype.Component;
import swisseph.SweConst;

@Component
public class TransitCalculator {

    private final SwissEphemerisCalculator calculator;
    private final AyanamsaCalculator ayanamsaCalculator;

    public TransitCalculator(
            SwissEphemerisCalculator calculator,
            AyanamsaCalculator ayanamsaCalculator
    ) {
        this.calculator = calculator;
        this.ayanamsaCalculator = ayanamsaCalculator;
    }
    private int findTransitHouse(
            double planetLongitude,
            double[] siderealHouses
    ) {

        for (int house = 1; house <= 12; house++) {

            double start = siderealHouses[house];

            double end;

            if (house == 12) {
                end = siderealHouses[1] + 360.0;
            } else {
                end = siderealHouses[house + 1];
            }

            double longitude = planetLongitude;

            if (longitude < start) {
                longitude += 360.0;
            }

            if (longitude >= start && longitude < end) {
                return house;
            }
        }

        return 12;
    }

    public boolean activatesCareerHouse(int house) {

        return house == 2
                || house == 6
                || house == 10
                || house == 11;
    }

    public int calculateCareerTransitScore(
            Planet planet,
            int year,
            int month,
            int day,
            double hour,
            double[] siderealHouses
    ) {

        double longitude =
                calculateSiderealLongitude(
                        planet,
                        year,
                        month,
                        day,
                        hour
                );

        int house =
                findTransitHouse(
                        longitude,
                        siderealHouses
                );

        if (!activatesCareerHouse(house)) {
            return 0;
        }

        return switch (planet) {

            case JUPITER -> 25;

            case SATURN -> 20;

            case RAHU, KETU -> 15;

            default -> 5;
        };
    }

    public boolean hasVedicAspect(
            Planet planet,
            int transitHouse,
            int targetHouse
    ) {

        int distance =
                ((targetHouse - transitHouse + 12) % 12) + 1;

        // Every planet has 7th aspect
        if (distance == 7) {
            return true;
        }

        // Mars: 4th and 8th aspects
        if (planet == Planet.MARS
                && (distance == 4 || distance == 8)) {
            return true;
        }

        // Jupiter: 5th and 9th aspects
        if (planet == Planet.JUPITER
                && (distance == 5 || distance == 9)) {
            return true;
        }

        // Saturn: 3rd and 10th aspects
        if (planet == Planet.SATURN
                && (distance == 3 || distance == 10)) {
            return true;
        }

        return false;
    }

    private double calculateSiderealLongitude(
            Planet planet,
            int year,
            int month,
            int day,
            double hour
    ) {

        double[] result;

        if (planet == Planet.RAHU || planet == Planet.KETU) {

            result = calculator.calculatePlanet(
                    year,
                    month,
                    day,
                    hour,
                    SweConst.SE_MEAN_NODE
            );

        } else {

            int planetId = getPlanetId(planet);

            result = calculator.calculatePlanet(
                    year,
                    month,
                    day,
                    hour,
                    planetId
            );
        }

        double tropicalLongitude = result[0];

        // Ketu is exactly opposite Rahu
        if (planet == Planet.KETU) {

            tropicalLongitude += 180.0;

            if (tropicalLongitude >= 360.0) {
                tropicalLongitude -= 360.0;
            }
        }

        double ayanamsa =
                ayanamsaCalculator.calculateLahiriAyanamsa(
                        year,
                        month,
                        day,
                        hour
                );

        double siderealLongitude =
                tropicalLongitude - ayanamsa;

        if (siderealLongitude < 0) {
            siderealLongitude += 360.0;
        }

        return siderealLongitude;
    }
    private int getPlanetId(Planet planet) {

        return switch (planet) {

            case SUN -> SweConst.SE_SUN;
            case MOON -> SweConst.SE_MOON;
            case MERCURY -> SweConst.SE_MERCURY;
            case VENUS -> SweConst.SE_VENUS;
            case MARS -> SweConst.SE_MARS;
            case JUPITER -> SweConst.SE_JUPITER;
            case SATURN -> SweConst.SE_SATURN;

            case RAHU, KETU ->
                    throw new IllegalArgumentException(
                            "Rahu and Ketu are handled separately"
                    );
        };
    }

    private int findHouse(
            double planetLongitude,
            double[] houses
    ) {

        for (int house = 1; house <= 12; house++) {

            double start = houses[house];

            double end;

            if (house == 12) {
                end = houses[1] + 360.0;
            } else {
                end = houses[house + 1];
            }

            double longitude = planetLongitude;

            if (longitude < start) {
                longitude += 360.0;
            }

            if (longitude >= start && longitude < end) {
                return house;
            }
        }

        return 12;
    }

    public int calculateTransitHouse(
            Planet planet,
            int year,
            int month,
            int day,
            double hour,
            double latitude,
            double longitude,
            double[] natalHouses
    ) {

        double transitLongitude =
                calculateSiderealLongitude(
                        planet,
                        year,
                        month,
                        day,
                        hour
                );

        return findHouse(
                transitLongitude,
                natalHouses
        );
    }


}