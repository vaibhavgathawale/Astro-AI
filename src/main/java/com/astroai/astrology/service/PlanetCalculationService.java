package com.astroai.astrology.service;

import com.astroai.astrology.calculator.*;
import com.astroai.astrology.model.*;
import org.springframework.stereotype.Service;
import swisseph.SweConst;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlanetCalculationService {

    private final SwissEphemerisCalculator calculator;
    private final AyanamsaCalculator ayanamsaCalculator;
    private final SiderealCalculator siderealCalculator;
    private final NakshatraCalculator nakshatraCalculator;
    private final HouseCalculator houseCalculator;
    private final PlanetHouseCalculator planetHouseCalculator;
    private final HouseLordCalculator houseLordCalculator;
    private final CareerScoreCalculator careerScoreCalculator;
    private final CareerFieldCalculator careerFieldCalculator;
    private final CareerIndicatorCalculator careerIndicatorCalculator;
    private final VimshottariDashaCalculator vimshottariDashaCalculator;
    private final JobTimingCalculator jobTimingCalculator;
    private final AntardashaCalculator antardashaCalculator;
    private final CurrentDashaCalculator currentDashaCalculator;
    private final PratyantardashaCalculator pratyantardashaCalculator;
    private final PratyantardashaCareerCalculator pratyantardashaCareerCalculator;
    private final CareerConclusionCalculator careerConclusionCalculator;
    private final JobOpportunityCalculator jobOpportunityCalculator;

    




    public PlanetCalculationService(
            SwissEphemerisCalculator calculator,
            AyanamsaCalculator ayanamsaCalculator,
            SiderealCalculator siderealCalculator,
            NakshatraCalculator nakshatraCalculator,
            HouseCalculator houseCalculator,
            PlanetHouseCalculator planetHouseCalculator,
            HouseLordCalculator houseLordCalculator,
            CareerScoreCalculator careerScoreCalculator,
            CareerFieldCalculator careerFieldCalculator,
            CareerIndicatorCalculator careerIndicatorCalculator,
            VimshottariDashaCalculator vimshottariDashaCalculator,
            JobTimingCalculator jobTimingCalculator,
            AntardashaCalculator antardashaCalculator,
            CurrentDashaCalculator currentDashaCalculator,
            PratyantardashaCalculator pratyantardashaCalculator,
            PratyantardashaCareerCalculator pratyantardashaCareerCalculator,
            CareerConclusionCalculator careerConclusionCalculator, JobOpportunityCalculator jobOpportunityCalculator

    ) {

        this.calculator = calculator;
        this.ayanamsaCalculator = ayanamsaCalculator;
        this.siderealCalculator = siderealCalculator;
        this.nakshatraCalculator = nakshatraCalculator;
        this.houseCalculator = houseCalculator;
        this.planetHouseCalculator = planetHouseCalculator;
        this.houseLordCalculator = houseLordCalculator;
        this.careerScoreCalculator = careerScoreCalculator;
        this.careerFieldCalculator = careerFieldCalculator;
        this.careerIndicatorCalculator = careerIndicatorCalculator;
        this.vimshottariDashaCalculator = vimshottariDashaCalculator;
        this.jobTimingCalculator = jobTimingCalculator;
        this.antardashaCalculator = antardashaCalculator;
        this.currentDashaCalculator = currentDashaCalculator;
        this.pratyantardashaCalculator = pratyantardashaCalculator;
        this.pratyantardashaCareerCalculator = pratyantardashaCareerCalculator;
        this.careerConclusionCalculator = careerConclusionCalculator;
        this.jobOpportunityCalculator = jobOpportunityCalculator;
    }


    // =========================================================
    // 1. SINGLE PLANET - SIDEREAL POSITION
    // =========================================================

    public SiderealPlanetPosition calculateSiderealPosition(
            Planet planet,
            int year,
            int month,
            int day,
            double hour
    ) {

        double[] result;

        /*
         * Rahu and Ketu are calculated separately
         * because they are lunar nodes.
         */

        if (planet == Planet.RAHU) {

            result = calculator.calculatePlanet(
                    year,
                    month,
                    day,
                    hour,
                    SweConst.SE_MEAN_NODE
            );

        } else if (planet == Planet.KETU) {

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


        // -----------------------------------------------------
        // Tropical longitude
        // -----------------------------------------------------

        double tropicalLongitude = result[0];


        /*
         * Ketu is exactly 180 degrees opposite Rahu.
         */

        if (planet == Planet.KETU) {

            tropicalLongitude += 180.0;

            if (tropicalLongitude >= 360.0) {
                tropicalLongitude -= 360.0;
            }
        }


        // -----------------------------------------------------
        // Lahiri Ayanamsa
        // -----------------------------------------------------

        double ayanamsa =
                ayanamsaCalculator.calculateLahiriAyanamsa(
                        year,
                        month,
                        day,
                        hour
                );


        // -----------------------------------------------------
        // Tropical -> Sidereal
        // -----------------------------------------------------

        double siderealLongitude =
                siderealCalculator.calculateSiderealLongitude(
                        tropicalLongitude,
                        ayanamsa
                );


        // -----------------------------------------------------
        // Sidereal Rashi
        // -----------------------------------------------------

        String sign =
                siderealCalculator.calculateSign(
                        siderealLongitude
                );


        // -----------------------------------------------------
        // Nakshatra
        // -----------------------------------------------------

        Nakshatra nakshatra =
                nakshatraCalculator.calculateNakshatra(
                        siderealLongitude
                );


        // -----------------------------------------------------
        // Pada
        // -----------------------------------------------------

        int pada =
                nakshatraCalculator.calculatePada(
                        siderealLongitude
                );


        // -----------------------------------------------------
        // Final response
        // -----------------------------------------------------

        return new SiderealPlanetPosition(
                planet.name(),
                tropicalLongitude,
                ayanamsa,
                siderealLongitude,
                sign,
                nakshatra.name(),
                pada
        );
    }


    // =========================================================
    // 2. CALCULATE PLANET → HOUSE POSITIONS
    // =========================================================

    public List<PlanetHousePosition> calculatePlanetHousePositions(
            int year,
            int month,
            int day,
            double hour,
            double latitude,
            double longitude
    ) {

        // -----------------------------------------------------
        // 1. Lahiri Ayanamsa
        // -----------------------------------------------------

        double ayanamsa =
                ayanamsaCalculator.calculateLahiriAyanamsa(
                        year,
                        month,
                        day,
                        hour
                );


        // -----------------------------------------------------
        // 2. Tropical houses
        // -----------------------------------------------------

        double[] tropicalHouses =
                houseCalculator.calculateHouses(
                        year,
                        month,
                        day,
                        hour,
                        latitude,
                        longitude
                );


        // -----------------------------------------------------
        // 3. Tropical → Sidereal houses
        // -----------------------------------------------------

        double[] siderealHouses = new double[13];

        for (int i = 1; i <= 12; i++) {

            double value =
                    tropicalHouses[i] - ayanamsa;

            if (value < 0) {
                value += 360.0;
            }

            siderealHouses[i] = value;
        }


        // -----------------------------------------------------
        // 4. Calculate planets
        // -----------------------------------------------------

        List<PlanetHousePosition> result =
                new ArrayList<>();

        for (Planet planet : Planet.values()) {

            SiderealPlanetPosition planetPosition =
                    calculateSiderealPosition(
                            planet,
                            year,
                            month,
                            day,
                            hour
                    );

            double planetLongitude =
                    planetPosition.siderealLongitude();


            // -------------------------------------------------
            // 5. Find planet's house
            // -------------------------------------------------

            int house =
                    findHouse(
                            planetLongitude,
                            siderealHouses
                    );


            result.add(
                    new PlanetHousePosition(
                            planet,
                            planetLongitude,
                            planetPosition.sign(),
                            planetPosition.nakshatra(),
                            planetPosition.pada(),
                            house
                    )
            );
        }

        return result;
    }


    // =========================================================
    // 3. FIND HOUSE FOR PLANET
    // =========================================================

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

            // Handle 0° crossing
            if (longitude < start) {
                longitude += 360.0;
            }

            if (longitude >= start && longitude < end) {
                return house;
            }
        }

        return 12;
    }


    // =========================================================
    // 4. COMPLETE SIDEREAL BIRTH CHART
    // =========================================================

    public SiderealBirthChart calculateSiderealBirthChart(
            int year,
            int month,
            int day,
            double hour,
            double latitude,
            double longitude
    ) {

        // =====================================================
        // 1. Calculate Sidereal Planets
        // =====================================================

        List<SiderealPlanetPosition> planets =
                new ArrayList<>();

        for (Planet planet : Planet.values()) {

            planets.add(
                    calculateSiderealPosition(
                            planet,
                            year,
                            month,
                            day,
                            hour
                    )
            );
        }


        // =====================================================
        // 2. Calculate Lahiri Ayanamsa
        // =====================================================

        double ayanamsa =
                ayanamsaCalculator.calculateLahiriAyanamsa(
                        year,
                        month,
                        day,
                        hour
                );


        // =====================================================
        // 3. Calculate Tropical Houses
        // =====================================================

        double[] tropicalHouses =
                houseCalculator.calculateHouses(
                        year,
                        month,
                        day,
                        hour,
                        latitude,
                        longitude
                );


        // =====================================================
        // 4. Convert Tropical Houses → Sidereal Houses
        // =====================================================

        double[] siderealHouses = new double[13];

        for (int i = 1; i <= 12; i++) {

            double siderealLongitude =
                    tropicalHouses[i] - ayanamsa;

            if (siderealLongitude < 0) {
                siderealLongitude += 360.0;
            }

            siderealHouses[i] = siderealLongitude;
        }


        // =====================================================
        // 5. Calculate Tropical Ascendant
        // =====================================================

        double tropicalAscendant =
                houseCalculator.calculateAscendant(
                        year,
                        month,
                        day,
                        hour,
                        latitude,
                        longitude
                );


        // =====================================================
        // 6. Convert Ascendant → Sidereal
        // =====================================================

        double siderealAscendant =
                tropicalAscendant - ayanamsa;

        if (siderealAscendant < 0) {
            siderealAscendant += 360.0;
        }


        // =====================================================
        // 7. Sidereal Ascendant Sign
        // =====================================================

        String ascendantSign =
                getZodiacSign(siderealAscendant);


        // =====================================================
        // 8. Build Sidereal Houses
        // =====================================================

        List<HousePosition> houses =
                new ArrayList<>();

        for (int i = 1; i <= 12; i++) {

            double houseLongitude =
                    siderealHouses[i];

            houses.add(
                    new HousePosition(
                            i,
                            houseLongitude,
                            getZodiacSign(houseLongitude)
                    )
            );
        }


        // =====================================================
        // 9. Calculate Planet → House Positions
        // =====================================================

        List<PlanetHousePosition> planetHousePositions =
                new ArrayList<>();

        for (SiderealPlanetPosition planet : planets) {

            int house =
                    planetHouseCalculator.calculateHouse(
                            planet.siderealLongitude(),
                            siderealHouses
                    );

            planetHousePositions.add(
                    new PlanetHousePosition(
                            Planet.valueOf(planet.planet()),
                            planet.siderealLongitude(),
                            planet.sign(),
                            planet.nakshatra(),
                            planet.pada(),
                            house
                    )
            );
        }


        // =====================================================
        // 10. Calculate House → Lord → Lord House
        // =====================================================

        List<HouseLordPosition> houseLordPositions =
                houseLordCalculator.calculateHouseLordPositions(
                        houses,
                        planetHousePositions
                );


        // =====================================================
        // 11. Calculate Career Indicators
        // =====================================================

        List<CareerIndicator> careerIndicators =
                careerIndicatorCalculator.calculateCareerIndicators(
                        houseLordPositions
                );


        // =====================================================
        // 12. Final Sidereal Birth Chart
        // =====================================================

        return new SiderealBirthChart(
                planets,
                houses,
                planetHousePositions,
                houseLordPositions,
                careerIndicators,
                siderealAscendant,
                ascendantSign
        );
    }


    // =========================================================
    // 5. CAREER HOUSE ANALYSIS
    // =========================================================

    public List<CareerHouseAnalysis> calculateCareerHouseAnalysis(
            int year,
            int month,
            int day,
            double hour,
            double latitude,
            double longitude
    ) {

        // Get complete sidereal chart
        SiderealBirthChart chart =
                calculateSiderealBirthChart(
                        year,
                        month,
                        day,
                        hour,
                        latitude,
                        longitude
                );


        List<CareerHouseAnalysis> result =
                new ArrayList<>();


        // Important career houses
        int[] careerHouses = {
                2, 6, 10, 11
        };


        for (int houseNumber : careerHouses) {

            // -------------------------------------------------
            // 1. Find house
            // -------------------------------------------------

            HousePosition house =
                    chart.houses()
                            .stream()
                            .filter(
                                    h -> h.house() == houseNumber
                            )
                            .findFirst()
                            .orElseThrow(
                                    () -> new IllegalStateException(
                                            "House not found: "
                                                    + houseNumber
                                    )
                            );


            // -------------------------------------------------
            // 2. Calculate House Lord
            // -------------------------------------------------

            Planet lord =
                    houseLordCalculator.calculateHouseLord(
                            house.sign()
                    );


            // -------------------------------------------------
            // 3. Find Lord's House
            // -------------------------------------------------

            int lordHouse =
                    chart.planetHousePositions()
                            .stream()
                            .filter(
                                    p -> p.planet() == lord
                            )
                            .map(
                                    PlanetHousePosition::house
                            )
                            .findFirst()
                            .orElse(0);


            // -------------------------------------------------
            // 4. Find planets in this house
            // -------------------------------------------------

            List<Planet> planetsInHouse =
                    chart.planetHousePositions()
                            .stream()
                            .filter(
                                    p -> p.house() == houseNumber
                            )
                            .map(
                                    PlanetHousePosition::planet
                            )
                            .toList();


            // -------------------------------------------------
            // 5. Create analysis
            // -------------------------------------------------

            result.add(
                    new CareerHouseAnalysis(
                            houseNumber,
                            house.sign(),
                            lord,
                            lordHouse,
                            planetsInHouse
                    )
            );
        }

        return result;
    }


    // =========================================================
    // 6. HOUSE LORD POSITIONS
    // =========================================================

    public List<HouseLordPosition> calculateHouseLordPositions(
            List<HousePosition> houses,
            List<PlanetHousePosition> planetHousePositions
    ) {

        List<HouseLordPosition> result =
                new ArrayList<>();


        for (HousePosition house : houses) {

            // -------------------------------------------------
            // Find lord of house sign
            // -------------------------------------------------

            Planet lord =
                    houseLordCalculator.calculateHouseLord(
                            house.sign()
                    );


            // -------------------------------------------------
            // Find which house the lord occupies
            // -------------------------------------------------

            int lordHouse = 0;

            for (PlanetHousePosition planetPosition :
                    planetHousePositions) {

                if (planetPosition.planet() == lord) {

                    lordHouse =
                            planetPosition.house();

                    break;
                }
            }


            // -------------------------------------------------
            // Create HouseLordPosition
            // -------------------------------------------------

            result.add(
                    new HouseLordPosition(
                            house.house(),
                            house.sign(),
                            lord,
                            lordHouse
                    )
            );
        }

        return result;
    }


    // =========================================================
    // 7. TROPICAL PLANET POSITION
    // =========================================================

    public PlanetPosition calculatePlanetPosition(
            Planet planet,
            int year,
            int month,
            int day,
            double hour
    ) {

        // -----------------------------------------------------
        // Rahu
        // -----------------------------------------------------

        if (planet == Planet.RAHU) {

            double[] result =
                    calculator.calculatePlanet(
                            year,
                            month,
                            day,
                            hour,
                            SweConst.SE_MEAN_NODE
                    );

            double longitude = result[0];
            double speed = result[3];

            return new PlanetPosition(
                    "Rahu",
                    longitude,
                    speed,
                    getZodiacSign(longitude)
            );
        }


        // -----------------------------------------------------
        // Ketu
        // -----------------------------------------------------

        if (planet == Planet.KETU) {

            double[] result =
                    calculator.calculatePlanet(
                            year,
                            month,
                            day,
                            hour,
                            SweConst.SE_MEAN_NODE
                    );

            double longitude =
                    result[0] + 180.0;

            if (longitude >= 360.0) {
                longitude -= 360.0;
            }

            double speed = result[3];

            return new PlanetPosition(
                    "Ketu",
                    longitude,
                    speed,
                    getZodiacSign(longitude)
            );
        }


        // -----------------------------------------------------
        // Normal planets
        // -----------------------------------------------------

        int planetId =
                getPlanetId(planet);


        double[] result =
                calculator.calculatePlanet(
                        year,
                        month,
                        day,
                        hour,
                        planetId
                );


        double longitude = result[0];
        double speed = result[3];


        String sign =
                getZodiacSign(longitude);


        return new PlanetPosition(
                formatPlanetName(planet),
                longitude,
                speed,
                sign
        );
    }


    // =========================================================
    // 8. CAREER SCORE
    // =========================================================

    public CareerScore calculateCareerScore(
            int year,
            int month,
            int day,
            double hour,
            double latitude,
            double longitude
    ) {

        List<CareerHouseAnalysis> analysis =
                calculateCareerHouseAnalysis(
                        year,
                        month,
                        day,
                        hour,
                        latitude,
                        longitude
                );


        return careerScoreCalculator.calculate(
                analysis
        );
    }


    // =========================================================
    // 9. CAREER FIELD ANALYSIS
    // =========================================================

    public List<CareerFieldScore> calculateCareerFieldAnalysis(
            int year,
            int month,
            int day,
            double hour,
            double latitude,
            double longitude
    ) {

        // -----------------------------------------------------
        // 1. Get complete sidereal chart
        // -----------------------------------------------------

        SiderealBirthChart chart =
                calculateSiderealBirthChart(
                        year,
                        month,
                        day,
                        hour,
                        latitude,
                        longitude
                );


        // -----------------------------------------------------
        // 2. Calculate Career Fields
        // -----------------------------------------------------
        //
        // IMPORTANT:
        // CareerFieldCalculator now expects:
        //
        // List<PlanetHousePosition>
        // List<HouseLordPosition>
        //
        // Therefore we directly pass these two lists
        // from the complete chart.
        // -----------------------------------------------------

        return careerFieldCalculator.calculate(
                chart.planetHousePositions(),
                chart.houseLordPositions()
        );
    }

     // =========================================================
     // JOB TIMING ANALYSIS
    // =========================================================

