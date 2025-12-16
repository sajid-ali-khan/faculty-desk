package com.sajid.college_app.repositories;

import com.sajid.college_app.models.Faculty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Integer> {
    boolean existsByFacultyCode(String facultyCode);

    Optional<Faculty> findByFacultyCode(String facultyCode);

    List<Faculty> findByFacultyCodeIn(Collection<String> codes);

    @Query("""
            select f from Faculty f
            where lower(f.name) like lower(concat('%', :query, '%'))
               or lower(f.facultyCode) like lower(concat('%', :query, '%'))
            """)
    Page<Faculty> findByQueryContainingIgnoreCase(@Param("query")String query, Pageable pageable);

}
