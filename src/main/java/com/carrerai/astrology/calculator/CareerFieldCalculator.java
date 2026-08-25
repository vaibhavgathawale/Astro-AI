package com.carrerai.astrology.calculator;

import com.carrerai.astrology.model.CareerFieldScore;
import com.carrerai.astrology.model.HouseLordPosition;
import com.carrerai.astrology.model.Planet;
import com.carrerai.astrology.model.PlanetHousePosition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CareerFieldCalculator {

    public List<CareerFieldScore> calculate(
            List<PlanetHousePosition> planetHousePositions,
            List<HouseLordPosition> houseLordPositions
    ) {

        List<CareerFieldScore> result =
                new ArrayList<>();

        int technologyScore = 40;
        int financeScore = 40;
        int managementScore = 40;
        int governmentScore = 40;
        int researchScore = 40;

        // =====================================================
        // 1. Planets influencing the 10th house
        // =====================================================

        List<Planet> planetsIn10thHouse =
                planetHousePositions.stream()
                        .filter(position -> position.house() == 10)
                        .map(PlanetHousePosition::planet)
                        .toList();

        // =====================================================
        // 2. Mercury
        // =====================================================

        if (planetsIn10thHouse.contains(Planet.MERCURY)) {

            technologyScore += 25;
            financeScore += 5;

            result.add(
                    new CareerFieldScore(
                            "Technology / IT",
                            technologyScore,
                            "Mercury is placed in the 10th house"
                    )
            );
        }

        // =====================================================
        // 3. Saturn
        // =====================================================

        if (planetsIn10thHouse.contains(Planet.SATURN)) {

            technologyScore += 15;
            managementScore += 10;
            governmentScore += 10;

            result.add(
                    new CareerFieldScore(
                            "Engineering / Technology",
                            technologyScore,
                            "Saturn is placed in the 10th house"
                    )
            );
        }

        // =====================================================
        // 4. Jupiter
        // =====================================================

        if (planetsIn10thHouse.contains(Planet.JUPITER)) {

            financeScore += 20;
            managementScore += 15;

            result.add(
                    new CareerFieldScore(
                            "Finance / Advisory",
                            financeScore,
                            "Jupiter is placed in the 10th house"
                    )
            );
        }

        // =====================================================
        // 5. Sun
        // =====================================================

        if (planetsIn10thHouse.contains(Planet.SUN)) {

            governmentScore += 25;
            managementScore += 20;

            result.add(
                    new CareerFieldScore(
                            "Government / Leadership",
                            governmentScore,
                            "Sun is placed in the 10th house"
                    )
            );
        }

        // =====================================================
        // 6. Mars
        // =====================================================

        if (planetsIn10thHouse.contains(Planet.MARS)) {

            technologyScore += 15;

            result.add(
                    new CareerFieldScore(
                            "Engineering / Technical",
                            technologyScore,
                            "Mars is placed in the 10th house"
                    )
            );
        }

        // =====================================================
        // 7. Rahu
        // =====================================================

        if (planetsIn10thHouse.contains(Planet.RAHU)) {

            technologyScore += 20;

            result.add(
                    new CareerFieldScore(
                            "Technology / Foreign Sector",
                            technologyScore,
                            "Rahu is placed in the 10th house"
                    )
            );
        }

        // =====================================================
        // 8. Venus
        // =====================================================

        if (planetsIn10thHouse.contains(Planet.VENUS)) {

            financeScore += 10;
            managementScore += 10;

            result.add(
                    new CareerFieldScore(
                            "Business / Creative",
                            managementScore,
                            "Venus is placed in the 10th house"
                    )
            );
        }

        // =====================================================
        // 9. Ketu
        // =====================================================

        if (planetsIn10thHouse.contains(Planet.KETU)) {

            researchScore += 20;

            result.add(
                    new CareerFieldScore(
                            "Research / Analysis",
                            researchScore,
                            "Ketu is placed in the 10th house"
                    )
            );
        }

        // =====================================================
        // 10. 10th Lord Analysis
        // =====================================================

        HouseLordPosition tenthLord =
                findHouseLord(
                        houseLordPositions,
                        10
                );

        if (tenthLord != null) {

            Planet lord =
                    tenthLord.lord();

            int lordHouse =
                    tenthLord.lordHouse();

            if (lord == Planet.MERCURY) {

                technologyScore += 15;

                result.add(
                        new CareerFieldScore(
                                "Technology / Communication",
                                technologyScore,
                                "10th house lord is Mercury and is placed in house "
                                        + lordHouse
                        )
                );
            }

            if (lord == Planet.SATURN) {

                technologyScore += 15;
                managementScore += 10;

                result.add(
                        new CareerFieldScore(
                                "Engineering / Administration",
                                technologyScore,
                                "10th house lord is Saturn and is placed in house "
                                        + lordHouse
                        )
                );
            }

            if (lord == Planet.JUPITER) {

                financeScore += 15;
                managementScore += 15;

                result.add(
                        new CareerFieldScore(
                                "Finance / Advisory / Management",
                                financeScore,
                                "10th house lord is Jupiter and is placed in house "
                                        + lordHouse
                        )
                );
            }

            if (lord == Planet.SUN) {

                governmentScore += 20;
                managementScore += 15;

                result.add(
                        new CareerFieldScore(
                                "Government / Leadership",
                                governmentScore,
                                "10th house lord is Sun and is placed in house "
                                        + lordHouse
                        )
                );
            }

            if (lord == Planet.MARS) {

                technologyScore += 15;

                result.add(
                        new CareerFieldScore(
                                "Engineering / Technical",
                                technologyScore,
                                "10th house lord is Mars and is placed in house "
                                        + lordHouse
                        )
                );
            }

            if (lord == Planet.VENUS) {

                financeScore += 10;
                managementScore += 10;

                result.add(
                        new CareerFieldScore(
                                "Business / Finance / Creative",
                                financeScore,
                                "10th house lord is Venus and is placed in house "
                                        + lordHouse
                        )
                );
            }
        }

        // =====================================================
        // 11. 6th House Lord → Job / Service
        // =====================================================

        HouseLordPosition sixthLord =
                findHouseLord(
                        houseLordPositions,
                        6
                );

        if (sixthLord != null) {

            if (sixthLord.lord() == Planet.SATURN ||
                    sixthLord.lord() == Planet.MERCURY ||
                    sixthLord.lord() == Planet.MARS) {

                technologyScore += 10;

                result.add(
                        new CareerFieldScore(
                                "Technical / Service",
                                technologyScore,
                                "6th house lord supports technical service-oriented work"
                        )
                );
            }
        }

        // =====================================================
        // 12. 11th House Lord → Income / Gains
        // =====================================================

        HouseLordPosition eleventhLord =
                findHouseLord(
                        houseLordPositions,
                        11
                );

        if (eleventhLord != null) {

            if (eleventhLord.lord() == Planet.JUPITER ||
                    eleventhLord.lord() == Planet.VENUS ||
                    eleventhLord.lord() == Planet.MERCURY) {

                financeScore += 10;

                result.add(
                        new CareerFieldScore(
                                "Finance / Business",
                                financeScore,
                                "11th house lord supports income and business-related fields"
                        )
                );
            }
        }

        return result;
    }

    private HouseLordPosition findHouseLord(
            List<HouseLordPosition> houseLordPositions,
            int house
    ) {

        return houseLordPositions.stream()
                .filter(position -> position.house() == house)
                .findFirst()
                .orElse(null);
    }
}