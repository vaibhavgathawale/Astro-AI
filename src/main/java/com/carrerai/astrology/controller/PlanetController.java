package com.carrerai.astrology.controller;

import com.carrerai.astrology.model.Planet;
import com.carrerai.astrology.model.PlanetPosition;
import com.carrerai.service.PlanetCalculationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {

    private final PlanetCalculationService planetCalculationService;

    public PlanetController(
            PlanetCalculationService planetCalculationService
    ) {
        this.planetCalculationService = planetCalculationService;
    }

    @GetMapping("/position")
    public PlanetPosition getPlanetPosition(

            @RequestParam Planet planet,

            @RequestParam int year,

            @RequestParam int month,

            @RequestParam int day,

            @RequestParam double hour

    ) {

        return planetCalculationService.calculatePlanetPosition(
                planet,
                year,
                month,
                day,
                hour
        );
    }
}