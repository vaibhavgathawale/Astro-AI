package com.carrerai.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AstroProfileResponse(

        Long id,
        String name,
        LocalDate dateOfBirth,
        LocalTime timeOfBirth,
        String placeOfBirth
) {
}
