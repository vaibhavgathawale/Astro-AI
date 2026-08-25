package com.astroai.astrology.controller;

import com.astroai.astrology.model.CareerPrediction;
import com.astroai.astrology.model.CareerRequest;
import com.astroai.astrology.service.PlanetCalculationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/career")
public class CareerController {

    private final PlanetCalculationService planetCalculationService;

    public CareerController(
            PlanetCalculationService planetCalculationService
    ) {
        this.planetCalculationService =
                planetCalculationService;
    }

    // =========================================================
    // COMPLETE CAREER PREDICTION
    // =========================================================

    @PostMapping("/predict")
    public CareerPrediction predictCareer(
            @RequestBody CareerRequest request
    ) {

        return planetCalculationService
                .calculateCompleteCareerPrediction(
                        request.year(),
                        request.month(),
                        request.day(),
                        request.hour(),
                        request.latitude(),
                        request.longitude()
                );
    }
}