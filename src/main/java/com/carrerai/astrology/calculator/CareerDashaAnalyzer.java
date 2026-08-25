package com.carrerai.astrology.calculator;

import com.carrerai.astrology.model.DashaPeriod;
import com.carrerai.astrology.model.HouseLordPosition;
import com.carrerai.astrology.model.Planet;
import com.carrerai.astrology.model.PlanetHousePosition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CareerDashaAnalyzer {

    // =========================================================
    // Career related houses
    // =========================================================

    private static final int CAREER_HOUSE = 10;
    private static final int SERVICE_HOUSE = 6;
    private static final int INCOME_HOUSE = 11;
    private static final int SELF_HOUSE = 1;

    // =========================================================
    // Analyze Mahadasha for Career
    // =========================================================

    public List<String> analyzeCareerDashas(
            List<DashaPeriod> dashas,
            List<PlanetHousePosition> planetHousePositions,
            List<HouseLordPosition> houseLordPositions
    ) {

        List<String> results = new ArrayList<>();

        for (DashaPeriod dasha : dashas) {

            Planet planet = dasha.planet();

            int score = 0;

            // -------------------------------------------------
            // 1. Check where Dasha planet is placed
            // -------------------------------------------------

            PlanetHousePosition planetPosition =
                    findPlanetPosition(
                            planet,
                            planetHousePositions
                    );

            if (planetPosition != null) {

                int house = planetPosition.house();

                if (house == CAREER_HOUSE) {
                    score += 5;
                }

                if (house == SERVICE_HOUSE) {
                    score += 4;
                }

                if (house == INCOME_HOUSE) {
                    score += 4;
                }

                if (house == SELF_HOUSE) {
                    score += 2;
                }
            }

            // -------------------------------------------------
            // 2. Check whether planet is lord of career houses
            // -------------------------------------------------

            if (isLordOfHouse(
                    planet,
                    CAREER_HOUSE,
                    houseLordPositions
            )) {
                score += 5;
            }

            if (isLordOfHouse(
                    planet,
                    SERVICE_HOUSE,
                    houseLordPositions
            )) {
                score += 4;
            }

            if (isLordOfHouse(
                    planet,
                    INCOME_HOUSE,
                    houseLordPositions
            )) {
                score += 4;
            }

            // -------------------------------------------------
            // 3. Career result
            // -------------------------------------------------

            if (score >= 9) {

                results.add(
                        planet + " Mahadasha (" +
                                dasha.startYear() +
                                " - " +
                                dasha.endYear() +
                                ") : STRONG CAREER PERIOD"
                );

            } else if (score >= 5) {

                results.add(
                        planet + " Mahadasha (" +
                                dasha.startYear() +
                                " - " +
                                dasha.endYear() +
                                ") : GOOD CAREER PERIOD"
                );

            } else if (score >= 2) {

                results.add(
                        planet + " Mahadasha (" +
                                dasha.startYear() +
                                " - " +
                                dasha.endYear() +
                                ") : MODERATE CAREER PERIOD"
                );
            }
        }

        return results;
    }

    // =========================================================
    // Find planet position
    // =========================================================

    private PlanetHousePosition findPlanetPosition(
            Planet planet,
            List<PlanetHousePosition> positions
    ) {

        for (PlanetHousePosition position : positions) {

            if (position.planet() == planet) {
                return position;
            }
        }

        return null;
    }

    // =========================================================
    // Check whether planet is lord of a house
    // =========================================================

    private boolean isLordOfHouse(
            Planet planet,
            int house,
            List<HouseLordPosition> houseLordPositions
    ) {

        for (HouseLordPosition position : houseLordPositions) {

            if (position.house() == house &&
                    position.lord() == planet) {

                return true;
            }
        }

        return false;
    }
}