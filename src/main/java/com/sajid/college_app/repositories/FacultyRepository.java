package com.sajid.college_app.repositories;

import com.sajid.college_app.models.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    boolean existsByFacultyCode(String facultyCode);

    Optional<Faculty> findByFacultyCode(String facultyCode);

    List<Faculty> findByFacultyCodeIn(Collection<String> codes);
}
