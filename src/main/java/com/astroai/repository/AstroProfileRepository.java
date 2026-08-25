package com.astroai.repository;

import com.astroai.entity.AstroProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AstroProfileRepository extends JpaRepository<AstroProfile, Long> {
}
