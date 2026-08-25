package com.carrerai.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AstroProfileRequest(

        @NotBlank
        String name,

        @NotNull
        LocalDate dateOfBirth,

        @NotNull
        LocalTime timeOfBirth,

        @NotBlank
        String placeOfBirth
) {
}
