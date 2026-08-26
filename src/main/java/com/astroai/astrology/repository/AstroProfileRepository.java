package com.astroai.astrology.repository;

import com.astroai.astrology.model.AstroProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AstroProfileRepository extends JpaRepository<AstroProfile, Long> {
}
