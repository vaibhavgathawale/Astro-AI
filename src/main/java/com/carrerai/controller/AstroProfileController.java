package com.carrerai.controller;

import com.carrerai.dto.AstroProfileRequest;
import com.carrerai.dto.AstroProfileResponse;
import com.carrerai.service.AstroProfileService;
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