// =========================================================
// JOB TIMING ANALYSIS
// =========================================================

    public List<JobTimingPrediction> calculateJobTiming(
            int year,
            int month,
            int day,
            double hour,
            double latitude,
            double longitude
    ) {

        // =====================================================
        // 1. Complete Sidereal Chart
        // =====================================================

        SiderealBirthChart chart =
                calculateSiderealBirthChart(
                        year,
                        month,
                        day,
                        hour,
                        latitude,
                        longitude
                );


        // =====================================================
        // 2. Find Sidereal Moon
        // =====================================================

        SiderealPlanetPosition moon =
                chart.planets()
                        .stream()
                        .filter(
                                p -> p.planet()
                                        .equals(Planet.MOON.name())
                        )
                        .findFirst()
                        .orElseThrow(
                                () -> new IllegalStateException(
                                        "Sidereal Moon position not found"
                                )
                        );


        // =====================================================
        // 3. Prediction Window = Birth Year + 70 Years
        // =====================================================

        double predictionEndYear =
                year + 70.0;


        // =====================================================
        // 4. Calculate Mahadashas
        // =====================================================

        List<DashaPeriod> mahadashas =
                vimshottariDashaCalculator.calculateMahadashas(
                        moon.siderealLongitude(),
                        year,
                        9
                );


        // =====================================================
        // 5. Keep Mahadashas within 70 Years
        // =====================================================

        List<DashaPeriod> filteredMahadashas =
                mahadashas.stream()
                        .filter(
                                dasha ->
                                        dasha.startYear()
                                                < predictionEndYear
                        )
                        .map(
                                dasha ->
                                        new DashaPeriod(
                                                dasha.planet(),
                                                dasha.startYear(),
                                                Math.min(
                                                        dasha.endYear(),
                                                        predictionEndYear
                                                )
                                        )
                        )
                        .toList();


        // =====================================================
        // 6. Calculate Antardashas
        // =====================================================

        List<AntardashaPeriod> antardashas =
                new ArrayList<>();


        for (DashaPeriod mahadasha :
                filteredMahadashas) {

            antardashas.addAll(
                    antardashaCalculator.calculate(
                            mahadasha
                    )
            );
        }


        // =====================================================
        // 7. Keep Antardashas within 70 Years
        // =====================================================

        antardashas =
                antardashas.stream()
                        .filter(
                                antardasha ->
                                        antardasha.startYear()
                                                < predictionEndYear
                        )
                        .map(
                                antardasha ->
                                        new AntardashaPeriod(
                                                antardasha.mahadashaLord(),
                                                antardasha.antardashaLord(),
                                                antardasha.startYear(),
                                                Math.min(
                                                        antardasha.endYear(),
                                                        predictionEndYear
                                                )
                                        )
                        )
                        .toList();


        // =====================================================
        // 8. Calculate Pratyantardashas
        // =====================================================

        List<PratyantardashaPeriod> pratyantardashas =
                new ArrayList<>();


        for (AntardashaPeriod antardasha :
                antardashas) {

            pratyantardashas.addAll(
                    pratyantardashaCalculator
                            .calculate(
                                    antardasha
                            ));
        }


        // =====================================================
        // 9. Keep Pratyantardashas within 70 Years
        // =====================================================

        pratyantardashas =
                pratyantardashas.stream()
                        .filter(
                                pratyantardasha ->
                                        pratyantardasha.startYear()
                                                < predictionEndYear
                        )
                        .map(
                                pratyantardasha ->
                                        new PratyantardashaPeriod(
                                                pratyantardasha.mahadashaLord(),
                                                pratyantardasha.antardashaLord(),
                                                pratyantardasha.pratyantardashaLord(),
                                                pratyantardasha.startYear(),
                                                Math.min(
                                                        pratyantardasha.endYear(),
                                                        predictionEndYear
                                                )
                                        )
                        )
                        .toList();


        // =====================================================
        // 10. Career Scoring at Pratyantardasha Level
        // =====================================================

        return pratyantardashaCareerCalculator.calculate(
                pratyantardashas,
                chart.houseLordPositions()
        );
    }

    public CareerPrediction calculateCompleteCareerPrediction(
            int year,
            int month,
            int day,
            double hour,
            double latitude,
            double longitude
    ) {

        // =====================================================
        // 1. Career Field Analysis
        // =====================================================

        List<CareerFieldScore> careerFields =
                calculateCareerFieldAnalysis(
                        year,
                        month,
                        day,
                        hour,
                        latitude,
                        longitude
                );


        // =====================================================
        // 2. Calculate all Job Timing internally
        // =====================================================

        List<JobTimingPrediction> allJobTimings =
                calculateJobTiming(
                        year,
                        month,
                        day,
                        hour,
                        latitude,
                        longitude
                );


        // =====================================================
        // 3. Keep only TOP 5 career periods
        // =====================================================
// =====================================================
// 3. Select strongest FUTURE career periods
// =====================================================

        double currentYear =
                java.time.LocalDate.now().getYear();

        List<JobTimingPrediction> bestPeriods =
                allJobTimings.stream()

                        // Ignore historical periods
                        .filter(period ->
                                period.endYear() >= currentYear
                        )

                        // Keep reasonably strong periods
                        .filter(period ->
                                period.score() >= 70
                        )

                        // Highest score first
                        .sorted(
                                (a, b) -> {

                                    int scoreCompare =
                                            Integer.compare(
                                                    b.score(),
                                                    a.score()
                                            );

                                    if (scoreCompare != 0) {
                                        return scoreCompare;
                                    }

                                    return Double.compare(
                                            a.startYear(),
                                            b.startYear()
                                    );
                                }
                        )

                        .limit(5)

                        .toList();


        // =====================================================
        // 4. Current Dasha
        // =====================================================

        List<JobTimingPrediction> currentDashaList =
                currentDashaCalculator.findCurrentDasha(
                        allJobTimings
                );

        JobTimingPrediction currentDasha =
                currentDashaList.isEmpty()
                        ? null
                        : currentDashaList.get(0);


        // =====================================================
        // 5. Overall Career Score
        // =====================================================

        CareerScore careerScore =
                calculateCareerScore(
                        year,
                        month,
                        day,
                        hour,
                        latitude,
                        longitude
                );


        // =====================================================
        // 6. Career Conclusion
        // =====================================================

        CareerConclusion conclusion =
                careerConclusionCalculator.calculate(
                        careerScore,
                        bestPeriods,
                        currentDasha
                );
        // =====================================================
        // 7. Job Opportunity Analysis
        // =====================================================

        JobOpportunityPrediction jobOpportunity =
                jobOpportunityCalculator.calculate(
                        allJobTimings,
                        currentDasha
                );


        // =====================================================
        // 7. Final Response
        // =====================================================

        return new CareerPrediction(
                careerFields,
                bestPeriods,
                currentDasha,
                careerScore,
                conclusion,
                jobOpportunity
        );
    }



    // =========================================================
    // 10. COMPLETE TROPICAL BIRTH CHART
    // =========================================================

    public BirthChart calculateBirthChart(
            int year,
            int month,
            int day,
            double hour,
            double latitude,
            double longitude
    ) {

        // =====================================================
        // 1. Calculate all planets
        // =====================================================

        List<PlanetPosition> planets =
                new ArrayList<>();


        for (Planet planet : Planet.values()) {

            planets.add(
                    calculatePlanetPosition(
                            planet,
                            year,
                            month,
                            day,
                            hour
                    )
            );
        }


        // =====================================================
        // 2. Calculate 12 house cusps
        // =====================================================

        double[] houseData =
                houseCalculator.calculateHouses(
                        year,
                        month,
                        day,
                        hour,
                        latitude,
                        longitude
                );


        // =====================================================
        // 3. Calculate Ascendant
        // =====================================================

        double ascendant =
                houseCalculator.calculateAscendant(
                        year,
                        month,
                        day,
                        hour,
                        latitude,
                        longitude
                );


        String ascendantSign =
                getZodiacSign(ascendant);


        // =====================================================
        // 4. Build 12 houses
        // =====================================================

        List<HousePosition> houses =
                new ArrayList<>();


        for (int i = 1; i <= 12; i++) {

            double houseLongitude =
                    houseData[i];


            houses.add(
                    new HousePosition(
                            i,
                            houseLongitude,
                            getZodiacSign(houseLongitude)
                    )
            );
        }


        // =====================================================
        // 5. Return complete Birth Chart
        // =====================================================

        return new BirthChart(
                planets,
                houses,
                ascendant,
                ascendantSign
        );
    }


    // =========================================================
    // 11. SWISS EPHEMERIS PLANET ID
    // =========================================================

    private int getPlanetId(Planet planet) {

        return switch (planet) {

            case SUN ->
                    SweConst.SE_SUN;

            case MOON ->
                    SweConst.SE_MOON;

            case MERCURY ->
                    SweConst.SE_MERCURY;

            case VENUS ->
                    SweConst.SE_VENUS;

            case MARS ->
                    SweConst.SE_MARS;

            case JUPITER ->
                    SweConst.SE_JUPITER;

            case SATURN ->
                    SweConst.SE_SATURN;

            case RAHU, KETU ->
                    throw new IllegalArgumentException(
                            "Rahu and Ketu are handled separately"
                    );
        };
    }


    // =========================================================
    // 12. ZODIAC SIGN
    // =========================================================

    private String getZodiacSign(
            double longitude
    ) {

        String[] signs = {

                "Aries",
                "Taurus",
                "Gemini",
                "Cancer",
                "Leo",
                "Virgo",
                "Libra",
                "Scorpio",
                "Sagittarius",
                "Capricorn",
                "Aquarius",
                "Pisces"
        };


        int index =
                (int) (longitude / 30.0);


        return signs[index];
    }


    // =========================================================
    // 13. PLANET NAME FORMAT
    // =========================================================

    private String formatPlanetName(
            Planet planet
    ) {

        String name =
                planet.name();


        return name.charAt(0)
                + name.substring(1).toLowerCase();
    }
}