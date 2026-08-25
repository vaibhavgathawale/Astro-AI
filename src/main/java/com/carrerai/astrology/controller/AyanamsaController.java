package com.carrerai.astrology.controller;

import com.carrerai.astrology.calculator.AyanamsaCalculator;
import com.carrerai.astrology.model.Planet;
import com.carrerai.astrology.model.SiderealBirthChart;
import com.carrerai.astrology.model.SiderealPlanetPosition;
import com.carrerai.service.PlanetCalculationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/astrology")
public class AyanamsaController {

    private final AyanamsaCalculator ayanamsaCalculator;
    private final PlanetCalculationService planetCalculationService;

    public AyanamsaController(
            AyanamsaCalculator ayanamsaCalculator,
            PlanetCalculationService planetCalculationService
    ) {
        this.ayanamsaCalculator = ayanamsaCalculator;
        this.planetCalculationService = planetCalculationService;
    }

    // =========================================================
    // 1. GET LAHIRI AYANAMSA
    // =========================================================

    @GetMapping("/ayanamsa")
    public double getAyanamsa(

            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day,
            @RequestParam double hour

    ) {

        return ayanamsaCalculator.calculateLahiriAyanamsa(
                year,
                month,
                day,
                hour
        );
    }

    // =========================================================
    // 2. GET SINGLE PLANET SIDEREAL POSITION
    // =========================================================

    @GetMapping("/sidereal")
    public SiderealPlanetPosition getSiderealPosition(

            @RequestParam Planet planet,
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day,
            @RequestParam double hour

    ) {

        return planetCalculationService.calculateSiderealPosition(
                planet,
                year,
                month,
                day,
                hour
        );
    }

    // =========================================================
    // 3. GET COMPLETE SIDEREAL BIRTH CHART
    // =========================================================

    @GetMapping("/sidereal-chart")
    public SiderealBirthChart getSiderealBirthChart(

            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day,
            @RequestParam double hour,
            @RequestParam double latitude,
            @RequestParam double longitude

    ) {

        return planetCalculationService.calculateSiderealBirthChart(
                year,
                month,
                day,
                hour,
                latitude,
                longitude
        );
    }
}