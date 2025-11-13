package com.sajid.college_app.repositories;

import com.sajid.college_app.models.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchemeRepository extends JpaRepository<Scheme, Integer> {
    boolean existsBySchemeCode(String schemeCode);
}
