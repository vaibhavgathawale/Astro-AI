package com.carrerai.service;

import com.carrerai.entity.AstroProfile;
import com.carrerai.repository.AstroProfileRepository;
import com.carrerai.dto.AstroProfileRequest;
import com.carrerai.dto.AstroProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AstroProfileService {

    private final AstroProfileRepository repository;

    public AstroProfileResponse createProfile(AstroProfileRequest request) {

        AstroProfile profile = AstroProfile.builder()
                .name(request.name())
                .dateOfBirth(request.dateOfBirth())
                .timeOfBirth(request.timeOfBirth())
                .placeOfBirth(request.placeOfBirth())
                .latitude(request.latitude())
                .longitude(request.longitude())
                .build();

        AstroProfile savedProfile = repository.save(profile);

        return mapToResponse(savedProfile);
    }

    private AstroProfileResponse mapToResponse(AstroProfile profile) {

        return new AstroProfileResponse(
                profile.getId(),
                profile.getName(),
                profile.getDateOfBirth(),
                profile.getTimeOfBirth(),
                profile.getPlaceOfBirth(),
                profile.getLatitude(),
                profile.getLongitude()
        );
    }
}