package com.astroai.astrology.controller;

import com.astroai.dto.AstroProfileRequest;
import com.astroai.dto.AstroProfileResponse;
import com.astroai.astrology.service.AstroProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/astro/profiles")
@RequiredArgsConstructor
public class AstroProfileController {

    private final AstroProfileService service;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AstroProfileResponse createProfile(
            @Valid @RequestBody AstroProfileRequest request) {

        return service.createProfile(request);
    }
}
