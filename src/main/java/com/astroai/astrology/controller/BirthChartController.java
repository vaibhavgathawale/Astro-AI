package com.astroai.astrology.controller;

import com.astroai.astrology.model.*;
import com.astroai.career.service.PlanetCalculationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@RequestMapping("/api/chart")
public class BirthChartController {

    private final PlanetCalculationService planetCalculationService;

    public BirthChartController(
            PlanetCalculationService planetCalculationService
    ) {
        this.planetCalculationService = planetCalculationService;
    }

    // =========================================================
    // 1. COMPLETE SIDEREAL BIRTH CHART
    // =========================================================

    @GetMapping("/sidereal")
    public SiderealBirthChart calculateSiderealBirthChart(

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

    // =========================================================
    // 2. COMPLETE TROPICAL BIRTH CHART
    // =========================================================

    @GetMapping
    public BirthChart calculateBirthChart(

            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day,
            @RequestParam double hour,
            @RequestParam double latitude,
            @RequestParam double longitude

    ) {

        return planetCalculationService.calculateBirthChart(
                year,
                month,
                day,
                hour,
                latitude,
                longitude
        );
    }

    @GetMapping("/career-analysis")
    public List<CareerHouseAnalysis> calculateCareerHouseAnalysis(

            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day,
            @RequestParam double hour,
            @RequestParam double latitude,
            @RequestParam double longitude

    ) {

        return planetCalculationService.calculateCareerHouseAnalysis(
                year,
                month,
                day,
                hour,
                latitude,
                longitude
        );
    }

    @GetMapping("/career-fields")
    public List<CareerFieldScore> calculateCareerFields(

            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day,
            @RequestParam double hour,
            @RequestParam double latitude,
            @RequestParam double longitude

    ) {

        return planetCalculationService.calculateCareerFieldAnalysis(
                year,
                month,
                day,
                hour,
                latitude,
                longitude
        );
    }


    @GetMapping("/career-score")
    public CareerScore calculateCareerScore(

            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day,
            @RequestParam double hour,
            @RequestParam double latitude,
            @RequestParam double longitude

    ) {

        return planetCalculationService.calculateCareerScore(
                year,
                month,
                day,
                hour,
                latitude,
                longitude
        );
    }
}