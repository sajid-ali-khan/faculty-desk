package com.sajid.college_app.repositories;

import com.sajid.college_app.models.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchemeRepository extends JpaRepository<Scheme, Integer> {
    boolean existsBySchemeCode(String schemeCode);
    Optional<Scheme > findBySchemeCode(String schemeCode);
}
